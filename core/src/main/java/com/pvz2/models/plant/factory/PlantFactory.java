package com.pvz2.models.plant.factory;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.view.GameScreen;
import com.pvz2.view.PlantGraphic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PlantFactory {
    private static final Map<PlantType, Supplier<Plant>> REGISTRY = new HashMap<>();

    public PlantFactory() {
        SunProducerFactory.register(REGISTRY);
        ShooterFactory.register(REGISTRY);
        ExplosiveFactory.register(REGISTRY);
        MeleeFactory.register(REGISTRY);
        WallNutFactory.register(REGISTRY);
        ModifierAndHomingFactory.register(REGISTRY);
        MintFactory.register(REGISTRY);
        REGISTRY.put(PlantType.MARIGOLD, () -> new Plant(PlantType.MARIGOLD, 300, 0));
    }

    public static Plant createPlant(PlantType type, int x, int y, Cell cell) {
        java.util.function.Supplier<Plant> plantSupplier = REGISTRY.get(type);

        if (plantSupplier == null) {
            GameMenuController.updateState("Error: Plant type " + type.name() + " is not registered in PlantFactory!");
            return null;
        }

        Plant newPlant = REGISTRY.get(type).get();
        newPlant.setCell(cell);
        newPlant.setX(x);
        newPlant.setY(y);
        return newPlant;
    }

    public static Plant createPlant(PlantType type, int x, int y, Cell cell, GameScreen gameScreen){
        Plant plant = createPlant(type, x, y, cell);
        if (plant != null){
            gameScreen.getPlantGraphics().add(new PlantGraphic(plant,
                gameScreen.getGame().pamPlayer));
            return plant;
        }
        return null;
    }

}
