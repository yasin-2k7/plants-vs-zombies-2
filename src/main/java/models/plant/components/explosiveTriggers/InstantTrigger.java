package models.plant.components.explosiveTriggers;

import models.plant.Plant;
import models.plant.components.ExplosivesComponent;

public class InstantTrigger implements ExplosiveTrigger{
    public static final InstantTrigger INSTANCE = new InstantTrigger();
    private InstantTrigger() {}

    @Override
    public boolean shouldTrigger(Plant owner, ExplosivesComponent component) {
        return true;
    }
}
