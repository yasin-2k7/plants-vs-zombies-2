package models.plant.components.explosiveTriggers;

import models.plant.Plant;
import models.plant.components.ExplosivesComponent;

public interface ExplosiveTrigger {
    boolean shouldTrigger(Plant owner, ExplosivesComponent component);
}
