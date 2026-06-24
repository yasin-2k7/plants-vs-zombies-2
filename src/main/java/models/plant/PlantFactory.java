package models.plant;

import models.core.App;
import models.enums.PlantType;
import models.plant.components.SunProducerComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PlantFactory {
    private final Map<PlantType, Supplier<Plant>> registry = new HashMap<>();

    public PlantFactory(){
        registry.put(PlantType.SUNFLOWER, this::buildSunflower);
        registry.put(PlantType.TWIN_SUNFLOWER, this::buildTwinSunflower);
        registry.put(PlantType.SUN_SHROOM, this::buildSunShroom);
        registry.put(PlantType.PRIMAL_SUNFLOWER, this::buildPrimalSunflower);
        registry.put(PlantType.GOLD_BLOOM, this::buildGoldBloom);
    }

    public Plant createPlant(PlantType type, int x, int y) {
        Plant newPlant = registry.get(type).get();
        newPlant.setX(x);
        newPlant.setY(y);
        return newPlant;
    }

    private Plant buildSunflower() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SUNFLOWER);
        int health = level >= 3 ? 450 : 300;
        int prodTime = level >= 2 ? 24 : 22;
        boolean doubleSunChance = level == 4;
        Plant p = new Plant(PlantType.SUNFLOWER, health, 0, 0, 0);
        p.addComponent(new SunProducerComponent(50, 1, prodTime, doubleSunChance, false, 3, 0));
        return p;
    }

    private Plant buildTwinSunflower() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.TWIN_SUNFLOWER);
        int health = level >= 3 ? 450 : 300;
        int prodTime = level >= 2 ? 24 : 22;
        Plant p = new Plant(PlantType.TWIN_SUNFLOWER, health, 0, 0, 0);
        p.addComponent(new SunProducerComponent(50, 2, prodTime, false, false, 5, 0));
        return p;
    }

    private Plant buildSunShroom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SUN_SHROOM);
        int health = level >= 3 ? 450 : 300;
        int growTimeReduce = level >= 2 ? 5 : 0;
        boolean doubleSunChance = level == 4;
        Plant p = new Plant(PlantType.SUN_SHROOM, health, 0, 0, 0);
        p.addComponent(new SunProducerComponent(25, 1, 24, doubleSunChance, true, 3, growTimeReduce));
        return p;
    }

    private Plant buildPrimalSunflower() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PRIMAL_SUNFLOWER);
        int health = level >= 3 ? 450 : 300;
        int prodTime = level >= 2 ? 24 : 22;
        Plant p = new Plant(PlantType.PRIMAL_SUNFLOWER, health, 0, 0, 0);
        p.addComponent(new SunProducerComponent(75, 1, prodTime, false, false, 3, 0));
        return p;
    }

    private Plant buildGoldBloom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.GOLD_BLOOM);
        Plant p = new Plant(PlantType.SUN_SHROOM, 0, 0, 0, 0);
        int sunNumber = level >= 3 ? 17 : 15;
        p.addComponent(new SunProducerComponent(25, sunNumber, 0, false, false, 0, 0));
        return p;
    }

// not completed...

    private Plant buildSunBean(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SUN_BEAN);
        int health = level >= 3 ? 450 : 300;
        int sunSize = level >= 2 ? 10 : 5;
        Plant p = new Plant(PlantType.SUN_BEAN, health, 0, 0, 0);
        p.addComponent(new SunProducerComponent(sunSize, 1, 0, false, false, 0, 0));
        // next component
        return p;
    }



}
