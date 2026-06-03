package com.byteoverlay.logic;

import com.byteoverlay.config.ByteConfig;

// [ByteOverlay Feature] SilentModeFeature
public class SilentModeFeature {
    public static boolean ENABLED = true;

    /**
     * Checks if the overlay is visible.
     * If SilentMode is enabled, it returns the value of ByteConfig.enabled.
     * If SilentMode is disabled, it always returns true to maintain original behavior.
     */
    public static boolean isOverlayVisible() {
        if (!ENABLED) return true;
        return ByteConfig.enabled;
    }

    public static void remove() {
        ENABLED = false;
    }
}
