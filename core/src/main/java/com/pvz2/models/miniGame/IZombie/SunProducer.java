package com.pvz2.models.miniGame.IZombie;

import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;

public class SunProducer extends Zombie {
    private int baseSunRate;
    private long spawnTick;
    private long lastSunProduceTick;
    private boolean initialized = false;

    public SunProducer(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
        this.baseSunRate = 50;
        this.spawnTick = 0;
        this.lastSunProduceTick = 0;
    }

    public void initSpawnTick(long currentTick) {
        this.spawnTick = currentTick;
        this.lastSunProduceTick = currentTick;
        this.initialized = true;
    }

    public int calculateSunAmount(long currentTick) {
        long elapsedTicks = currentTick - spawnTick;
        return 15 + (int) (elapsedTicks / 100) * 5;
    }

    public void updateSunGeneration(IZombieLevel level) {
        long currentTick = level.getCurrentTick();
        if (currentTick - lastSunProduceTick >= baseSunRate) {
            int sunAmount = calculateSunAmount(currentTick);
            System.out.println("SunProducer generated " + sunAmount + " suns! ☀️");
            level.addSunToPlayer(sunAmount);
            lastSunProduceTick = currentTick;
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        super.takeDamage(amount, damageType);
    }

    @Override
    public void update() {
        super.update();
        GameWorld currentGame = App.getCurrentGame();
        if (currentGame instanceof IZombieLevel level) {
            if (!initialized) {
                initSpawnTick(level.getCurrentTick());
            }

            updateSunGeneration(level);
        }
    }
}
