package models.quest;

import models.enums.PlantType;
import models.quest.reward.*;
import models.quest.types.DailyQuest;
import models.quest.types.MainQuest;

import java.util.function.Predicate;

public class QuestFactory {

    // 1. Daily Sun Collector
    public static DailyQuest createDailySunCollectorQuest(int sunAmount) {
        String id = "daily_sun_" + sunAmount;
        String desc = "Collect " + sunAmount + " sun units in one day";
        Predicate<QuestStats> condition = stats -> stats.getSunsCollectedToday() >= sunAmount;
        int coinReward = sunAmount / 100;
        Reward reward = new CurrencyReward(coinReward, 0);
        return new DailyQuest(id, desc, QuestPriority.MEDIUM, condition, reward);
    }

    // 2. Chapter Hunter
    public static MainQuest createChapterHunterQuest(String chapter) {
        String id = "main_hunter_" + chapter;
        String desc = "Defeat 50 zombies from " + chapter + " chapter";
        Predicate<QuestStats> condition = stats ->
                stats.getZombiesKilledByChapter().getOrDefault(chapter, 0) >= 50;
        Reward reward = new RandomSeedPacketReward(10);
        return new MainQuest(id, desc, condition, reward);
    }

    // 3. Professional Plant Killer
    public static DailyQuest createPlantKillerQuest(PlantType plant) {
        String id = "daily_plant_killer_" + plant.name();
        String desc = "Kill 10 zombies only with " + plant.name();
        Predicate<QuestStats> condition = stats ->
                stats.getZombiesKilledByPlant().getOrDefault(plant, 0) >= 10 &&
                        stats.isOnlyPlantKills() &&
                        stats.getExclusivePlantUsed() == plant;
        Reward reward = new RandomUnlockReward();
        return new DailyQuest(id, desc, QuestPriority.HIGH, condition, reward);
    }

    // 4. Only Cactus
    public static DailyQuest createCactusOnlyQuest() {
        String id = "daily_cactus_only";
        String desc = "Kill 10 zombies only with Cactus";
        Predicate<QuestStats> condition = stats ->
                stats.getZombiesKilledByPlant().getOrDefault(PlantType.CACTUS, 0) >= 10 &&
                        stats.isOnlyPlantKills() &&
                        stats.getExclusivePlantUsed() == PlantType.CACTUS;
        Reward reward = new CurrencyReward(0, 20);
        return new DailyQuest(id, desc, QuestPriority.HIGH, condition, reward);
    }

    // 5. Economic Vegetarian
    public static MainQuest createEconomicVegetarianQuest(int n) {
        String id = "main_eco_" + n;
        String desc = "Win a level without losing more than " + n + " plants";
        Predicate<QuestStats> condition = stats ->
                stats.isLevelWon() && stats.getPlantsLostInLevel() <= n;
        int seedCount = 20 - n;
        Reward reward = new RandomSeedPacketReward(seedCount);
        return new MainQuest(id, desc, condition, reward);
    }
}