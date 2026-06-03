package com.byteoverlay.logic;

import net.minecraft.util.EnumChatFormatting;

// [ByteOverlay Feature] NickFormatterFeature
public class NickFormatterFeature {
    public static boolean ENABLED = true;

    /**
     * Formats the nick according to status and captures team colors.
     * Ensures fixed alignment for the level badge.
     */
    public static String formatNick(PlayerData data) {
        if (!ENABLED) return data.getTabName();

        String displayName = propagateTeamColor(data.getTabName());
        
        if (data.isStaff()) {
            return getAlignedLevel(data) + displayName + " §5[staff]";
        }
        if (data.isNicked()) {
            return getAlignedLevel(data) + displayName + " §4[nicked]";
        }

        StringBuilder sb = new StringBuilder();
        
        // Aligned [BADGE_LEVEL]
        sb.append(getAlignedLevel(data));

        sb.append(displayName);
        
        // Add Clan Suffix (Feature 7)
        sb.append(ClanSuffixFeature.getClanSuffix(data));
        
        // Add Alt Suffix (Feature 6)
        sb.append(AltDetectorFeature.getAltSuffix(data));
        
        // Add Sniper Suffix (Feature 4 & 5)
        sb.append(SniperListFeature.getSniperSuffix(data));

        return sb.toString();
    }

    /**
     * Returns the level badge. 
     * Note: Pure alignment is handled in OverlayRenderer by splitting prefix and name
     * because FontRenderer width varies by character. 
     * Here we just ensure we return the pieces needed.
     */
    public static String getLevelPart(PlayerData data) {
        String badge = data.getLevelBadge();
        if (badge == null || badge.isEmpty()) {
            return "§a[" + data.getLevel() + "✶] ";
        }
        return badge + " ";
    }

    private static String getAlignedLevel(PlayerData data) {
        return getLevelPart(data);
    }

    private static String propagateTeamColor(String tabName) {
        if (tabName == null || !tabName.contains("§")) return tabName;
        
        String color = "";
        String bold = "";
        
        for (int i = 0; i < tabName.length() - 1; i++) {
            if (tabName.charAt(i) == '§') {
                char code = tabName.charAt(i + 1);
                EnumChatFormatting ecf = getByChar(code);
                if (ecf != null) {
                    if (ecf.isColor()) {
                        color = "§" + code;
                    } else if (ecf == EnumChatFormatting.BOLD) {
                        bold = "§" + code;
                    }
                }
            }
            if (Character.isLetterOrDigit(tabName.charAt(i)) && tabName.charAt(i) != '§') break;
        }
        
        if (color.isEmpty()) return tabName;

        String[] parts = tabName.split(" ", 2);
        if (parts.length > 1) {
            String teamPart = parts[0];
            String namePart = parts[1];
            if (namePart.startsWith("§f") || namePart.startsWith("§r") || !namePart.contains("§")) {
                return teamPart + " " + color + bold + namePart.replaceFirst("§[fr]", "");
            }
        }
        
        return tabName;
    }

    private static EnumChatFormatting getByChar(char code) {
        for (EnumChatFormatting ecf : EnumChatFormatting.values()) {
            if (ecf.toString().length() > 1 && ecf.toString().charAt(1) == code) return ecf;
        }
        return null;
    }

    public static void remove() {
        ENABLED = false;
    }
}
