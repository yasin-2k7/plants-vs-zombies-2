package models.zombie.zombiesType;

import models.zombie.Zombie;

public class SpawnerZombie extends Zombie {
    private int spawnCooldown; // زمان بین هر بار احضار
    private int currentCooldown = 0;

    public SpawnerZombie(int health, int speed, int damage, int spawnCooldown) {
        super("Spawner Zombie", health, speed, damage);
        this.spawnCooldown = spawnCooldown;
    }

    // متد احضار یا پرتاب زامبی جدید
    public void spawnZombie(String zombieType, int targetX, int targetY) {
        if (currentCooldown <= 0) {
            // لاجیک اضافه کردن زامبی جدید به زمین بازی

            currentCooldown = spawnCooldown; // ریست کردن کول‌داون
        } else {
            currentCooldown--;
        }
    }
}
