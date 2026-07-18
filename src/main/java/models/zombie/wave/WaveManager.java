package models.zombie.wave;

import controller.GameMenuController;
import models.core.App;
import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;

import java.util.List;
import java.util.Random;

public class WaveManager {
    private List<Wave> waves;
    private int currentWaveIndex;
    private Wave currentWave;
    private int totalZombiesInCurrentWave;
    private int killedZombiesInCurrentWave;
    private boolean levelCompleted;
    private boolean firstWaveStarted;

    public WaveManager(List<Wave> waves) {
        this.waves = waves;
        this.currentWaveIndex = 0;
        if (!waves.isEmpty()) {
            this.currentWave = waves.get(0);
            this.totalZombiesInCurrentWave = currentWave.getTotalZombieCount();
            printWaveStartMessage(currentWave); // چاپ پیام شروع موج
            this.firstWaveStarted = true;
        } else {
            this.levelCompleted = true;
        }
        this.killedZombiesInCurrentWave = 0;
    }

    private void printWaveStartMessage(Wave wave) {
        int waveNum = wave.getWaveNumber();
        if (wave.isFlagWave()) {
            GameMenuController.updateState("The final wave has come.");
        } else {
            GameMenuController.updateState("Wave " + waveNum + " started.");
        }
    }

    public boolean update() {
        if (levelCompleted) return false;
        if (currentWave == null) return false;
        if (currentWave.isFinishedSpawning()) {
            if (killedZombiesInCurrentWave >= totalZombiesInCurrentWave * 0.75) {
                goToNextWave();
                return true;
            }
        }
        return false;
    }

    private void goToNextWave() {
        currentWaveIndex++;
        if (currentWaveIndex < waves.size()) {
            currentWave = waves.get(currentWaveIndex);
            totalZombiesInCurrentWave = currentWave.getTotalZombieCount();
            killedZombiesInCurrentWave = 0;
            printWaveStartMessage(currentWave);
        } else {
            levelCompleted = true;
        }
    }

    // متد اسپاون زامبی
    public void spawnNextZombie(int lane, GameWorld game) {
        if (levelCompleted || currentWave == null) return;
        WaveSpawnEntry entry = currentWave.getNextSpawn();
        if (entry == null) return;

        Zombie zombie = new ZombieFactory().createZombie(entry.getZombie());
        if (zombie == null) return;
        if (App.getCurrentUser().getShowedZombies().containsKey())

        int spawnCol = game.getCols();

        if (currentWave.isFlagWave() && game.isSandstormActive()) {
            int columnsForward = 1 + new Random().nextInt(4);
            spawnCol = Math.max(0, spawnCol - columnsForward);
            System.out.println("A zombie rides a sandstorm and enters " + columnsForward + " columns ahead!");
        }

        float x = spawnCol * App.getCellWidth(); // فرض: عرض سلول ۱۰۰ و سمت راست
        float y = lane * App.getCellHeight() + App.getCellHeight()/2;
        zombie.setX(x);
        zombie.setY(y);

        game.addZombie(zombie);

        String typeName = entry.getZombieAlias();
        int waveNum = currentWave.getWaveNumber();
        int cost = entry.getWavePointCost();
        System.out.println("Zombie " + typeName + " spawned at wave " + waveNum +
                " in lane " + (lane+1) + " which costed " + cost + ".");
    }

    // تقلب: تمام زامبی‌های فعال را نابود می‌کند
    public void releaseTheNuke(GameWorld game) {
        List<Zombie> zombies = game.getActiveZombies();
        for (Zombie z : zombies) {
            if (!z.isDead()) {
                z.die(); // این متد پیام مرگ را چاپ می‌کند
            }
        }
        zombies.clear();
        System.out.println("All zombies eliminated by nuke!");
    }

    // هر بار که یک زامبی کشته می‌شود این متد صدا میزنیم
    public void onZombieKilled(Zombie zombie) {
        if (levelCompleted) return;
        killedZombiesInCurrentWave++;
    }

    // دریافت زامبی بعدی برای اسپاون (از موج جاری)
    public WaveSpawnEntry getNextZombieToSpawn() {
        if (levelCompleted || currentWave == null) return null;
        return currentWave.getNextSpawn();
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }
    public Wave getCurrentWave() {
        return currentWave;
    }
    public int getKilledZombiesInCurrentWave() { return killedZombiesInCurrentWave; }
    public int getTotalZombiesInCurrentWave() { return totalZombiesInCurrentWave; }

    public List<Wave> getWaves() {
        return waves;
    }

    public int getTotalWavesCount() {
        return waves != null ? waves.size() : 0;
    }

    public int getCurrentWaveIndex() {
        return currentWaveIndex;
    }
    public void startFirstWave() {
        if (waves.isEmpty()) return;
        this.currentWaveIndex = 0;
        this.currentWave = waves.get(0);
        this.totalZombiesInCurrentWave = currentWave.getTotalZombieCount();
        this.killedZombiesInCurrentWave = 0;
        this.levelCompleted = false;
        printWaveStartMessage(currentWave);
        this.firstWaveStarted = true;
    }
}
