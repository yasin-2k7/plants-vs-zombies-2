package models.quest;

import models.plant.Plant;

import java.time.LocalDate;
import java.util.Map;

public class QuestStats {
    //روزانه
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
    private Map<Class<? extends Plant>, Integer> zombiesKilledByPlant;
    private Map<String, Integer> zombiesKilledByFamily;
    private int sunProducerPlantsInLevel;

    public void resetDailyStats(){

    }

    public void resetLevelStats(){

    }

}
