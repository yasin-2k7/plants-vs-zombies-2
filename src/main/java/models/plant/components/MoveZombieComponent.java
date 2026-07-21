package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;
import models.plant.components.moveZombieStrategy.MoveZombieStrategy;
import models.zombie.Zombie;

public class MoveZombieComponent implements GameComponent {
    MoveZombieStrategy strategy;

    public MoveZombieComponent(MoveZombieStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void update(Plant owner) {
        strategy.onUpdate(owner);
    }

    @Override
    public void activatePlantFood(Plant owner) {
        strategy.onPlantFood(owner);
    }

    @Override
    public int onTakeDamage(Plant owner, int damageAmount, Zombie attacker) {
        strategy.onTakeDamage(owner, damageAmount, attacker);
        return 0;
    }
}
