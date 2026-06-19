package models.core;

import models.enums.PlantType;
import models.quest.QuestStats;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private QuestStats questStats;

    private String username;
    private String hashPassword;
    private String nickname;
    private String email;
    private String gender;
    private String securityQ;
    private String securityA;

    private int unlockedChapter;
    private int unlockedLevel;
    private int currentLevel;
    private int coins;
    private int gems;
    private int pot;
    private HashMap<PlantType, Integer> UnlockedPlantsLevels;

    private static ArrayList<News> allNews;

    public void unlockPlant(PlantType plantType){
        this.UnlockedPlantsLevels.put(plantType, 1);
    }


    public User(){}

    public String getUsername() {
        return username;
    }

    public void setHashPassword(String hashPassword) { this.hashPassword = hashPassword;}
    public void setUsername(String username) {
        this.username = username;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setEmail(String email) { this.email = email;}
    public void setGender(String gender) { this.gender = gender;}
    public void setSecurityQ(String securityQ) { this.securityQ = securityQ;}
    public void setSecurityA(String securityA) {this.securityA = securityA;}

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

    public HashMap<PlantType, Integer> getUnlockedPlantsLevels() {
        return UnlockedPlantsLevels;
    }
}
