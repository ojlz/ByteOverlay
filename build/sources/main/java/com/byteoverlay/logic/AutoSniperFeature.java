package com.byteoverlay.logic;

import com.byteoverlay.api.MushResponse;

// [ByteOverlay Feature] AutoSniperFeature
public class AutoSniperFeature {
    public static boolean ENABLED = true;

    /**
     * Checks if a player is an automatic sniper based on stats.
     * Criterion: losses > (final_deaths * 1.5)
     */
    public static boolean checkAutoSniper(PlayerData data, MushResponse.BedwarsData bw) {
        if (!ENABLED || bw == null) return false;
        
        // If already a manual sniper, don't trigger auto sniper logic for alert duplication
        if (data.isSniper()) return false;

        double losses = bw.losses != null ? bw.losses : 0.0;
        double finalDeaths = bw.finalDeaths != null ? bw.finalDeaths : 0.0;

        if (losses > (finalDeaths * 1.5)) {
            data.setSniper(true);
            SniperListFeature.sendAlert(data.getNick(), true);
            return true;
        }
        
        return false;
    }

    public static void remove() {
        ENABLED = false;
    }
}
