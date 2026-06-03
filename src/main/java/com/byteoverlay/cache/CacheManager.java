package com.byteoverlay.cache;

import com.byteoverlay.logic.PlayerData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
    // Persistent cache for the entire session
    private final Map<String, PlayerData> sessionCache = new ConcurrentHashMap<>();

    public PlayerData get(String nick) {
        return sessionCache.get(nick.toLowerCase());
    }

    public void put(String nick, PlayerData data) {
        sessionCache.put(nick.toLowerCase(), data);
    }

    public void clear() {
        sessionCache.clear();
    }

    public boolean contains(String nick) {
        return sessionCache.containsKey(nick.toLowerCase());
    }
}
