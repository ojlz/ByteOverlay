package com.byteoverlay.logic;

public class PlayerData {
    private final String nick;
    private double fkdr;
    private int winstreak;
    private double winRate;
    private int wins;
    private int losses;
    private int level;
    private String levelBadge;
    private boolean isNicked;
    private boolean isStaff;
    private boolean isAlt;
    private boolean isSniper;
    private boolean fetching = true;
    private String tabName;
    private String clanTag = "§7NONE";

    public PlayerData(String nick) {
        this.nick = nick;
        this.tabName = nick;
    }

    // Getters and Setters
    public boolean isSniper() { return isSniper; }
    public void setSniper(boolean sniper) { isSniper = sniper; }
    public boolean isAlt() { return isAlt; }
    public void setAlt(boolean alt) { isAlt = alt; }
    public String getClanTag() { return clanTag; }
    public void setClanTag(String clanTag) { this.clanTag = clanTag; }
    public String getNick() { return nick; }
    public double getFkdr() { return fkdr; }
    public void setFkdr(double fkdr) { this.fkdr = fkdr; }
    public int getWinstreak() { return winstreak; }
    public void setWinstreak(int winstreak) { this.winstreak = winstreak; }
    public double getWinRate() { return winRate; }
    public void setWinRate(double winRate) { this.winRate = winRate; }
    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }
    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public String getLevelBadge() { return levelBadge; }
    public void setLevelBadge(String levelBadge) { this.levelBadge = levelBadge; }
    public boolean isNicked() { return isNicked; }
    public void setNicked(boolean nicked) { isNicked = nicked; }
    public boolean isStaff() { return isStaff; }
    public void setStaff(boolean staff) { isStaff = staff; }
    public boolean isFetching() { return fetching; }
    public void setFetching(boolean fetching) { this.fetching = fetching; }
    public String getTabName() { return tabName; }
    public void setTabName(String tabName) { this.tabName = tabName; }
}
