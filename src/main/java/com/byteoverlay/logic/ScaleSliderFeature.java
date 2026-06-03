package com.byteoverlay.logic;

import com.byteoverlay.config.ByteConfig;
import net.minecraft.client.renderer.GlStateManager;

// [ByteOverlay Feature] ScaleSliderFeature
public class ScaleSliderFeature {
    public static boolean ENABLED = true;

    /**
     * Applies the global scale to the rendering context.
     * Uses GlStateManager to push matrix and scale.
     */
    public static void applyScale() {
        if (!ENABLED) return;
        float scale = ByteConfig.scale;
        if (scale == 1.0f) return;
        
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);
    }

    /**
     * Ends the scaled rendering context.
     */
    public static void endScale() {
        if (!ENABLED) return;
        if (ByteConfig.scale == 1.0f) return;
        GlStateManager.popMatrix();
    }

    public static void remove() {
        ENABLED = false;
    }
}
