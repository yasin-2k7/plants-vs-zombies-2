package com.pvz2.models.mupoint;

import com.pvz2.models.zombie.Zombie;

public class KillEvent {
    private Zombie zombie;
    private long spawnTick;
    private long deathTick;
    private int simultaneousKills;
    private boolean bySplashDamage;
    private boolean plantEatenInLine;

    public KillEvent(Zombie zombie, long spawnTick, long deathTick,
                     int simultaneousKills, boolean bySplashDamage, boolean plantEatenInLine) {
        this.zombie = zombie;
        this.spawnTick = spawnTick;
        this.deathTick = deathTick;
        this.simultaneousKills = simultaneousKills;
        this.bySplashDamage = bySplashDamage;
        this.plantEatenInLine = plantEatenInLine;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public long getSurvivalTicks() {
        return deathTick - spawnTick;
    }

    public int getSimultaneousKills() {
        return simultaneousKills;
    }

    public boolean isBySplashDamage() {
        return bySplashDamage;
    }

    public boolean hasEatenPlant() {
        return plantEatenInLine;
    }
}
