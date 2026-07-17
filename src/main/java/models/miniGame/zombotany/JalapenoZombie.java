package models.miniGame.zombotany;

import models.enums.Zombies;
import models.zombie.Zombie;

public class JalapenoZombie extends Zombie {
    private long entryTime;
    private int burnDelay = 10;

    public JalapenoZombie(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
    }
}
