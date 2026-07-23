package models.core;

import models.enums.Chapter;
import models.enums.NewsType;
import models.enums.PlantType;
import models.greenhouse.GreenHouse;
import models.miniGame.MiniGameLevels;
import models.quest.Quest;
import models.quest.QuestManager;
import models.quest.QuestStats;
import models.quest.types.DailyQuest;

import java.util.*;

public class User {
    private QuestStats questStats;

    private String username;
    private String hashPassword;
    private String nickname;
    private String email;
    private String gender;
    private String securityQ;
    private String securityA;

    private int gamesPlayed;
    private int highScore;

    private Set<MiniGameLevels> completedMiniGames = EnumSet.noneOf(MiniGameLevels.class);

    private int unlockedChapter;
    private transient Chapter currentChapter;
    private int unlockedLevel;
    private int currentLevel;
    private int userLevel;
    private int coins;
    private int gems;
    private int pot;
    private HashMap<PlantType, Integer> seedPackets;
    private HashMap<PlantType, Integer> unlockedPlantsLevels;
    private HashMap<PlantType, Boolean> plantBoosts;
    private HashMap<String, Boolean> showedZombies;
    private ArrayList<News> unreadNews;
    private List<News> newsList = new ArrayList<>();
    private GreenHouse greenhouse;
    private transient boolean isLoaded = false;
    private int gameDifficulty = 3;
    private int plantFoods = 0;
    private transient QuestManager questManager = new QuestManager();
    private Set<String> completedQuestIds = new HashSet<>();
    private int maxMupoint = 0;


    public User(){
        this.plantBoosts = new HashMap<>();
        this.unlockedPlantsLevels = new HashMap<>();
        this.seedPackets = new HashMap<>();
        this.showedZombies = new HashMap<>();
        this.greenhouse = new GreenHouse();
        this.coins = 10000;
        this.gems = 1000;
        this.unlockedChapter = 1;
        this.unlockedLevel = 1;
        this.questStats = new QuestStats();
        gamesPlayed = 0;
        highScore = 0;
        putInitialPlants();
        putZombies();
    }

    private void putZombies(){
        showedZombies.put("ZombieDefault", false);
        showedZombies.put("ZombieConeHead", false);
        showedZombies.put("ZombieBucketHead", false);
        showedZombies.put("ZombieBrickHead", false);
        showedZombies.put("ZombieKnight", false);
        showedZombies.put("ZombieGargantuar", false);
        showedZombies.put("ZombieImp", false);
        showedZombies.put("ZombieRa", false);
        showedZombies.put("ZombieExplorer", false);
        showedZombies.put("ZombieTombRaiser", false);
        showedZombies.put("ZombieIceAgeDodo", false);
        showedZombies.put("ZombieIceAgeHunter", false);
        showedZombies.put("ZombieIceAgeTroglobite", false);
        showedZombies.put("ZombieBeachFisherman", false);
        showedZombies.put("ZombieBeachOctopus", false);
        showedZombies.put("ZombieBeachSnorkel", false);
        showedZombies.put("ZombieDarkJuggler", false);
        showedZombies.put("ZombieWizard", false);
        showedZombies.put("ZombieDarkKing", false);
        showedZombies.put("ZombieDarkImpDragon", false);
        showedZombies.put("ZombieModernAllStar", false);
        showedZombies.put("ZombieLostCityJane", false);
        showedZombies.put("ZombieCrystalSkull", false);
        showedZombies.put("ZombieProspector", false);
        showedZombies.put("ZombiePiano", false);
        showedZombies.put("ZombieArcade", false);
        showedZombies.put("ZombieNewspaper", false);
    }

    private void putInitialPlants(){
        unlockedPlantsLevels.put(PlantType.SUNFLOWER, 1);
        unlockedPlantsLevels.put(PlantType.PEASHOOTER, 1);
        unlockedPlantsLevels.put(PlantType.CABBAGE_PULT, 1);
        unlockedPlantsLevels.put(PlantType.POTATO_MINE, 1);
        unlockedPlantsLevels.put(PlantType.CHERRY_BOMB, 1);
        unlockedPlantsLevels.put(PlantType.ICEBERG_LETTUCE, 1);
        unlockedPlantsLevels.put(PlantType.WALL_NUT, 1);
        unlockedPlantsLevels.put(PlantType.GRAVE_BUSTER, 1);
        unlockedPlantsLevels.put(PlantType.REPEATER, 1);
        unlockedPlantsLevels.put(PlantType.SNOW_PEA, 1);
        unlockedPlantsLevels.put(PlantType.LILY_PAD, 1);
    }

    public void afterLoad() {
        if (this.plantBoosts == null) this.plantBoosts = new HashMap<>();
        if (this.unlockedPlantsLevels == null) this.unlockedPlantsLevels = new HashMap<>();
        if (this.seedPackets == null) this.seedPackets = new HashMap<>();
        if (this.greenhouse == null) this.greenhouse = new GreenHouse();
        if (this.questStats == null) this.questStats = new QuestStats();
        this.isLoaded = true;
        if (this.questManager == null) {
            this.questManager = new QuestManager();
        }
        if (this.completedQuestIds == null) {
            this.completedQuestIds = new HashSet<>();
        }
    }

    public void initQuests() {
        questManager.generateMainQuests(this);
        questManager.generateEpicQuests(this);
        questManager.resetDailyIfNeeded(this);
        questManager.generateDailyQuests(this);

        for (Quest q : questManager.getActiveQuests()) {
            if (completedQuestIds.contains(q.getId())) {
                q.setCompleted(true);
            }
        }
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

    public HashMap<PlantType, Integer> getSeedPackets() {
        return seedPackets;
    }

    public boolean checkPassword(String password) {
        String hashedInput = PasswordHasher.hashSHA256(password);
        return hashedInput.equals(this.hashPassword);
    }

    public void unlockPlant(PlantType plantType) {
        this.unlockedPlantsLevels.put(plantType, 1);
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
        return unlockedPlantsLevels;
    }

    public boolean hasBoost(PlantType type) {return plantBoosts.getOrDefault(type, false);}
    public void addBoost(PlantType type) {plantBoosts.put(type, true); save();}
    public void useBoost(PlantType type) {plantBoosts.put(type, false); save();}

    public List<PlantType> getUnlockedPlantTypesWithPlantFood() {
        List<PlantType> result = new ArrayList<>();
        for (PlantType type : unlockedPlantsLevels.keySet()) {
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

    public void setPlantFoods(int count){
        this.plantFoods = count;
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

    public String getSecurityA() {
        return securityA;
    }

    public boolean checkSeqA(String answer) {
        String hashedInput = PasswordHasher.hashSHA256(answer);
        return hashedInput.equals(this.securityA);
    }

    public String getSecurityQ() {
        return securityQ;
    }

    public List<News> getAllNews() {
        return newsList;
    }

    public ArrayList<News> getUnreadNews() {
        return (ArrayList<News>) newsList.stream()
                .filter(n -> !n.isRead())
                .toList();
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public Chapter getCurrentChapter() {
        return currentChapter;
    }

    public int getGameDifficulty() {
        return gameDifficulty;
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
        if(gameDifficulty < 1 || gameDifficulty > 5){
            throw new IllegalArgumentException("Difficulty level must be between 1 and 5");
        }
        this.gameDifficulty = gameDifficulty;
    }

    public void setCurrentChapter(Chapter currentChapter) {
        this.currentChapter = currentChapter;
    }

    public int getUnlockedChapter() {
        return unlockedChapter;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUnlockedLevel() {
        return unlockedLevel;
    }

    public void addNews(News news){
        newsList.add(news);
    }

    public HashMap<String, Boolean> getShowedZombies() {
        return showedZombies;
    }

    public void notifyPlantUnlock(String plantName){
        addNews(new News(
                "Unlock plant",
                "plant" + plantName,
                NewsType.PLANT_UNLOCKED
        ));
    }

    public void notifyZombieUnlock(String zombieName){
        addNews(new News(
                "Unlock zombie",
                "zombie" + zombieName,
                NewsType.ZOMBIE_UNLOCKED
        ));
    }

    public void notifyLevelUnlock(String levelName){
        addNews(new News(
                "Unlock new level",
                "level" + levelName,
                NewsType.LEVEL_UNLOCKED
        ));
    }

    public void notifyMinigameUnlocked(String minigameName){
        addNews(new News(
                "Unlock minigame",
                "minigame" + minigameName,
                NewsType.MINIGAME_UNLOCKED
        ));
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public int getPot() {return pot;}

    public QuestStats getQuestStats() { return questStats; }
    public Set<String> getCompletedQuestIds() { return completedQuestIds; }
    public void addCompletedQuest(String questId) {
        completedQuestIds.add(questId);
        save();
    }

    public void unlockLevel() {
        int newLevel = unlockedLevel == 4 ? 1 : unlockedLevel+1;
        int newChapter = newLevel == 1 ? unlockedChapter+1 : unlockedChapter;
        unlockedLevel = newLevel;
        unlockedChapter = newChapter;
        notifyLevelUnlock(newChapter + "-" + newLevel);
        UserDataManager.saveUser(this);
    }



    public void setCoins(int coins) {
        this.coins = coins;
    }

    public HashMap<PlantType, Boolean> getPlantBoosts() {
        return plantBoosts;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public int getMaxMupoint() {
        return maxMupoint;
    }

    public void setMaxMupoint(int maxMupoint) {
        this.maxMupoint = maxMupoint;
    }

    public void updateMupointRecord(int currentScore) {
        if (currentScore > this.maxMupoint) {
            this.maxMupoint = currentScore;
        }
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getNormalQuestsCount() {
        if (completedQuestIds == null) return 0;
        return (int) completedQuestIds.stream()
                .filter(id -> !id.startsWith("daily_"))
                .count();
    }

    public int getDailyQuestsCount() {
        if (completedQuestIds == null) return 0;
        return (int) completedQuestIds.stream()
                .filter(id -> id.startsWith("daily_"))
                .count();
    }

    public int getHighScore() {
        return highScore;
    }

    public int getCompletedMainLevels() {return (unlockedChapter-1)*4 + unlockedLevel-1;}

    public int getCompletedLevels() {
        return completedMiniGames.size() + (unlockedChapter-1)*4 + unlockedLevel-1;
    }

    public Set<MiniGameLevels> getMiniGameLevels() {
        return completedMiniGames;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }
}
