package models.core;

import models.enums.PlantType;
import models.quest.QuestStats;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private QuestStats questStats;

    private String username;
    private String password;

    private int currentLevel;
    private int coins;
    private int gems;
    private int pot;
    private HashMap<PlantType, Integer> UnlockedPlantsLevels;


    public User(){}

    public boolean checkPassword(String password) {
        return false;
    }

    public void addCoins(int amount){}

    public boolean spendCoins(int amount){
        return false;
    }

    public void addGems(int amount){}

    public boolean spendGems(int amount){
        return false;
    }

    public void unlockNewPlant(){}

    public void advanceLevel(){}



}
