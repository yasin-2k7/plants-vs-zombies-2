package models.miniGame.zombotany;

import models.enums.Zombies;
import models.zombie.Zombie;

public class PeashooterZombie extends Zombie {
    private int shootCooldown;

    public PeashooterZombie(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
    }
}
