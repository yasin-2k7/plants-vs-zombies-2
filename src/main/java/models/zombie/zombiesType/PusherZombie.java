package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;

public class PusherZombie extends Zombie {
    private String objectName; // "ARCADE", "PIANO", "BARREL", "ICEBLOCK"
    private int objectHealth;
    private float objectX; // موقعیت شیء (جلوی زامبی)


    public PusherZombie(int health, int speed, int damage, String pushedObjectName, int objHealth) {
        super(Zombies.PUSHER, health, speed, damage);
        this.objectName = objectName;
        this.objectHealth = objectHealth;
        this.objectX = this.x + 60; // شیء جلوی زامبی مثلا
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        if (objectHealth > 0) {
            objectHealth -= amount;
            if (objectHealth < 0) objectHealth = 0;
        } else {
        super.takeDamage(amount, damageType);
        }
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update();
        this.objectX = this.x + 60;
        // اگر شیء سالم است، گیاهان جلوی آن را له می‌کند
        if (objectHealth > 0) {
            GameWorld game = App.getCurrentGame();
            if (game != null) {
                Plant plant = game.getPlantAtPosition(objectX, this.y);
                if (plant != null && !plant.isDead()) {
                    plant.die();
                }
            }
        }
    }

    public int getObjectHealth() { return objectHealth; }
    public String getObjectName() { return objectName; }
}
