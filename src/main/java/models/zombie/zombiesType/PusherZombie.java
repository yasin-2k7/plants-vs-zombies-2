package models.zombie.zombiesType;

import models.zombie.Zombie;

public class PusherZombie extends Zombie {
    private String pushedObjectName; // می‌تواند آرکید، پیانو یا یخ باشد
    private int objectHealth;

    public PusherZombie(int health, int speed, int damage, String pushedObjectName, int objHealth) {
        super("Pusher Zombie", health, speed, damage);
        this.pushedObjectName = pushedObjectName;
        this.objectHealth = objHealth;
    }

    @Override
    public void update() {
        if (objectHealth > 0) {
            // منطق له کردن گیاهان در صورت برخورد شیء با آن‌ها
            // moveObjectWithZombie();
        }
        super.update();
    }
}
