package models.miniGame.IZombie;

import models.enums.Zombies;
import models.zombie.Zombie;

public class SunProducer extends Zombie {
    private int baseSunRate;
    private long spawnTick;
    private long lastSunProduceTick;

    public SunProducer(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
        this.baseSunRate = 50;
        this.spawnTick = 0;
        this.lastSunProduceTick = 0;
    }

    public void initSpawnTick(long currentTick){
        this.spawnTick = currentTick;
        this.lastSunProduceTick = currentTick;
    }

    public int calculateSunAmount(long currentTick){
        long elapsedTicks = currentTick - spawnTick;
        return 15 + (int) (elapsedTicks / 100) * 5;
    }

    public void updateSunGeneration(IZombieLevel level) {
        long currentTick = level.getCurrentTick();
        if (currentTick - lastSunProduceTick >= baseSunRate) {
            int sunAmount = calculateSunAmount(currentTick);
            level.addSunToPlayer(sunAmount);
            lastSunProduceTick = currentTick;
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if (health > 0) {
            health -= amount;
            if (health < 0) health = 0;
        }else {
            super.takeDamage(amount, damageType);
        }
    }
}
