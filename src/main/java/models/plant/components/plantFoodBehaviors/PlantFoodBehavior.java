package models.plant.components.plantFoodBehaviors;

import models.plant.Plant;
import models.plant.components.ShooterComponent;

public interface PlantFoodBehavior {
    void activate(Plant owner, ShooterComponent shooterComponent);
}
