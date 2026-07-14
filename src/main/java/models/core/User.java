package models.core;

import models.enums.PlantType;
import models.greenhouse.GreenHouse;
import models.quest.QuestStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private HashMap<PlantType, Integer> seedPackets;
    private HashMap<PlantType, Integer> UnlockedPlantsLevels;
    private HashMap<PlantType, Boolean> plantBoosts;
    private static ArrayList<News> allNews;
    private ArrayList<News> unreadNews;
    private GreenHouse greenhouse;
    private transient boolean isLoaded = false;
    private int gameDifficulty = 3;
    private int plantFoods = 0;

    public User(){
        this.plantBoosts = new HashMap<>();
        this.UnlockedPlantsLevels = new HashMap<>();
        this.seedPackets = new HashMap<>();
        this.greenhouse = new GreenHouse();
        this.coins = 100;
        this.gems = 10;
    }

    public void afterLoad() {
        if (this.plantBoosts == null) this.plantBoosts = new HashMap<>();
        if (this.UnlockedPlantsLevels == null) this.UnlockedPlantsLevels = new HashMap<>();
        if (this.seedPackets == null) this.seedPackets = new HashMap<>();
        if (this.greenhouse == null) this.greenhouse = new GreenHouse();
        this.isLoaded = true;
    }

    private void save() {
        // فقط اگر کاربر از فایل لود شده باشد یا جدیداً ثبت‌نام کرده باشد، ذخیره کن
        if (isLoaded || !username.isEmpty()) {
            UserDataManager.saveUser(this);
        }
    }

    public void addSeedPackets(PlantType type, int amount) {
        int currentSeeds = this.seedPackets.getOrDefault(type, 0);
        this.seedPackets.put(type, currentSeeds + amount);
        save();
    }

    public int getSeedPacketsCount(PlantType type) {
        return this.seedPackets.getOrDefault(type, 0);
    }

    public boolean checkPassword(String password) {
        String hashedInput = PasswordHasher.hashSHA256(password);
        return hashedInput.equals(this.hashPassword);
    }

    public void unlockPlant(PlantType plantType) {
        this.UnlockedPlantsLevels.put(plantType, 1);
        save();
    }

    public void addCoins(int amount){
        this.coins += amount;
        save();
    }
    public boolean spendCoins(int amount){
        if (coins < amount) return false;
        coins -= amount;
        save();
        return true;
    }
    public void addGems(int amount){
        this.gems += amount;
        save();
    }
    public boolean spendGems(int amount){
        if (gems < amount) return false;
        gems -= amount;
        save();
        return true;
    }

    public void unlockNewPlant(){}

    public void advanceLevel(){}

    public HashMap<PlantType, Integer> getUnlockedPlantsLevels() {
        return UnlockedPlantsLevels;
    }

    public boolean hasBoost(PlantType type) {return plantBoosts.getOrDefault(type, false);}
    public void addBoost(PlantType type) {plantBoosts.put(type, true); save();}
    public void useBoost(PlantType type) {plantBoosts.put(type, false); save();}

    public List<PlantType> getUnlockedPlantTypesWithPlantFood() {
        List<PlantType> result = new ArrayList<>();
        for (PlantType type : UnlockedPlantsLevels.keySet()) {
            if (type != PlantType.MARIGOLD && hasPlantFoodAbility(type)) {
                result.add(type);
            }
        }
        return result;
    }


    private boolean hasPlantFoodAbility(PlantType type) {
        if (type == PlantType.MARIGOLD) return false;
        switch (type) {
            case GOLD_BLOOM:
            case CHERRY_BOMB:
            case GRAPESHOT:
            case JALAPENO:
            case DOOM_SHROOM:
            case ICE_SHROOM:
            case HOT_POTATO:
            case GRAVE_BUSTER:
                return false;
            default:
                return true;
        }
    }

    public boolean addPlantFood(int count) {
        if (this.plantFoods + count > 3) {
            return false;
        }
        this.plantFoods += count;
        save();
        return true;
    }

    public boolean usePlantFood() {
        if (this.plantFoods > 0) {
            this.plantFoods--;
            save();
            return true;
        }
        return false;
    }

    public GreenHouse getGreenhouse() {return greenhouse;}
    public String getUsername() {
        return username;
    }
    public int getCoins() {return coins;}
    public int getGems() {return gems;}
    public String getNickname() {return nickname;}
    public int getPlantFoods() {return plantFoods;}
    public String getEmail() {return email;}

    public String getEmail() {
        return email;
    }

    public String getSecurityA() {
        return securityA;
    }

    public String getSecurityQ() {
        return securityQ;
    }

    public ArrayList<News> getAllNews() {
        return allNews;
    }

    public ArrayList<News> getUnreadNews() {
        return unreadNews;
    }

    public String getHashPassword() {
        return hashPassword;
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

    public void setGameDifficulty(int gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }
}
