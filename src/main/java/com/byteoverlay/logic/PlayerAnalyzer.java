package com.byteoverlay.logic;

import com.byteoverlay.api.ApiClient;
import com.byteoverlay.api.MushResponse;
import com.byteoverlay.cache.CacheManager;
import com.byteoverlay.config.ByteConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerAnalyzer {
    private final ApiClient apiClient = new ApiClient();
    private final CacheManager cacheManager;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicLong worldToken = new AtomicLong(0);
    private long cooldownUntil = 0;

    public PlayerAnalyzer(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void onWorldChange() {
        worldToken.incrementAndGet();
        cacheManager.clear();
    }

    public void analyze(String nick) {
        if (cacheManager.contains(nick)) return;

        PlayerData data = new PlayerData(nick);
        cacheManager.put(nick, data);
        SniperListFeature.checkSniper(data);

        long token = worldToken.get();

        executor.execute(() -> {
            if (token != worldToken.get()) return;

            try {
                while (System.currentTimeMillis() < cooldownUntil) {
                    Thread.sleep(500);
                    if (token != worldToken.get()) return;
                }

                MushResponse response = apiClient.fetchPlayer(nick);
                if (token != worldToken.get()) return;

                processResponse(data, response);
            } catch (Exception e) {
                data.setFetching(false);
                if (e.getMessage() != null && e.getMessage().contains("429")) {
                    cooldownUntil = System.currentTimeMillis() + 8000;
                    sendMessage("§cRate Limit atingido! Pausando por 8s...");
                } else {
                    System.err.println("[ByteOverlay] Erro ao buscar dados de " + nick + ": " + e.getMessage());
                }
            }
        });
    }

    private void processResponse(PlayerData data, MushResponse response) {
        try {
            if (response == null) {
                data.setFetching(false);
                return;
            }

            if (!response.success && response.errorCode == 404) {
                data.setNicked(true);
                sendMessage("§4§l(!) NICKED DETECTADO: §f" + data.getNick());
                playAlertSound(false);
            } else if (response.success && response.response != null) {
                MushResponse.ResponseData resp = response.response;

                if (resp.firstLogin != null) {
                    data.setAlt(AltDetectorFeature.checkIsAlt(resp.firstLogin));
                }

                if (resp.banned && resp.banBlacklistCount > 0) {
                    data.setStaff(true);
                    sendMessage("§5§l⚠ STAFF DETECTADO ⚠ §r§f" + data.getNick());
                    playAlertSound(true);
                }

                if (resp.clan != null && resp.clan.tag != null) {
                    String colorCode = getMinecraftColorFromHex(resp.clan.tagColor);
                    data.setClanTag(colorCode + "[" + resp.clan.tag + "]");
                }

                if (resp.stats != null && resp.stats.bedwars != null) {
                    MushResponse.BedwarsData bw = resp.stats.bedwars;

                    double fkills = bw.finalKills != null ? bw.finalKills : 0.0;
                    double fdeaths = bw.finalDeaths != null ? bw.finalDeaths : 0.0;
                    double wins = bw.wins != null ? bw.wins : 0.0;
                    double losses = bw.losses != null ? bw.losses : 0.0;

                    data.setFkdr(fdeaths == 0 ? fkills : fkills / fdeaths);
                    data.setWinRate(losses == 0 ? wins : wins / losses);
                    data.setWins((int)wins);
                    data.setLosses((int)losses);
                    data.setWinstreak(bw.winstreak != null ? bw.winstreak.intValue() : 0);
                    data.setLevel(bw.level != null ? bw.level.intValue() : 0);

                    if (bw.levelBadge != null && bw.levelBadge.format != null) {
                        data.setLevelBadge(bw.levelBadge.format.replace("&", "§"));
                    }

                    AutoSniperFeature.checkAutoSniper(data, bw);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.setFetching(false);
    }

    private String getMinecraftColorFromHex(String hex) {
        if (hex == null || hex.isEmpty()) return "§7";
        hex = hex.toLowerCase().replace("#", "");
        switch (hex) {
            case "000000": return "§0"; case "0000aa": return "§1";
            case "00aa00": return "§2"; case "00aaaa": return "§3";
            case "aa0000": return "§4"; case "aa00aa": return "§5";
            case "ffaa00": return "§6"; case "aaaaaa": return "§7";
            case "555555": return "§8"; case "5555ff": return "§9";
            case "55ff55": return "§a"; case "55ffff": return "§b";
            case "ff5555": return "§c"; case "ff55ff": return "§d";
            case "ffff55": return "§e"; case "ffffff": return "§f";
            default: return "§7";
        }
    }

    private void playAlertSound(boolean isStaff) {
        if (!SilentModeFeature.isOverlayVisible()) return;
        final Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            if (mc.thePlayer != null) {
                String sound = ByteConfig.getSound(isStaff ? ByteConfig.staffSoundIndex : ByteConfig.nickedSoundIndex);
                float volume = ByteConfig.soundVolume / 100f;
                mc.thePlayer.playSound(sound, volume, isStaff ? 1.0F : 0.5F);
            }
        });
    }

    public void sendMessage(final String message) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            if (mc.thePlayer != null) {
                mc.thePlayer.addChatMessage(new ChatComponentText(message));
            }
        });
    }
}
