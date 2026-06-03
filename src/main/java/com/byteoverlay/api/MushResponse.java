package com.byteoverlay.api;

import com.google.gson.annotations.SerializedName;

public class MushResponse {
    public boolean success;
    @SerializedName("error_code")
    public int errorCode;
    public ResponseData response;

    public static class ResponseData {
        public String nick;
        @SerializedName("first_login")
        public Long firstLogin;
        public boolean banned;
        @SerializedName("ban_blacklist_count")
        public int banBlacklistCount;
        public StatsData stats;
        public ClanData clan;
    }

    public static class ClanData {
        public String name;
        public String tag;
        @SerializedName("tag_color")
        public String tagColor; // Hex like #aa00aa
    }

    public static class StatsData {
        public BedwarsData bedwars;
    }

    public static class BedwarsData {
        @SerializedName("final_kills")
        public Double finalKills;
        @SerializedName("final_deaths")
        public Double finalDeaths;
        public Double winstreak;
        public Double wins;
        public Double losses;
        public Double level;
        @SerializedName("level_badge")
        public LevelBadge levelBadge;
    }

    public static class LevelBadge {
        public String format;
    }
}
