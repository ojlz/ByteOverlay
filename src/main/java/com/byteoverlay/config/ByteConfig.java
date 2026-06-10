package com.byteoverlay.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ByteConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    public static boolean enabled = true;
    public static boolean showFKDR = true;
    public static boolean showWS = true;
    public static boolean showWR = true;
    public static boolean showWins = true;
    public static boolean showLosses = true;
    public static boolean showLevel = true;
    public static boolean showClan = true;
    public static boolean showFps = true;
    public static boolean showPing = true;

    public static boolean nametagsEnabled = true;
    public static boolean ntShowFKDR = true;
    public static boolean ntShowWS = true;
    public static boolean ntShowLevel = true;
    public static boolean ntShowHealthBar = true;

    public static int backgroundAlpha = 150; // 0-255
    public static int headerColorIndex = 11; // Aqua
    public static int nickedSoundIndex = 0;
    public static int staffSoundIndex = 1;
    public static float soundVolume = 100f;
    public static int posX = 5;
    public static int posY = 5;
    public static float scale = 1.0f;
    public static List<String> snipers = new ArrayList<>();

    public static void init() {
        configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/byteoverlay.json");
        load();
    }

    public static void load() {
        if (!configFile.exists()) {
            save();
            return;
        }
        try (FileReader reader = new FileReader(configFile)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            if (data != null) {
                enabled = data.enabled;
                showFKDR = data.showFKDR;
                showWS = data.showWS;
                showWR = data.showWR;
                showWins = data.showWins;
                showLosses = data.showLosses;
                showLevel = data.showLevel;
                showClan = data.showClan;
                showFps = data.showFps;
                showPing = data.showPing;
                nametagsEnabled = data.nametagsEnabled;
                ntShowFKDR = data.ntShowFKDR;
                ntShowWS = data.ntShowWS;
                ntShowLevel = data.ntShowLevel;
                ntShowHealthBar = data.ntShowHealthBar;
                backgroundAlpha = data.backgroundAlpha;
                headerColorIndex = data.headerColorIndex;
                nickedSoundIndex = data.nickedSoundIndex;
                staffSoundIndex = data.staffSoundIndex;
                soundVolume = data.soundVolume;
                posX = data.posX;
                posY = data.posY;
                scale = data.scale;
                if (data.snipers != null) snipers = data.snipers;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            ConfigData data = new ConfigData();
            data.enabled = enabled;
            data.showFKDR = showFKDR;
            data.showWS = showWS;
            data.showWR = showWR;
            data.showWins = showWins;
            data.showLosses = showLosses;
            data.showLevel = showLevel;
            data.showClan = showClan;
            data.showFps = showFps;
            data.showPing = showPing;
            data.nametagsEnabled = nametagsEnabled;
            data.ntShowFKDR = ntShowFKDR;
            data.ntShowWS = ntShowWS;
            data.ntShowLevel = ntShowLevel;
            data.ntShowHealthBar = ntShowHealthBar;
            data.backgroundAlpha = backgroundAlpha;
            data.headerColorIndex = headerColorIndex;
            data.nickedSoundIndex = nickedSoundIndex;
            data.staffSoundIndex = staffSoundIndex;
            data.soundVolume = soundVolume;
            data.posX = posX;
            data.posY = posY;
            data.scale = scale;
            data.snipers = snipers;
            GSON.toJson(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ConfigData {
        boolean enabled = true;
        boolean showFKDR = true;
        boolean showWS = true;
        boolean showWR = true;
        boolean showWins = true;
        boolean showLosses = true;
        boolean showLevel = true;
        boolean showClan = true;
        boolean showFps = true;
        boolean showPing = true;
        boolean nametagsEnabled = true;
        boolean ntShowFKDR = true;
        boolean ntShowWS = true;
        boolean ntShowLevel = true;
        boolean ntShowHealthBar = true;
        int backgroundAlpha = 150;
        int headerColorIndex = 11;
        int nickedSoundIndex = 0;
        int staffSoundIndex = 1;
        float soundVolume = 100f;
        int posX = 5;
        int posY = 5;
        float scale = 1.0f;
        List<String> snipers = new ArrayList<>();
    }

    public static String getHeaderColor() {
        String[] codes = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        return "§" + codes[headerColorIndex];
    }

    public static String getSound(int index) {
        String[] sounds = {"random.levelup", "mob.wither.death", "note.pling", "random.orb", "random.click"};
        return sounds[index];
    }
}
