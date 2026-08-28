package com.pvz2.network.messages;

public class LeaderboardEntry {
    public String username;
    public int unlockedChapter;
    public int unlockedLevel;
    public int miniGamesCompleted;
    public int dailyQuestsCount;
    public int normalQuestsCount;
    public int maxMupoint;
    public boolean hasPlayedMuPoint; // the flag you're adding — distinguishes "never played" from a real 0 score

    public int getCompletedMainLevels() {
        return (unlockedChapter - 1) * 4 + unlockedLevel - 1;
    }


    public LeaderboardEntry() {}
}
