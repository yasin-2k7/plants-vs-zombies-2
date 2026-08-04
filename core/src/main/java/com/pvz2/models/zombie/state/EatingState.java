package com.pvz2.models.zombie.state;

import com.pvz2.models.core.App;
import com.pvz2.models.miniGame.beghouled.BeghouledMechanics;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.levelSetup.SaveOurSeedsLevelSetup;
import com.pvz2.models.world.loseCondition.SaveOurSeedsLose;
import com.pvz2.models.zombie.Zombie;

public class EatingState implements ZombieState {
    private Plant targetPlant;

    public EatingState(Plant targetPlant) {
        this.targetPlant = targetPlant;
    }

    @Override
    public void handleAction(Zombie zombie) {
        if (targetPlant != null && !targetPlant.isDead()) {
            targetPlant.takeDamage(zombie.getDamage(), zombie);
            zombie.setHasEatenPlant(true);

            if (targetPlant.isDead()) {
                zombie.setState(new WalkingState());
                GameWorld world = App.getCurrentGame();
                BeghouledMechanics beghouled = world.getMechanic(BeghouledMechanics.class);

                if (beghouled != null) {
                    beghouled.createCrater(world, targetPlant.getCell().getRow(), targetPlant.getCell().getCol());
                }

                if(world.getLevelSetup() instanceof SaveOurSeedsLevelSetup setup){
                    System.out.println("Level is SaveOurSeeds");

                    boolean isProtected = setup.isProtectedPlant(targetPlant);
                    System.out.println("Is plant protected? " + isProtected);

                    if (isProtected) {
                        SaveOurSeedsLose loseCondition = world.getLoseCondition(SaveOurSeedsLose.class);
                        System.out.println("LoseCondition found? " + (loseCondition != null));

                        if (loseCondition != null) {
                            loseCondition.onProtectedPlantEaten();
                            System.out.println("onProtectedPlantEaten() CALLED!");
                        }
                    }
                }
            }
        } else {
            zombie.setState(new WalkingState());
        }
    }
}
