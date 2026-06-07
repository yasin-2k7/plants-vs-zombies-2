package models.mupoint;

import models.zombie.*;
public class KillEvent {
    public Zombie zombie;
    public int killDuration; // زمان صرف شده برای کشتن
    public int simultaneousKills; // تعداد زامبی‌های کشته شده همزمان
    public boolean bySplashDamage; // کشته شده با آسیب گروهی؟

    public KillEvent(Zombie zombie, int killDuration, int simultaneousKills, boolean bySplashDamage) {
        this.zombie = zombie;
        this.killDuration = killDuration;
        this.simultaneousKills = simultaneousKills;
        this.bySplashDamage = bySplashDamage;
    }

}
