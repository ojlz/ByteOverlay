package com.byteoverlay;

import com.byteoverlay.config.ByteConfig;
import com.byteoverlay.cache.CacheManager;
import com.byteoverlay.input.KeybindManager;
import com.byteoverlay.input.SettingsCommand;
import com.byteoverlay.logic.PlayerAnalyzer;
import com.byteoverlay.render.OverlayRenderer;
import com.byteoverlay.render.NametagRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MainMod.MODID, name = MainMod.NAME, version = MainMod.VERSION, clientSideOnly = true)
public class MainMod {
    public static final String MODID = "byteoverlay";
    public static final String NAME = "Byte Overlay";
    public static final String VERSION = "2.1.4";
    
    public static MainMod instance;

    private CacheManager cacheManager;
    private PlayerAnalyzer analyzer;
    private OverlayRenderer renderer;
    private NametagRenderer nametagRenderer;
    private KeybindManager keybindManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ByteConfig.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;
        
        cacheManager = new CacheManager();
        analyzer = new PlayerAnalyzer(cacheManager);
        renderer = new OverlayRenderer(cacheManager, analyzer);
        nametagRenderer = new NametagRenderer(cacheManager);
        keybindManager = new KeybindManager(renderer, cacheManager);

        // Register events
        MinecraftForge.EVENT_BUS.register(renderer);
        MinecraftForge.EVENT_BUS.register(nametagRenderer);
        MinecraftForge.EVENT_BUS.register(keybindManager);
        
        // Register Command
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new SettingsCommand());
    }

    public OverlayRenderer getRenderer() { return renderer; }
}
