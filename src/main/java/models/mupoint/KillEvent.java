package models.mupoint;

import models.zombie.Zombie;

public class KillEvent {
    private Zombie zombie;
    private long spawnTick;
    private long deathTick;
    private int simultaneousKills; // تعداد زامبی‌های کشته‌شده همزمان در آن فریم
    private boolean bySplashDamage; // کشته شده با بمب/گیلاس/سیب‌زمینی
    private boolean plantEatenInLine; // آیا این زامبی موفق شده گیاهی رو بخوره؟

    public KillEvent(Zombie zombie, long spawnTick, long deathTick, int simultaneousKills, boolean bySplashDamage, boolean plantEatenInLine) {
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