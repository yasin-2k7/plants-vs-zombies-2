package models.zombie.state;

import models.core.App;
import models.miniGame.beghouled.BeghouledMechanics;
import models.plant.Plant;
import models.world.GameWorld;
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
            System.out.println("hit: " + zombie.getDamage());
            zombie.setHasEatenPlant(true);

            if (targetPlant.isDead()) {
                zombie.setState(new WalkingState());
                GameWorld world = App.getCurrentGame();
                BeghouledMechanics beghouled = world.getMechanic(BeghouledMechanics.class);

                if (beghouled != null) {
                    beghouled.createCrater(world, targetPlant.getCell().getRow(), targetPlant.getCell().getCol());
                }
            }
        } else {
            zombie.setState(new WalkingState());
        }
    }
}
