package com.pvz2.models.plant.factory;

import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantLayer;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.components.MagnetShroomComponent;
import com.pvz2.models.plant.components.PlacementBehaviorComponent;
import com.pvz2.models.plant.components.TorchwoodComponent;
import com.pvz2.models.world.Cell;

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
        int level = App.getCurrentUser().getUnlockedPlantsLevels().getOrDefault(PlantType.TORCHWOOD, 1);
        int health = (level >= 2) ? 600 : 300;
        boolean aoe = level >= 3;
        Plant p = new Plant(PlantType.TORCHWOOD, health, 0);
        p.addComponent(new TorchwoodComponent(2, aoe));
        p.setFire(true);
        return p;
    }

    private static Plant buildMagnetShroom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().getOrDefault(PlantType.MAGNET_SHROOM, 1);
        int health = (level >= 4) ? 500 : 300;
        int radius = (level >= 2) ? 3 : 2;
        Plant p = new Plant(PlantType.MAGNET_SHROOM, health, 0);
        p.addComponent(new MagnetShroomComponent(radius));
        return p;
    }

    private static Plant buildLilyPad() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().getOrDefault(PlantType.LILY_PAD, 1);
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
