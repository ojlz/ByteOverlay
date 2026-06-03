package com.byteoverlay.logic;

// [ByteOverlay Feature] AltDetectorFeature
public class AltDetectorFeature {
    public static boolean ENABLED = true;

    /**
     * Checks if a player is an alt based on their first login date.
     * dias < 5 means it's an alt.
     */
    public static boolean checkIsAlt(Long firstLogin) {
        if (!ENABLED || firstLogin == null) return false;
        long agora = System.currentTimeMillis();
        long diferenca = agora - firstLogin;
        long dias = diferenca / 86400000L;
        return dias < 5;
    }

    public static String getAltSuffix(PlayerData data) {
        if (ENABLED && data.isAlt()) {
            return " §e[ALT]";
        }
        return "";
    }

    public static void remove() {
        ENABLED = false;
    }
}
