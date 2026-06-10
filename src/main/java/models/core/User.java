package models.core;

import models.quest.QuestStats;
import java.util.HashMap;
import models.enums.PlantType;


public class User {
    private QuestStats questStats;
    private int gold;
    private int diamond;
    private int pot;
    private HashMap<PlantType, Integer> UnlockedPlantsLevels;


    public HashMap<PlantType, Integer> getUnlockedPlantsLevels() {
        return UnlockedPlantsLevels;
    }
}
