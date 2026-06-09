package models.core;

import models.enums.PlantType;

import java.util.HashMap;

public class User {
    private int gold;
    private int diamond;
    private int pot;
    private HashMap<PlantType, Integer> unlockedPlantsLevels;


    public HashMap<PlantType, Integer> getUnlockedPlantsLevels() {
        return unlockedPlantsLevels;
    }
}
