package models.zombie.state;

import models.plant.Plant;
import models.zombie.Zombie;

public class EatingState implements ZombieState {
    private Plant targetPlant;

    public EatingState(Plant targetPlant) {
        this.targetPlant = targetPlant;
    }

    @Override
    public void handleAction(Zombie zombie) {
        if (targetPlant != null && !targetPlant.isDead()) {
             targetPlant.takeDamage(zombie.getDamage(), zombie);
             targetPlant.takeDamage(zombie.getDamage());
            zombie.setHasEatenPlant(true);

             if (targetPlant.isDead()) {
                 zombie.setState(new WalkingState());
             }
        } else {
            zombie.setState(new WalkingState());
        }
    }
}
