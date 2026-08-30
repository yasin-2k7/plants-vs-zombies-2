package com.pvz2.models.miniGame.IZombie;

import com.pvz2.models.pool.GenericObjectPool;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.Sun;
import com.pvz2.models.world.obstacles.Grave;
import com.pvz2.models.world.obstacles.Obstacle;
import com.pvz2.models.world.obstacles.OctopusObstacle;
import com.pvz2.network.onlineIZombie.BrainCurrency;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.world.loseCondition.LoseCondition;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.winCondition.WinCondition;
import com.pvz2.network.onlineIZombie.ZombieCard;

import java.util.ArrayList;
import java.util.List;

public class OnlineIZombieLevel extends IZombieLevel {
    private int zombieBrains = 0;
    private final List<BrainCurrency> activeBrains = new ArrayList<>();
    private List<ZombieCard> zombieCards = new ArrayList<>();
    private final GenericObjectPool<BrainCurrency> brainsPool = new GenericObjectPool<>(BrainCurrency::new);

    public OnlineIZombieLevel(LevelSetup levelSetup,
                               ArrayList<LoseCondition> loseConditions,
                               WinCondition winCondition,
                               ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

    @Override
    public void tick(float delta) {
        if (state != GameState.PLAYING) return;
        elapsedTime += delta;
        updateAll(delta);
        for (ZombieCard zombieCard : zombieCards) zombieCard.update(delta);
        for (BrainCurrency brainCurrency : activeBrains) {
            brainCurrency.update(delta);
            if (brainCurrency.isExpired()) {
                brainCurrency.collect();
            }
        }
        removeIfDead();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.hasObstacle()) {
                    Obstacle obs = cell.getObstacle();
                    if (obs instanceof Grave grave) {
                        if (!grave.blocksProjectiles()) {
                            grave.releaseContent();
                            cell.setPlantable(true);
                            cell.removeObstacle();
                        }
                    } else if (obs instanceof OctopusObstacle) {
                        if (cell.isEmpty() || !obs.blocksProjectiles()) {
                            cell.removeObstacle();
                        }
                    } else if (!obs.blocksProjectiles()) {
                        cell.removeObstacle();
                    }
                }
            }
        }
        for (Mechanic mechanic : mechanics) {
            mechanic.applyMechanic(this);
        }
    }

    public int getZombieBrains() {
        return zombieBrains;
    }

    public void addBrainsToPlayer(int amount) {
        this.zombieBrains += amount;
    }

    public List<BrainCurrency> getActiveBrains() {
        return activeBrains;
    }

    public List<ZombieCard> getZombieCards() {
        return zombieCards;
    }

    public GenericObjectPool<BrainCurrency> getBrainsPool() { return brainsPool; }

}
