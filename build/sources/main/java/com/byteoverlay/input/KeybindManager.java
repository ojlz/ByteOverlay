package com.byteoverlay.input;

import com.byteoverlay.config.ByteConfig;
import com.byteoverlay.config.ByteGui;
import com.byteoverlay.cache.CacheManager;
import com.byteoverlay.render.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    private final KeyBinding clearCacheKey = new KeyBinding("Limpar Cache", Keyboard.KEY_P, "Byte Overlay");
    private final KeyBinding toggleOverlayKey = new KeyBinding("Ativar/Desativar Overlay", Keyboard.KEY_O, "Byte Overlay");
    private final KeyBinding toggleNametagKey = new KeyBinding("Ativar/Desativar Nametags", Keyboard.KEY_N, "Byte Overlay");
    private final KeyBinding settingsKey = new KeyBinding("Configurações do Overlay", Keyboard.KEY_INSERT, "Byte Overlay");

    private final OverlayRenderer renderer;
    private final CacheManager cacheManager;

    public KeybindManager(OverlayRenderer renderer, CacheManager cacheManager) {
        this.renderer = renderer;
        this.cacheManager = cacheManager;
        ClientRegistry.registerKeyBinding(clearCacheKey);
        ClientRegistry.registerKeyBinding(toggleOverlayKey);
        ClientRegistry.registerKeyBinding(toggleNametagKey);
        ClientRegistry.registerKeyBinding(settingsKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (clearCacheKey.isPressed()) {
            cacheManager.clear();
            sendMessage("§eCache limpo!");
        }

        if (toggleOverlayKey.isPressed()) {
            ByteConfig.enabled = !ByteConfig.enabled;
            ByteConfig.save();
            sendMessage("§bOverlay " + (ByteConfig.enabled ? "§aATIVADO" : "§cDESATIVADO"));
        }

        if (toggleNametagKey.isPressed()) {
            ByteConfig.nametagsEnabled = !ByteConfig.nametagsEnabled;
            ByteConfig.save();
            sendMessage("§bNametags " + (ByteConfig.nametagsEnabled ? "§aATIVADAS" : "§cDESATIVADAS"));
        }

        if (settingsKey.isPressed()) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft.getMinecraft().displayGuiScreen(new ByteGui());
            });
        }
    }

    private void sendMessage(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
}
