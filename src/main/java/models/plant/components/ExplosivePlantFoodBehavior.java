package models.plant.components;

import models.plant.Plant;

@FunctionalInterface
public interface ExplosivePlantFoodBehavior {
    void execute(Plant owner, ExplosivesComponent component);
}
