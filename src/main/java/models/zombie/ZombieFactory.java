package models.zombie;

import models.core.App;
import models.enums.PlantType;
import models.enums.Zombies;
import models.plant.Plant;

public class ZombieFactory {
    public Zombie createZombie(Zombies type) {
        switch (type) {
            case ZOMBIE:
                return buildZombie(type);
            default:
                return null;
        }
    }

    private Zombie buildZombie(Zombies) {
        return new Zombie() {
            @Override
            public void damageToPlant() {

            }
        }
    }
}
