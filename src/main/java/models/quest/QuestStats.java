package models.quest;

import models.enums.PlantFamily;
import models.enums.PlantType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private int explosivePlantsUsedInLevel;
    private boolean symmetryAchieved;
    private PlantFamily exclusiveFamilyUsed;
    private boolean onlyFamilyKills;
    private long firstWaveStartTime;
    private int zombiesKilledInFirstWave;
    private boolean firstWaveStarted;
    private int finalSunCount;

    private Set<Integer> emptyColumnsInLevel;
    private Set<Integer> emptyRowsInLevel;
    private int emptyColumnForCross;
    private int emptyRowForCross;
    private int lawnmowerKills;

    private Set<PlantFamily> familiesUsedInLevel = new HashSet<>();
    private int mushroomPlantsUsedInLevel;
    private int totalPlantsUsedInLevel;
    private int consecutiveWinsMaxDifficulty;
    private int zombiesKilledInFirstColumnWithoutMower;

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
        this.explosivePlantsUsedInLevel = 0;
        this.symmetryAchieved = false;
        this.exclusiveFamilyUsed = null;
        this.onlyFamilyKills = true;
        this.firstWaveStartTime = 0;
        this.zombiesKilledInFirstWave = 0;
        this.firstWaveStarted = false;
        this.finalSunCount = -1;
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
        this.familiesUsedInLevel.clear();
        this.mushroomPlantsUsedInLevel = 0;
        this.totalPlantsUsedInLevel = 0;
        this.consecutiveWinsMaxDifficulty = 0;
        this.zombiesKilledInFirstColumnWithoutMower = 0;
        this.sunProducerPlantsInLevel = 0;
        this.emptyColumnsInLevel = new HashSet<>();
        this.emptyRowsInLevel = new HashSet<>();
        this.emptyColumnForCross = -1;
        this.emptyRowForCross = -1;
        this.lawnmowerKills = 0;
    }

    public int getSunsCollectedToday() {
        return sunsCollectedToday;
    }

    public void addSunsCollectedToday(int amount) {
        this.sunsCollectedToday += amount;
    }

    public void addZombiesKilledToday(int amount) {
        this.zombiesKilledToday += amount;
    }

    public int getTotalZombiesKilled() {
        return totalZombiesKilled;
    }

    public void addTotalZombiesKilled(int amount) {
        this.totalZombiesKilled += amount;
    }

    public Map<String, Integer> getZombiesKilledByChapter() {
        return zombiesKilledByChapter;
    }

    public void addZombiesKilledByChapter(String chapter, int count) {
        zombiesKilledByChapter.put(chapter, zombiesKilledByChapter.getOrDefault(chapter, 0) + count);
    }

    public Map<PlantType, Integer> getZombiesKilledByPlant() {
        return zombiesKilledByPlant;
    }

    public void addZombiesKilledByPlant(PlantType plant, int count) {
        zombiesKilledByPlant.put(plant, zombiesKilledByPlant.getOrDefault(plant, 0) + count);

        if (exclusivePlantUsed == null) {
            exclusivePlantUsed = plant;
        } else if (!exclusivePlantUsed.equals(plant)) {
            onlyPlantKills = false;
        }
    }

    public int getPlantsLostInLevel() {
        return plantsLostInLevel;
    }

    public void incrementPlantsLost() {
        this.plantsLostInLevel++;
    }

    public boolean isLevelWon() {
        return levelWon;
    }

    public void setLevelWon(boolean levelWon) {
        this.levelWon = levelWon;
    }

    public PlantType getExclusivePlantUsed() {
        return exclusivePlantUsed;
    }

    public boolean isOnlyPlantKills() {
        return onlyPlantKills;
    }

    public LocalDate getLastResetDate() {
        return lastResetDate;
    }

    public int getExplosivePlantsUsedInLevel() {
        return explosivePlantsUsedInLevel;
    }

    public void incrementExplosivePlantsUsed() {
        this.explosivePlantsUsedInLevel++;
    }

    public boolean isSymmetryAchieved() {
        return symmetryAchieved;
    }

    public void setSymmetryAchieved(boolean symmetryAchieved) {
        this.symmetryAchieved = symmetryAchieved;
    }

    public PlantFamily getExclusiveFamilyUsed() {
        return exclusiveFamilyUsed;
    }

    public boolean isOnlyFamilyKills() {
        return onlyFamilyKills;
    }

    public void addZombieKilledByFamily(PlantFamily family) {
        if (exclusiveFamilyUsed == null) {
            exclusiveFamilyUsed = family;
        } else if (!exclusiveFamilyUsed.equals(family)) {
            onlyFamilyKills = false;
        }
    }

    public long getFirstWaveStartTime() {
        return firstWaveStartTime;
    }

    public void setFirstWaveStartTime(long time) {
        this.firstWaveStartTime = time;
        this.firstWaveStarted = true;
    }

    public int getZombiesKilledInFirstWave() {
        return zombiesKilledInFirstWave;
    }

    public void incrementZombiesKilledInFirstWave() {
        this.zombiesKilledInFirstWave++;
    }

    public boolean isFirstWaveStarted() {
        return firstWaveStarted;
    }

    public int getFinalSunCount() {
        return finalSunCount;
    }

    public void setFinalSunCount(int finalSunCount) {
        this.finalSunCount = finalSunCount;
    }

    public Set<PlantFamily> getFamiliesUsedInLevel() {
        return familiesUsedInLevel;
    }

    public void addFamilyUsedInLevel(PlantFamily family) {
        familiesUsedInLevel.add(family);
    }

    public int getMushroomPlantsUsedInLevel() {
        return mushroomPlantsUsedInLevel;
    }

    public void incrementMushroomPlantsUsed() {
        this.mushroomPlantsUsedInLevel++;
    }

    public int getTotalPlantsUsedInLevel() {
        return totalPlantsUsedInLevel;
    }

    public void incrementTotalPlantsUsed() {
        this.totalPlantsUsedInLevel++;
    }

    public int getConsecutiveWinsMaxDifficulty() {
        return consecutiveWinsMaxDifficulty;
    }

    public void incrementConsecutiveWinsMaxDifficulty() {
        this.consecutiveWinsMaxDifficulty++;
    }

    public void resetConsecutiveWinsMaxDifficulty() {
        this.consecutiveWinsMaxDifficulty = 0;
    }

    public int getZombiesKilledInFirstColumnWithoutMower() {
        return zombiesKilledInFirstColumnWithoutMower;
    }

    public void incrementZombiesKilledInFirstColumnWithoutMower() {
        this.zombiesKilledInFirstColumnWithoutMower++;
    }

    public int getSunProducerPlantsInLevel() {
        return sunProducerPlantsInLevel;
    }

    public void incrementSunProducerPlantsInLevel() {
        this.sunProducerPlantsInLevel++;
    }

    public Set<Integer> getEmptyColumnsInLevel() {
        return emptyColumnsInLevel;
    }

    public void addEmptyColumnInLevel(int col) {
        emptyColumnsInLevel.add(col);
    }

    public Set<Integer> getEmptyRowsInLevel() {
        return emptyRowsInLevel;
    }

    public void addEmptyRowInLevel(int row) {
        emptyRowsInLevel.add(row);
    }

    public int getEmptyColumnForCross() {
        return emptyColumnForCross;
    }

    public void setEmptyColumnForCross(int col) {
        this.emptyColumnForCross = col;
    }

    public int getEmptyRowForCross() {
        return emptyRowForCross;
    }

    public void setEmptyRowForCross(int row) {
        this.emptyRowForCross = row;
    }

    public int getLawnmowerKills() {
        return lawnmowerKills;
    }

    public void incrementLawnmowerKills() {
        this.lawnmowerKills++;
    }
}