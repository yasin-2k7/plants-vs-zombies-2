package models.plant.components.explosiveBehaviors;

import models.plant.Plant;

public interface ExplosiveBehavior {
    void execute(Plant owner);
    boolean isFinished();
}
