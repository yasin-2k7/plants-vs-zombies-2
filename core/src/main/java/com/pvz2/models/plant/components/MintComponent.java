package com.pvz2.models.plant.components;

import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;

import java.util.function.Consumer;

public class MintComponent implements GameComponent {
    private final PlantType plantType;
    private final Consumer<Plant> mintAction;
    private boolean executed = false;

    public MintComponent(PlantType plantType, Consumer<Plant> mintAction) {
        this.plantType = plantType;
        this.mintAction = mintAction;
    }

    @Override
    public void update(Plant owner) {
        if (executed) return;
        executed = true;

        if (mintAction != null) {
            mintAction.accept(owner);
        }

        owner.die();
    }

    @Override
    public void activatePlantFood(Plant owner) {
    }

    public PlantType getPlantType() {
        return plantType;
    }
}
