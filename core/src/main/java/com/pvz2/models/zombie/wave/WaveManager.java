package com.pvz2.models.zombie.wave;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserDataManager;
import com.pvz2.models.world.ChapterWorld.AncientEgyptWorld;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.ZombieFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveManager {
    private List<Wave> waves;
    private int currentWaveIndex;
    private Wave currentWave;
    private int totalZombiesInCurrentWave;
    private int killedZombiesInCurrentWave;
    private boolean levelCompleted;
    private final int totalZombiesOverall;   // <<< جدید

    private boolean repeatForever = false;
    private boolean wavesStarted = true;

    public WaveManager(List<Wave> waves) {
        this(waves, true);
    }

    public WaveManager(List<Wave> waves, boolean autoStart) {
        this.waves = waves;
        this.currentWaveIndex = 0;
        this.wavesStarted = autoStart;

        int sum = 0;
        for (Wave w : waves) {
            sum += w.getTotalZombieCount();
        }
        this.totalZombiesOverall = sum;

        if (!waves.isEmpty()) {
            this.currentWave = waves.get(0);
            this.totalZombiesInCurrentWave = currentWave.getTotalZombieCount();
            if (autoStart) {
                printWaveStartMessage(currentWave);
            }
        } else {
            this.levelCompleted = true;
        }
        this.killedZombiesInCurrentWave = 0;
    }


    public void startWaves() {
        if (wavesStarted || levelCompleted) return;
        this.wavesStarted = true;
        if (currentWave != null) {
            printWaveStartMessage(currentWave);
        }
    }
    public boolean isWavesStarted() {
        return wavesStarted;
    }

    public void setRepeatForever(boolean repeatForever) {
        this.repeatForever = repeatForever;
    }

    private void printWaveStartMessage(Wave wave) {
        int waveNum = wave.getWaveNumber();
        if (wave.isFlagWave()) {
            GameMenuController.showAnnouncement("The final wave has come.");
        } else {
            GameMenuController.showAnnouncement("Wave " + waveNum + " started.");
        }
    }

    public boolean update() {
        if (!wavesStarted || levelCompleted || currentWave == null) return false;
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
        } else if (repeatForever) {
            currentWaveIndex = 0;
            currentWave = waves.get(0);
            totalZombiesInCurrentWave = currentWave.getTotalZombieCount();
            killedZombiesInCurrentWave = 0;
            currentWave.resetSpawning();
            printWaveStartMessage(currentWave);
        } else {
            levelCompleted = true;
        }
    }

    public void spawnNextZombie(int lane, GameWorld game) {
        if (!wavesStarted || levelCompleted || currentWave == null) return;
        WaveSpawnEntry entry = currentWave.getNextSpawn();
        if (entry == null) return;

        Zombie zombie = new ZombieFactory().createZombie(entry.getZombie());
        if (zombie == null) return;

        String alias = entry.getZombieAlias();

        User user = App.getCurrentUser();
        if (user != null) {
            String inGameName = App.getArmoredZombieName(alias);
            if (!user.getShowedZombies().containsKey(inGameName)) {
                user.getShowedZombies().put(inGameName, true);
                user.notifyZombieUnlock(inGameName);
                UserDataManager.saveUser(App.getCurrentUser());
            }
        }

        int spawnCol = game.getCols();
        boolean isSandstormSpawn = currentWave.isFlagWave()
            && game.isSandstormActive()
            && (game instanceof AncientEgyptWorld);

        if (isSandstormSpawn) {
            int columnsForward = 1 + new Random().nextInt(4);
            spawnCol = Math.max(0, spawnCol - columnsForward);

            ((AncientEgyptWorld) game).spawnSandstorm(zombie, lane, spawnCol);

            GameMenuController.updateState(
                "A zombie rides a sandstorm and enters " + columnsForward + " columns ahead!");
        }

        float x = App.getFirstCellX() + spawnCol * App.getCellWidth();
        float y = App.getCellCenterY(lane);
        zombie.setX(x);
        zombie.setY(y);

        game.addZombie(zombie);

        String typeName = entry.getZombieAlias();
        int waveNum = currentWave.getWaveNumber();
        int cost = entry.getWavePointCost();
        GameMenuController.updateState("Zombie " + typeName + " spawned at wave " + waveNum +
                " in lane " + (lane + 1) + " which costed " + cost + ".");

    }

    public void releaseTheNuke(GameWorld game) {
        List<Zombie> zombies = game.getActiveZombies();
        for (Zombie z : zombies) {
            if (!z.isDead()) {
                z.die();
                onZombieKilled();
            }
        }
        GameMenuController.updateState("All zombies eliminated by nuke!");
    }

    public void onZombieKilled() {
        if (levelCompleted) return;
        killedZombiesInCurrentWave++;

    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    public Wave getCurrentWave() {
        return currentWave;
    }

    public List<Wave> getWaves() {
        return waves;
    }

    public int getTotalWavesCount() {
        return waves != null ? waves.size() : 0;
    }

    public int getCurrentWaveIndex() {
        return currentWaveIndex;
    }

    public int getKilledZombiesInCurrentWave() {
        return killedZombiesInCurrentWave;
    }

    public int getTotalZombiesInCurrentWave() {
        return totalZombiesInCurrentWave;
    }

    public float getOverallProgress() {
        int totalWaves = getTotalWavesCount();
        if (totalWaves == 0) return levelCompleted ? 1f : 0f;

        float currentWaveProgress = 0f;
        if (totalZombiesInCurrentWave > 0) {
            currentWaveProgress = Math.min(1f,
                    (float) killedZombiesInCurrentWave / (float) totalZombiesInCurrentWave);
        }

        float progress = (currentWaveIndex + currentWaveProgress) / (float) totalWaves;
        return levelCompleted ? 1f : Math.min(1f, progress);
    }

    public List<String> getAllZombieAliases() {
        List<String> aliases = new ArrayList<>();
        for (Wave w : waves) {
            for (WaveSpawnEntry entry : w.getSpawnEntries()) {
                aliases.add(entry.getZombieAlias());
            }
        }
        return aliases;
    }
}
