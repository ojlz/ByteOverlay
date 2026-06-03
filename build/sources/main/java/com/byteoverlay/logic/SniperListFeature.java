package com.byteoverlay.logic;

import com.byteoverlay.config.ByteConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.HashSet;
import java.util.Set;

// [ByteOverlay Feature] SniperListFeature
public class SniperListFeature {
    public static boolean ENABLED = true;
    private static final Set<String> alertedNicks = new HashSet<>();
    private static String lastWorldId = "";

    public static void checkSniper(PlayerData data) {
        if (!ENABLED) return;
        
        String nick = data.getNick().toLowerCase();
        boolean isSniper = false;
        for (String s : ByteConfig.snipers) {
            if (s.equalsIgnoreCase(nick)) {
                isSniper = true;
                break;
            }
        }
        
        if (isSniper) {
            data.setSniper(true);
            sendAlert(data.getNick(), false);
        }
    }

    public static void sendAlert(String nick, boolean automatic) {
        if (!ENABLED) return;
        
        // Clear alert list if world changed (new match)
        String currentWorld = getWorldId();
        if (!currentWorld.equals(lastWorldId)) {
            alertedNicks.clear();
            lastWorldId = currentWorld;
        }

        if (alertedNicks.contains(nick.toLowerCase())) return;

        alertedNicks.add(nick.toLowerCase());
        
        String message = automatic ? "§c(!) SNIPER DETECTADO AUTOMATICAMENTE: §f" + nick 
                                  : "§c(!) SNIPER DETECTADO: §f" + nick;
        
        sendMessage(message);
    }

    private static String getWorldId() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return "";
        return mc.theWorld.getWorldInfo().getWorldName() + ":" + mc.theWorld.getTotalWorldTime() / 24000L;
    }

    private static void sendMessage(String message) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public static String getSniperSuffix(PlayerData data) {
        if (ENABLED && data.isSniper()) {
            return " §c[SNIPER]";
        }
        return "";
    }

    public static void remove() {
        ENABLED = false;
        alertedNicks.clear();
    }
}
