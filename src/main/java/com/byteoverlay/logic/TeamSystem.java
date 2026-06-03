package com.byteoverlay.logic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

// [ByteOverlay Feature] TeamSystem
public class TeamSystem {
    public static boolean ENABLED = true;
    private static final Map<String, EnumChatFormatting> playerTeams = new HashMap<>();

    public static void updateTeams() {
        if (!ENABLED) return;
        playerTeams.clear();
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getNetHandler() == null) return;

        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            if (info == null || info.getGameProfile() == null) continue;
            String name = info.getGameProfile().getName();
            
            // Try to get formatted name to extract color
            String displayName = info.getDisplayName() != null ? info.getDisplayName().getFormattedText() : 
                               ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), name);
            
            EnumChatFormatting color = extractTeamColor(displayName);
            if (color != null) {
                playerTeams.put(name, color);
            }
        }
    }

    public static EnumChatFormatting getTeamColor(String nick) {
        if (!ENABLED) return EnumChatFormatting.GRAY;
        return playerTeams.getOrDefault(nick, EnumChatFormatting.GRAY);
    }

    private static EnumChatFormatting extractTeamColor(String formattedName) {
        if (formattedName == null || !formattedName.contains("§")) return null;
        
        // BedWars format might be: §l§bC§b joaozinhopvp
        // We look for the first letter that represents a team (R, B, G, Y, C, P, W, O, L)
        // and its preceding color code.
        for (int i = 0; i < formattedName.length() - 1; i++) {
            if (formattedName.charAt(i) == '§') {
                char code = formattedName.charAt(i + 1);
                EnumChatFormatting color = getByChar(code);
                
                // If we found a color, check if the next character (after possible other codes) is a team letter
                if (color != null && color.isColor()) {
                    // Look ahead for the team letter
                    for (int j = i + 2; j < formattedName.length(); j++) {
                        char c = formattedName.charAt(j);
                        if (c == '§') {
                            j++; // Skip next code char
                            continue;
                        }
                        if ("RBGYCPWOL".indexOf(Character.toUpperCase(c)) != -1) {
                            return color;
                        }
                        if (Character.isLetterOrDigit(c)) break; // Found something else
                    }
                }
            }
        }
        
        // Fallback to first color code if logic above fails
        for (int i = 0; i < formattedName.length() - 1; i++) {
            if (formattedName.charAt(i) == '§') {
                char code = formattedName.charAt(i + 1);
                EnumChatFormatting color = getByChar(code);
                if (color != null && color.isColor()) return color;
            }
        }
        return null;
    }

    private static EnumChatFormatting getByChar(char code) {
        for (EnumChatFormatting color : EnumChatFormatting.values()) {
            if (color.toString().length() > 1 && color.toString().charAt(1) == code) {
                return color;
            }
        }
        return null;
    }

    public static void remove() {
        ENABLED = false;
        playerTeams.clear();
    }
}
