package models.quest;

import models.enums.PlantType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class QuestStats {
    private int sunsCollectedToday;
    private int zombiesKilledToday;
    private int explosivesUsedToday;
    private boolean usedSymmetryToday;
    private LocalDate lastResetDate;

    private int totalZombiesKilled;
    private int chaptersCleared;
    private int consecutiveWins;

    private int sunsInCurrentLevel;
    private int zombiesKilledInLevel;
    private boolean noLawnmowerUsed;
    private boolean noCactusUsed;
    private int mushroomNightCount;
    private int columnsUsed;
    private int rowsUsed;
    private int emptyRowsAndCols;
    private int frozenZombiesKilled;
    private Map<PlantType, Integer> zombiesKilledByPlant = new HashMap<>();
    private Map<String, Integer> zombiesKilledByFamily = new HashMap<>();
    private int sunProducerPlantsInLevel;

    private int plantsLostInLevel;
    private boolean levelWon;
    private PlantType exclusivePlantUsed;
    private boolean onlyPlantKills;

    private Map<String, Integer> zombiesKilledByChapter = new HashMap<>();

    public QuestStats() {
        resetDailyStats();
        resetLevelStats();
    }

    public void resetDailyStats() {
        this.sunsCollectedToday = 0;
        this.zombiesKilledToday = 0;
        this.explosivesUsedToday = 0;
        this.usedSymmetryToday = false;
        this.lastResetDate = LocalDate.now();
    }

    public void resetLevelStats() {
        this.sunsInCurrentLevel = 0;
        this.zombiesKilledInLevel = 0;
        this.noLawnmowerUsed = true;
        this.noCactusUsed = true;
        this.mushroomNightCount = 0;
        this.columnsUsed = 0;
        this.rowsUsed = 0;
        this.emptyRowsAndCols = 0;
        this.frozenZombiesKilled = 0;
        this.sunProducerPlantsInLevel = 0;
        this.zombiesKilledByPlant.clear();
        this.zombiesKilledByFamily.clear();
        this.plantsLostInLevel = 0;
        this.levelWon = false;
        this.exclusivePlantUsed = null;
        this.onlyPlantKills = true;
    }

    public int getSunsCollectedToday() { return sunsCollectedToday; }
    public void addSunsCollectedToday(int amount) { this.sunsCollectedToday += amount; }

    public int getZombiesKilledToday() { return zombiesKilledToday; }
    public void addZombiesKilledToday(int amount) { this.zombiesKilledToday += amount; }

    public int getTotalZombiesKilled() { return totalZombiesKilled; }
    public void addTotalZombiesKilled(int amount) { this.totalZombiesKilled += amount; }

    public Map<String, Integer> getZombiesKilledByChapter() { return zombiesKilledByChapter; }
    public void addZombiesKilledByChapter(String chapter, int count) {
        zombiesKilledByChapter.put(chapter, zombiesKilledByChapter.getOrDefault(chapter, 0) + count);
    }

    public Map<PlantType, Integer> getZombiesKilledByPlant() { return zombiesKilledByPlant; }
    public void addZombiesKilledByPlant(PlantType plant, int count) {
        zombiesKilledByPlant.put(plant, zombiesKilledByPlant.getOrDefault(plant, 0) + count);

        if (exclusivePlantUsed == null) {
            exclusivePlantUsed = plant;
        } else if (!exclusivePlantUsed.equals(plant)) {
            onlyPlantKills = false;
        }
    }

    public int getPlantsLostInLevel() { return plantsLostInLevel; }
    public void incrementPlantsLost() { this.plantsLostInLevel++; }

    public boolean isLevelWon() { return levelWon; }
    public void setLevelWon(boolean levelWon) { this.levelWon = levelWon; }

    public PlantType getExclusivePlantUsed() { return exclusivePlantUsed; }
    public boolean isOnlyPlantKills() { return onlyPlantKills; }
    public LocalDate getLastResetDate() {return lastResetDate;}

}