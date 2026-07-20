package models.plant.factory;

import models.core.App;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.components.*;
import models.plant.components.explosiveBehaviors.*;
import models.world.Cell;
import java.util.*;
import java.util.function.Supplier;

public class PlantFactory {
    private static final Map<PlantType, Supplier<Plant>> registry = new HashMap<>();

    public PlantFactory(){
        SunProducerFactory.register(registry);
        ShooterFactory.register(registry);
        ExplosiveFactory.register(registry);
        MeleeFactory.register(registry);
    }

    public static Plant createPlant(PlantType type, int x, int y, Cell cell) {
        java.util.function.Supplier<Plant> plantSupplier = registry.get(type);

        if (plantSupplier == null) {
            System.out.println("Error: Plant type " + type.name() + " is not registered in PlantFactory!");
            return null;
        }

        Plant newPlant = registry.get(type).get();
        newPlant.setCell(cell);
        newPlant.setX(x);
        newPlant.setY(y);
        return newPlant;
    }


// not completed...

    private Plant buildSunBean(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SUN_BEAN);
        int health = level >= 3 ? 450 : 300;
        int sunSize = level >= 2 ? 10 : 5;
        Plant p = new Plant(PlantType.SUN_BEAN, health, 0);
        p.addComponent(new SunProducerComponent(sunSize, 1, 0, false, false, 0, 0));
        // next component
        return p;
    }

    public static boolean isPlantSupported(PlantType type) {
        return registry.containsKey(type);
    }
}