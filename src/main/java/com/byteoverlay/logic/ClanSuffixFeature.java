package com.byteoverlay.logic;

// [ByteOverlay Feature] ClanSuffixFeature
public class ClanSuffixFeature {
    public static boolean ENABLED = true;

    /**
     * Returns the clan as a suffix.
     * Conforme Imagem 4, o clan aparece como sufixo após o nick.
     */
    public static String getClanSuffix(PlayerData data) {
        if (!ENABLED) return "";
        String clan = data.getClanTag();
        if (clan == null || clan.isEmpty() || clan.contains("NONE")) return "";
        
        // Remove colors and brackets for comparison
        String strippedClan = clan.replaceAll("§[0-9a-fklmnor]", "").replace("[", "").replace("]", "");
        String strippedTab = data.getTabName().replaceAll("§[0-9a-fklmnor]", "");
        
        if (strippedTab.contains("[" + strippedClan + "]")) return "";
        
        // Clan already contains color and brackets, e.g., "§6[TAG]"
        return " " + clan;
    }

    public static void remove() {
        ENABLED = false;
    }
}
