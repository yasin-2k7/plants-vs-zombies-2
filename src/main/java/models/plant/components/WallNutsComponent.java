package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;
import models.zombie.Zombie;

public abstract class WallNutsComponent implements GameComponent {


    @Override
    public void update(Plant owner){}

    @Override
    public abstract void activatePlantFood(Plant owner);

}
