package models.zombie.wave;

import models.zombie.Zombie;

import java.util.List;

public class WaveManager {
    private List<Wave> waves;
    private int currentWaveIndex;
    private Wave currentWave;
    private int totalZombiesInCurrentWave;
    private int killedZombiesInCurrentWave;
    private boolean levelCompleted;

    public WaveManager(List<Wave> waves) {
        this.waves = waves;
        this.currentWaveIndex = 0;
        this.currentWave = waves.get(0);
        this.totalZombiesInCurrentWave = currentWave.getTotalZombieCount();
        this.killedZombiesInCurrentWave = 0;
        this.levelCompleted = false;
    }

    // این متد را هر تیک در گیم‌لوپ صدا میزنیم.
    public boolean update() {
        if (levelCompleted) return false;

        if (currentWave.isFinishedSpawning()) {
            if (killedZombiesInCurrentWave >= totalZombiesInCurrentWave * 0.75) {
                goToNextWave();
                return true;
            }
        }
        return false;
    }

    private void goToNextWave() {

    }

    // هر بار که یک زامبی کشته می‌شود این متد صدا میزنیم
    public void onZombieKilled(Zombie zombie) {
        if (levelCompleted) return;
        killedZombiesInCurrentWave++;
    }

    // دریافت زامبی بعدی برای اسپاون (از موج جاری)
    public WaveSpawnEntry getNextZombieToSpawn() {
        if (levelCompleted) return null;
        return currentWave.getNextSpawn();
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    public Wave getCurrentWave() {
        return currentWave;
    }
}
