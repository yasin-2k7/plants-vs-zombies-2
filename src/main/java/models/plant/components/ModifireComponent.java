package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class ModifireComponent implements GameComponent {
    private String modifierType;

    public ModifireComponent(String type) {
        this.modifierType = type;
    }

    @Override
    public void update(Plant plant) {
        // TODO: اعمال تغییرات روی زمین
    }

    @Override
    public void activatePlantFood(Plant owner) {

    }
}
