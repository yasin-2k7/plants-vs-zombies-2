package com.pvz2.models.plant.components;

import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;

public abstract class WallNutsComponent implements GameComponent {


    @Override
    public void update(Plant owner, float delta) {
        return;
    }

    @Override
    public abstract void activatePlantFood(Plant owner);

}
