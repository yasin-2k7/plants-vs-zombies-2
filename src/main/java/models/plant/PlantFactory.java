package models.plant;

import models.core.App;
import models.enums.PlantType;
import models.plant.components.ShooterComponent;

public class PlantFactory {

        public Plant createPlant(PlantType type) {
            int level = App.getCurrentUser().getUnlockedPlantsLevels().get(type);
            switch (type) {
                case SUNFLOWER:
                    return buildSunflower(level);
                case TWIN_SUNFLOWER:

                default:
                    return null;
            }
        }

        private Plant buildSunflower(int level) {
            int baseHealth = 300;
            int finalHealth = baseHealth + (level - 1) * 50;

            Plant p = new Plant(PlantType.SUNFLOWER);


           // p.addComponent(new ShooterComponent(10, ));
            return p;
        }



}
