package models.plant.factory;

import models.core.App;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.plant.GameComponent;
import models.plant.Plant;
import models.plant.components.MagnetShroomComponent;
import models.plant.components.PlacementBehaviorComponent;
import models.plant.components.TorchwoodComponent;
import models.world.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModifierAndHomingFactory {
    static void register(Map<PlantType, Supplier<Plant>> registry) {
        registry.put(PlantType.TORCHWOOD, ModifierAndHomingFactory::buildTorchwood);
        registry.put(PlantType.MAGNET_SHROOM, ModifierAndHomingFactory::buildMagnetShroom);
        registry.put(PlantType.LILY_PAD, ModifierAndHomingFactory::buildLilyPad);
    }

    private static Plant buildTorchwood() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.TORCHWOOD);
        int health = (level >= 2) ? 600 : 300;
        boolean aoe = level >= 3;
        Plant p = new Plant(PlantType.TORCHWOOD, health, 0);
        p.addComponent(new TorchwoodComponent(2, aoe));
        return p;
    }

    private static Plant buildMagnetShroom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.MAGNET_SHROOM);
        int health = (level >= 4) ? 500 : 300;
        int radius = (level >= 2) ? 3 : 2;
        Plant p = new Plant(PlantType.MAGNET_SHROOM, health, 0);
        p.addComponent(new MagnetShroomComponent(radius));
        return p;
    }

    private static Plant buildLilyPad() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.LILY_PAD);
        int health = (level >= 3) ? 500 : 300;
        Plant p = new Plant(PlantType.LILY_PAD, health, 0);
        GameComponent component = new PlacementBehaviorComponent(PlantLayer.BASE, false, 0, true) {
            @Override
            public void activatePlantFood(Plant owner) {
                List<Cell> emptyCells = App.getCurrentGame().findTwoEmptyCell(true);

                for (Cell cell : emptyCells) {
                    Plant cloneLilyPad = buildLilyPad();
                    cell.setPlant(cloneLilyPad, PlantLayer.BASE);
                }
            }
        };
        p.addComponent(component);
        return p;
    }


}
