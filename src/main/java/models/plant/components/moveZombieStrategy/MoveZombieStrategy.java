package models.plant.components.moveZombieStrategy;

import models.plant.Plant;
import models.zombie.Zombie;

public interface MoveZombieStrategy {
    void onUpdate(Plant owner);

    void onTakeDamage(Plant owner, int damage, Zombie attacker);

    void onPlantFood(Plant owner);
}
