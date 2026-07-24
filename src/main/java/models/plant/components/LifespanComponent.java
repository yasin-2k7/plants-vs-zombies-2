package models.plant.components;

import models.core.App;
import models.enums.PlantType;
import models.plant.GameComponent;
import models.plant.Plant;

public class LifespanComponent implements GameComponent {
    private final PlantType type;
    private final float maxLifeTime;
    private float timer = 0f;

    public LifespanComponent(PlantType type, float maxLifeTime) {
        this.type = type;
        this.maxLifeTime = maxLifeTime;
    }

    @Override
    public void activatePlantFood(Plant owner) {
        App.getCurrentGame().triggerSmallShroomsPlantFood(type);
    }

    public void onGlobalPlantFoodActivated(PlantType type) {
        if (this.type.equals(type)) {
            timer = 0f;
        }
    }

    @Override
    public void update(Plant owner) {
        timer++;
        if (timer >= maxLifeTime) {
            owner.destroy();
        }
    }

}
