package models.zombie.wave;

import models.core.App;
import models.core.DifficultyCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wave {
    private int waveNumber;
    private int totalCost;
    private List<WaveSpawnEntry> spawnEntries;
    private int spawnDelayTicks;
    private int currentIndex;
    private int ticksSinceLastSpawn;
    private boolean isFinalWave;

    public Wave(int waveNumber,
                int totalCost,
                List<WaveSpawnEntry> spawnEntries,
                int spawnDelayTicks,
                boolean isFinalWave) {
        this.waveNumber = waveNumber;
        this.totalCost = totalCost;
        this.spawnEntries = new ArrayList<>(spawnEntries);
        this.spawnDelayTicks = spawnDelayTicks;
        this.currentIndex = 0;
        this.ticksSinceLastSpawn = 0;
        this.isFinalWave = isFinalWave;
    }

    // متد کمکی برای تولید خودکار موج بر اساس هزینه کل و لیست ورودی‌های مجاز
    public static Wave generateRandomWave(int waveNumber,
                                          int totalCost,
                                          List<WaveSpawnEntry> availableEntries,
                                          int spawnDelayTicks, boolean isFinalWave,
                                          Random random) {
        List<WaveSpawnEntry> generatedEntries = new ArrayList<>();
        int currentCost = 0;

        // تا زمانی که بودجه داریم و زامبی‌های مجاز تعریف شده‌اند
        while (currentCost < totalCost && !availableEntries.isEmpty()) {
            WaveSpawnEntry randomEntry = availableEntries.get(random.nextInt(availableEntries.size()));

            // بررسی اینکه آیا اضافه کردن این زامبی از سقف سختی مجاز فراتر می‌رود یا خیر
            if (currentCost + randomEntry.getWavePointCost() <= totalCost) {
                generatedEntries.add(new WaveSpawnEntry(randomEntry.getZombieAlias(), randomEntry.getWavePointCost()));
                currentCost += randomEntry.getWavePointCost();
            } else {
                // اگر هزینه این نمونه زیاد بود، لیست را چک می‌کنیم؛ اگر هیچ زامبی کم امتیاز تری پیدا نشد خارج می‌شویم
                boolean canAddAny = false;
                for (WaveSpawnEntry entry : availableEntries) {
                    if (currentCost + entry.getWavePointCost() <= totalCost) {
                        canAddAny = true;
                        break;
                    }
                }
                if (!canAddAny) break;
            }
        }
        return new Wave(waveNumber, totalCost, generatedEntries, spawnDelayTicks, isFinalWave);
    }

    public static List<Wave> generateWaves(int totalWaves,
                                           int baseDifficulty,
                                           List<WaveSpawnEntry> availableEntries,
                                           int spawnDelayTicks,
                                           Random random) {

        int userDifficulty = App.getCurrentUser().getGameDifficulty();
        double decreaseFactor = DifficultyCalculator.decreaseFactor(userDifficulty);

        List<Wave> waves = new ArrayList<>();
        for (int i = 1; i <= totalWaves; i++) {
            boolean isFinal = (i == totalWaves);
            double difficulty = baseDifficulty * Math.pow(1.25, i - 1);
            if (isFinal) {
                difficulty *= 2;
            }
            int cost = (int) Math.round(difficulty * decreaseFactor);

            Wave wave = generateRandomWave(i, cost, availableEntries, i == 1 ? 60 : spawnDelayTicks, isFinal, random);
            waves.add(wave);
        }
        return waves;
    }

    public static List<Wave> generateWaves(int totalWaves,
                                           int baseDifficulty,
                                           List<WaveSpawnEntry> availableEntries,
                                           int spawnDelayTicks) {
        return generateWaves(totalWaves, baseDifficulty, availableEntries, spawnDelayTicks, new Random());
    }

    public boolean isFinishedSpawning() {
        return currentIndex >= spawnEntries.size();
    }

    public WaveSpawnEntry getNextSpawn() {
        if (isFinishedSpawning()) return null;
        if (ticksSinceLastSpawn < spawnDelayTicks) {
            ticksSinceLastSpawn++;
            return null;
        }
        ticksSinceLastSpawn = 0;
        return spawnEntries.get(currentIndex++);
    }

    public void resetSpawning() {
        this.currentIndex = 0;
        this.ticksSinceLastSpawn = 0;
    }

    public int getTotalZombieCount() {
        return spawnEntries.size();
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public boolean isFlagWave() {
        return isFinalWave;
    }
}