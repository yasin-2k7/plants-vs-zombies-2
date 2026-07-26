package models.plant.components.shooterPlantFoodBehaviors;

import models.core.App;
import models.plant.Plant;
import models.plant.components.ShooterComponent;
import models.projectile.Projectile;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTargetPlantFood implements PlantFoodBehavior {
    private final int targetCount;

    public RandomTargetPlantFood(int targetCount) {
        this.targetCount = targetCount;
    }

    public RandomTargetPlantFood() {
        this.targetCount = Integer.MAX_VALUE;
    }

    @Override
    public void activate(Plant owner, ShooterComponent shooterComponent) {
        List<Zombie> allZombies = App.getCurrentGame().getActiveZombies();

        if (allZombies.isEmpty()) {
            return;
        }

        List<Zombie> zombieCopy = new ArrayList<>(allZombies);
        Collections.shuffle(zombieCopy);
        int finalCount = Math.min(targetCount, zombieCopy.size());
        List<Zombie> selectedZombies = zombieCopy.subList(0, finalCount);

        for (Zombie zombie : selectedZombies) {
            Projectile p = App.getCurrentGame().getProjectilesPool().acquire();
            p.reset(owner.getX(), owner.getY(), shooterComponent.getPlantFoodStrategy(),
                    shooterComponent.getMovementStrategies().getFirst().get(),
                    shooterComponent.getStrikeStrategy(), shooterComponent.getGiantType());
            p.setTarget(zombie);
            App.getCurrentGame().getActiveProjectiles().add(p);
        }
    }
}
