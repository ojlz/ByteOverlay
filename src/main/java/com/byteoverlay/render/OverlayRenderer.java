package com.byteoverlay.render;

import com.byteoverlay.cache.CacheManager;
import com.byteoverlay.config.ByteConfig;
import com.byteoverlay.logic.*;
import com.byteoverlay.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class OverlayRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final CacheManager cacheManager;
    private final PlayerAnalyzer analyzer;
    
    private List<PlayerData> lastPlayers = new ArrayList<>();
    private PlayerData lastMyData = null;
    private Object lastWorld = null;

    public OverlayRenderer(CacheManager cacheManager, PlayerAnalyzer analyzer) {
        this.cacheManager = cacheManager;
        this.analyzer = analyzer;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        if (mc.theWorld != lastWorld) {
            lastWorld = mc.theWorld;
            analyzer.onWorldChange();
        }

        updatePlayerData();
        
        if (ByteConfig.enabled) {
            drawOverlay();
        }
    }

    private void updatePlayerData() {
        if (mc.getNetHandler() == null) return;
        Collection<NetworkPlayerInfo> playersMap = mc.getNetHandler().getPlayerInfoMap();
        if (playersMap == null) return;
        
        List<PlayerData> dataList = new ArrayList<>();
        PlayerData myData = null;
        String myNick = mc.getSession().getProfile().getName();
        
        List<NetworkPlayerInfo> players = new ArrayList<>(playersMap);

        for (NetworkPlayerInfo info : players) {
            if (info == null || info.getGameProfile() == null) continue;
            String name = info.getGameProfile().getName();
            if (name == null || isBot(info, name)) continue;

            if (!cacheManager.contains(name)) {
                if (ByteConfig.enabled || ByteConfig.nametagsEnabled) {
                    analyzer.analyze(name);
                }
            }
            
            PlayerData data = cacheManager.get(name);
            if (data != null) {
                String displayName = info.getDisplayName() != null ? info.getDisplayName().getFormattedText() : 
                                   net.minecraft.scoreboard.ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), name);
                data.setTabName(displayName);

                if (name.equalsIgnoreCase(myNick)) myData = data;
                else dataList.add(data);
            }
        }
        
        // --- TEAM GROUPING ---
        // Sort players by team color to group them
        dataList.sort((p1, p2) -> {
            String c1 = extractColorCode(p1.getTabName());
            String c2 = extractColorCode(p2.getTabName());
            return c1.compareTo(c2);
        });

        this.lastPlayers = dataList;
        this.lastMyData = myData;
    }

    private String extractColorCode(String text) {
        if (text == null || !text.contains("§")) return "§f";
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == '§') {
                char c = text.charAt(i + 1);
                for (EnumChatFormatting ecf : EnumChatFormatting.values()) {
                    if (ecf.isColor() && ecf.toString().charAt(1) == c) return ecf.toString();
                }
            }
        }
        return "§f";
    }

    private void drawOverlay() {
        ScaleSliderFeature.applyScale();
        float scaleInv = 1.0f / ByteConfig.scale;
        
        FontRenderer fr = mc.fontRendererObj;
        float x = ByteConfig.posX * scaleInv;
        float y = ByteConfig.posY * scaleInv;
        
        int count = lastPlayers.size();
        
        // --- ALIGNMENT LOGIC ---
        int maxLevelWidth = 0;
        int maxNameOnlyWidth = 100;
        for (PlayerData p : lastPlayers) {
            String fullNick = NickFormatterFeature.formatNick(p);
            String levelPart = NickFormatterFeature.getLevelPart(p);
            String nameOnlyPart = fullNick.substring(levelPart.length());
            int lw = fr.getStringWidth(levelPart);
            int nw = fr.getStringWidth(nameOnlyPart);
            if (lw > maxLevelWidth) maxLevelWidth = lw;
            if (nw > maxNameOnlyWidth) maxNameOnlyWidth = nw;
        }
        
        int nameColTotalWidth = maxLevelWidth + maxNameOnlyWidth + 10;

        int width = nameColTotalWidth;
        if (ByteConfig.showFKDR) width += 45;
        if (ByteConfig.showWS) width += 40;
        if (ByteConfig.showWR) width += 40;
        if (ByteConfig.showWins) width += 45;
        if (ByteConfig.showLosses) width += 45;
        
        int height = 24 + (count * 10) + (lastMyData != null ? 15 : 0);

        int bgColor = (ByteConfig.backgroundAlpha << 24) & 0xFF000000;
        Gui.drawRect((int)x - 2, (int)y - 2, (int)(x + width + 2), (int)(y + height + 2), bgColor);

        float currY = y;
        String hCol = ByteConfig.getHeaderColor();

        fr.drawStringWithShadow("§lByteOverlay", x + (width / 2f) - (fr.getStringWidth("ByteOverlay") / 2f), currY, -1);
        currY += 12;

        float currX = x;
        fr.drawStringWithShadow(hCol + "Name", currX, currY, -1);
        currX += nameColTotalWidth;
        
        if (ByteConfig.showFKDR) { fr.drawStringWithShadow(hCol + "FKDR", currX, currY, -1); currX += 45; }
        if (ByteConfig.showWS) { fr.drawStringWithShadow(hCol + "WS", currX, currY, -1); currX += 40; }
        if (ByteConfig.showWR) { fr.drawStringWithShadow(hCol + "WR", currX, currY, -1); currX += 40; }
        if (ByteConfig.showWins) { fr.drawStringWithShadow(hCol + "Wins", currX, currY, -1); currX += 45; }
        if (ByteConfig.showLosses) { fr.drawStringWithShadow(hCol + "Losses", currX, currY, -1); currX += 45; }
        
        currY += 12;

        for (PlayerData p : lastPlayers) {
            String fullNick = NickFormatterFeature.formatNick(p);
            String levelPart = NickFormatterFeature.getLevelPart(p);
            String nameOnlyPart = fullNick.substring(levelPart.length());
            
            // Draw Level (Aligned at start)
            fr.drawStringWithShadow(levelPart, x, currY, -1);
            
            // Draw Name (Aligned after max level width)
            fr.drawStringWithShadow(nameOnlyPart, x + maxLevelWidth, currY, -1);
            
            currX = x + nameColTotalWidth;

            if (p.isFetching()) {
                fr.drawStringWithShadow("§7Loading...", currX, currY, -1);
            } else if (p.isNicked()) {
                fr.drawStringWithShadow("§4§lnicked", currX, currY, -1);
            } else if (p.isStaff()) {
                fr.drawStringWithShadow("§5§lstaff", currX, currY, -1);
            } else {
                if (ByteConfig.showFKDR) { fr.drawStringWithShadow(getFkdrColor(p.getFkdr()) + formatStat(p.getFkdr()), currX, currY, -1); currX += 45; }
                if (ByteConfig.showWS) { fr.drawStringWithShadow(getWsColor(p.getWinstreak()) + formatStat(p.getWinstreak()), currX, currY, -1); currX += 40; }
                if (ByteConfig.showWR) {
                    String wrColor = p.getWinRate() >= 0.5 ? "§a" : (p.getWinRate() >= 0.2 ? "§e" : "§7");
                    fr.drawStringWithShadow(wrColor + formatStat(p.getWinRate()), currX, currY, -1);
                    currX += 40;
                }
                if (ByteConfig.showWins) { fr.drawStringWithShadow("§7" + formatStat(p.getWins()), currX, currY, -1); currX += 45; }
                if (ByteConfig.showLosses) { fr.drawStringWithShadow("§7" + formatStat(p.getLosses()), currX, currY, -1); currX += 45; }
            }
            currY += 10;
        }

        boolean hasAnyStat = (lastMyData != null && !lastMyData.isFetching() && (ByteConfig.showFKDR || ByteConfig.showWR))
                          || ByteConfig.showFps || ByteConfig.showPing;
        if (hasAnyStat) {
            currY += 5;
            StringBuilder personal = new StringBuilder("§bSeu ");
            boolean first = true;
            if (lastMyData != null && !lastMyData.isFetching()) {
                if (ByteConfig.showFKDR) {
                    personal.append("FKDR: ").append(getFkdrColor(lastMyData.getFkdr())).append(formatStat(lastMyData.getFkdr()));
                    first = false;
                }
                if (ByteConfig.showWR) {
                    if (!first) personal.append(" §8|");
                    personal.append(" §bWR: §f").append(formatStat(lastMyData.getWinRate()));
                    first = false;
                }
            }
            if (ByteConfig.showFps) {
                if (!first) personal.append(" §8|");
                personal.append(" §bFPS: §f").append(Minecraft.getDebugFPS());
                first = false;
            }
            if (ByteConfig.showPing) {
                if (!first) personal.append(" §8|");
                int ping = 0;
                if (mc.getNetHandler() != null) {
                    NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
                    if (info != null) ping = info.getResponseTime();
                }
                personal.append(" §bPing: §f").append(ping).append("ms");
            }
            fr.drawStringWithShadow(personal.toString().trim(), x, currY, -1);
        }
        ScaleSliderFeature.endScale();
    }

    private String formatStat(double val) {
        if (val == 0.0) return "§8-";
        return MathUtils.format(val);
    }

    private String formatStat(int val) {
        if (val == 0) return "§8-";
        return String.valueOf(val);
    }

    private String getFkdrColor(double val) {
        if (val >= 10.0) return "§c";
        if (val >= 2.0) return "§e";
        return "§7";
    }

    private String getWsColor(int ws) {
        if (ws >= 10) return "§c";
        if (ws >= 5) return "§a";
        return "§7";
    }

    private boolean isBot(NetworkPlayerInfo info, String name) {
        if (name == null) return true;
        if (name.length() == 8 && name.matches("[0-9a-fF]+")) return true;
        if (name.matches("[0-9]{10,}")) return true;
        if (name.contains(" ") || name.startsWith("NPC")) return true;
        return false;
    }

    public List<PlayerData> getLastPlayers() { return lastPlayers; }
    public PlayerData getLastMyData() { return lastMyData; }
}
