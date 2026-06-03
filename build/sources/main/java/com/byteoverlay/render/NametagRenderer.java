package com.byteoverlay.render;

import com.byteoverlay.cache.CacheManager;
import com.byteoverlay.config.ByteConfig;
import com.byteoverlay.logic.PlayerData;
import com.byteoverlay.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class NametagRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final CacheManager cacheManager;

    public NametagRenderer(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Specials.Post<EntityPlayer> event) {
        if (!ByteConfig.nametagsEnabled) return;
        if (!(event.entity instanceof EntityPlayer)) return;
        
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player == mc.thePlayer) return; // Don't render on self
        
        PlayerData data = cacheManager.get(player.getName());
        if (data == null || data.isFetching()) return;

        renderStats(player, data, event.x, event.y, event.z);
    }

    private void renderStats(EntityPlayer player, PlayerData data, double x, double y, double z) {
        String text;
        if (data.isNicked()) {
            text = "§4§lNICKED";
        } else if (data.isStaff()) {
            text = "§5§lSTAFF";
        } else {
            StringBuilder sb = new StringBuilder();
            
            if (ByteConfig.ntShowLevel) {
                sb.append(data.getLevelBadge() != null ? data.getLevelBadge() : "§a" + data.getLevel()).append(" ");
            }
            
            if (ByteConfig.ntShowFKDR) {
                sb.append("§fFKDR: ").append(getFkdrColor(data.getFkdr())).append(MathUtils.format(data.getFkdr())).append(" ");
            }
            
            if (ByteConfig.ntShowWS) {
                sb.append("§fWS: ").append(getWsColor(data.getWinstreak())).append(data.getWinstreak()).append(" ");
            }
            text = sb.toString().trim();
        }

        if (text.isEmpty()) return;

        RenderManager renderManager = mc.getRenderManager();
        FontRenderer fontRenderer = mc.fontRendererObj;

        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        
        // Increased height offset to avoid overlapping with health and nick
        double heightOffset = player.isSneaking() ? 0.7D : 1.1D;
        
        GlStateManager.translate((float) x, (float) y + player.height + heightOffset, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        int i = fontRenderer.getStringWidth(text) / 2;
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) (-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, 553648127);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
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
}
