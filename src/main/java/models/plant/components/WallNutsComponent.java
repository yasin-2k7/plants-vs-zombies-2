package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public abstract class WallNutsComponent implements GameComponent {


    @Override
    public void update(Plant owner) {
        return;
    }

    @Override
    public abstract void activatePlantFood(Plant owner);

}
