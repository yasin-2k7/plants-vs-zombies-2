package models.plant.components;

import models.enums.PlantType;
import models.plant.GameComponent;
import models.plant.Plant;

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
