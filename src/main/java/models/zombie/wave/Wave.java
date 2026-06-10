package models.zombie.wave;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wave {
    private int waveNumber;
    private int totalCost;                // سختی موج
    private List<WaveSpawnEntry> spawnEntries;  // لیست نهایی زامبی‌های این موج (تکرار مجاز)
    private int spawnDelayTicks;          // تأخیر بین اسپاون هر زامبی (بر حسب تیک)
    private int currentIndex;             // شاخص بعدی برای اسپاون
    private int ticksSinceLastSpawn;      // شمارشگر تیک برای تأخیر
    private boolean isFinalWave;

    public Wave(int waveNumber, int totalCost, List<WaveSpawnEntry> spawnEntries,
                int spawnDelayTicks, boolean isFinalWave) {
        this.waveNumber = waveNumber;
        this.totalCost = totalCost;
        this.spawnEntries = new ArrayList<>(spawnEntries);
        this.spawnDelayTicks = spawnDelayTicks;
        this.currentIndex = 0;
        this.ticksSinceLastSpawn = 0;
        this.isFinalWave = isFinalWave;
    }

    // متد کمکی برای تولید خودکار موج بر اساس هزینه کل و لیست ورودی‌های مجاز
    public static Wave generateRandomWave(int waveNumber, int totalCost,
                                          List<WaveSpawnEntry> availableEntries,
                                          int spawnDelayTicks, boolean isFinalWave) {
        // TODO: 
        return null;
    }

    // آیا تمام زامبی‌های این موج اسپاون شده‌اند؟
    public boolean isFinishedSpawning() {
        return currentIndex >= spawnEntries.size();
    }

    // هر بار در حلقه تیک صدا زده می‌شود. اگر زمان اسپاون بعدی رسیده باشد،
    // زامبی بعدی را برمی‌گرداند، در غیر این صورت null.
    public WaveSpawnEntry getNextSpawn() {
        if (isFinishedSpawning()) return null;
        if (ticksSinceLastSpawn < spawnDelayTicks) {
            ticksSinceLastSpawn++;
            return null;
        }
        ticksSinceLastSpawn = 0;
        return spawnEntries.get(currentIndex++);
    }

    // تعداد کل زامبی‌های این موج
    public int getTotalZombieCount() {
        return spawnEntries.size();
    }

    // Getters
    public int getWaveNumber() { return waveNumber; }
    public int getTotalCost() { return totalCost; }
    public boolean isFinalWave() { return isFinalWave; }
}