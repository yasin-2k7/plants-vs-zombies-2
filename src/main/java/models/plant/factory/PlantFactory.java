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
        WallNutFactory.register(registry);
        ModifierAndHomingFactory.register(registry);
        MintFactory.register(registry);
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

    public static boolean isPlantSupported(PlantType type) {
        return registry.containsKey(type);
    }
}