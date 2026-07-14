package models.plant;

import models.core.App;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.enums.ProjectileType;
import models.plant.components.*;
import models.plant.components.shooterPlantFoodBehaviors.RandomTargetPlantFood;
import models.plant.visions.RotatedVisionStrategy;
import models.plant.visions.StraightVisionStrategy;
import models.projectile.hitStrategies.CombinedDamageStrategy;
import models.projectile.movementStrategies.BowlingMovementStrategy;
import models.projectile.movementStrategies.LobbedMovementStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.movementStrategies.StraightMovementStrategy;
import models.projectile.strikeStrategies.CheckLobbedStrike;
import models.projectile.strikeStrategies.CheckStraightStrike;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PlantFactory {
    private static final Map<PlantType, Supplier<Plant>> registry = new HashMap<>();

    public PlantFactory(){
        registry.put(PlantType.SUNFLOWER, this::buildSunflower);
        registry.put(PlantType.TWIN_SUNFLOWER, this::buildTwinSunflower);
        registry.put(PlantType.SUN_SHROOM, this::buildSunShroom);
        registry.put(PlantType.PRIMAL_SUNFLOWER, this::buildPrimalSunflower);
        registry.put(PlantType.GOLD_BLOOM, this::buildGoldBloom);
        registry.put(PlantType.PEASHOOTER, this::buildPeaShooter);
        registry.put(PlantType.REPEATER, this::buildRepeater);
        registry.put(PlantType.THREEPEATER, this::buildThreepeater);
        registry.put(PlantType.SNOW_PEA, this::buildSnowPea);
        registry.put(PlantType.ROTOBAGA, this::buildRotobaga);
        registry.put(PlantType.PEA_POD, this::buildPeaPod);
        registry.put(PlantType.SPLIT_PEA, this::buildSplitPea);
        registry.put(PlantType.CITRON, this::buildCitron);
        registry.put(PlantType.BOWLING_BULB, this::buildBowlingBulb);
        registry.put(PlantType.CACTUS, this::buildCactus);
        registry.put(PlantType.FIRE_PEASHOOTER, this::buildFirePeashooter);
        registry.put(PlantType.STARFRUIT, this::buildStarfruit);
        registry.put(PlantType.GOO_PEASHOOTER, this::buildGooPeashooter);
        registry.put(PlantType.MEGA_GATLING_PEA, this::buildMegaGatlingPea);
        registry.put(PlantType.SEA_SHROOM, this::buildSeaShroom);
        registry.put(PlantType.PUFF_SHROOM, this::buildPuffShroom);
        registry.put(PlantType.FUME_SHROOM, this::buildFumeShroom);
        registry.put(PlantType.CABBAGE_PULT, this::buildCabbagePult);
        registry.put(PlantType.KERNEL_PULT, this::buildKernelPult);
        registry.put(PlantType.MELON_PULT, this::buildMelonPult);
        registry.put(PlantType.WINTER_MELON, this::buildWinterMelonPult);
        registry.put(PlantType.PEPPER_PULT, this::buildPepperPult);
    }

    public static Plant createPlant(PlantType type, int x, int y) {
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
        Plant p = new Plant(PlantType.SUNFLOWER, health, 0);
        p.addComponent(new SunProducerComponent(50, 1, prodTime, doubleSunChance, false, 3, 0));
        return p;
    }

    private Plant buildTwinSunflower() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.TWIN_SUNFLOWER);
        int health = level >= 3 ? 450 : 300;
        int prodTime = level >= 2 ? 24 : 22;
        Plant p = new Plant(PlantType.TWIN_SUNFLOWER, health, 0);
        p.addComponent(new SunProducerComponent(50, 2, prodTime, false, false, 5, 0));
        return p;
    }

    private Plant buildSunShroom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SUN_SHROOM);
        int health = level >= 3 ? 450 : 300;
        int growTimeReduce = level >= 2 ? 5 : 0;
        boolean doubleSunChance = level == 4;
        Plant p = new Plant(PlantType.SUN_SHROOM, health, 0);
        p.addComponent(new SunProducerComponent(25, 1, 24, doubleSunChance, true, 3, growTimeReduce));
        return p;
    }

    private Plant buildPrimalSunflower() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PRIMAL_SUNFLOWER);
        int health = level >= 3 ? 450 : 300;
        int prodTime = level >= 2 ? 24 : 22;
        Plant p = new Plant(PlantType.PRIMAL_SUNFLOWER, health, 0);
        p.addComponent(new SunProducerComponent(75, 1, prodTime, false, false, 3, 0));
        return p;
    }

    private Plant buildGoldBloom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.GOLD_BLOOM);
        Plant p = new Plant(PlantType.SUN_SHROOM, 0, 0);
        int sunNumber = level >= 3 ? 17 : 15;
        p.addComponent(new SunProducerComponent(25, sunNumber, 0, false, false, 0, 0));
        return p;
    }

    private Plant buildPeaShooter(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PEASHOOTER);
        int health = level >= 3 ? 450 : 300;
        int damage = level >= 2 ? 30 : 20;
        Plant p = new Plant(PlantType.PEASHOOTER, health, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.PEA);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.PEA,
                                            null,
                                            15,
                                            1,
                                            20,
                                            false,
                                            () -> combinedDamageStrategy,
                                            new CheckStraightStrike(),
                                            0,
                                            1,
                                            0,
                                            0);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildRepeater(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.REPEATER);
        int health = level >= 3 ? 500 : 300;
        int damage = level >= 2 ? 30 : 20;
        Plant p = new Plant(PlantType.REPEATER, health, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.PEA);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.PEA,
                ProjectileType.GIANT_PEA,
                15,
                2,
                30,
                true,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                1,
                1,
                1,
                20);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildThreepeater(){ // plant food...
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.THREEPEATER);
        int health = level >= 4 ? 500 : 300;
        int damage = level >= 3 ? 30 : 20;
        Plant p = new Plant(PlantType.THREEPEATER, health, 0);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.PEA);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.PEA,
                null,
                15,
                1,
                30,
                false,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                0,
                1,
                0,
                0);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, 3*App.getCellHeight(), false));
        for (int i = -1; i <= 1; i++){
            final int finalI = i;
            MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, finalI*App.getCellHeight());
            newComponent.getMovementStrategies().add(() -> movementStrategy);
        }
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildSnowPea(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SNOW_PEA);
        int damage = level >= 2 ? 30 : 20;
        Plant p = new Plant(PlantType.SNOW_PEA, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.ICE_PEA);
        combinedDamageStrategy.setElement("ICE");
        if (level >= 3){
            combinedDamageStrategy.setChillTime(combinedDamageStrategy.getChillTime() + 20);
        }
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.ICE_PEA,
                null,
                15,
                1,
                20,
                false,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                0,
                1,
                0,
                0);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildRotobaga(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.ROTOBAGA);
        int damage = level >= 2 ? 20 : 10;
        int health = level >= 3 ? 450 : 300;
        Plant p = new Plant(PlantType.ROTOBAGA, health, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.ROTOBAGA_PROJECTILE);
        for (int i = 0; i < 4; i++){
            ShooterComponent shooterComponent = new ShooterComponent(ProjectileType.ROTOBAGA_PROJECTILE,
                    null,
                    15,
                    1,
                    20,
                    false,
                    () -> combinedDamageStrategy,
                    new CheckStraightStrike(),
                    0,
                    1,
                    0,
                    0);
            final int finalI = i;
            shooterComponent.getVisions().add(new RotatedVisionStrategy((float) (i * Math.PI/2 + Math.PI/4), App.getCellHeight(), 1000));
            MovementStrategy movementStrategy = new StraightMovementStrategy((float) (5*Math.cos(finalI * Math.PI/2 + Math.PI/4)), (float) (5*Math.sin(finalI * Math.PI/2 + Math.PI/4)),0);
            shooterComponent.getMovementStrategies().add(() -> movementStrategy);
            p.addComponent(shooterComponent);
        }
        return p;
    }

    private Plant buildPeaPod(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PEA_POD);
        int damage = level >= 2 ? 30 : 20;
        int health = level >= 3 ? 500 : 300;
        Plant p = new Plant(PlantType.PEA_POD, health, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.PEA);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.PEA,
                ProjectileType.GIANT_PEA,
                15,
                1,
                1,
                true,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                1,
                1,
                1,
                20);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);        p.addComponent(newComponent);
        p.addComponent(new PlacementBehaviorComponent(PlantLayer.MAIN, true, 5, false));
        return p;
    }

    private Plant buildSplitPea(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SPLIT_PEA);
        int damage = level >= 2 ? 30 : 20;
        int health = level >= 3 ? 500 : 300;
        Plant p = new Plant(PlantType.SPLIT_PEA, health, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.PEA);
        for (int i = 0; i <= 1; i++){
            ShooterComponent newComponent = new ShooterComponent(ProjectileType.PEA,
                    null,
                    15,
                    i+1,
                    20,
                    false,
                    () -> combinedDamageStrategy,
                    new CheckStraightStrike(),
                    0,
                    1,
                    0,
                    0);
            final int finalI = i;
            newComponent.getVisions().add(new StraightVisionStrategy(1000 * (float)Math.cos(i*Math.PI), App.getCellHeight(), false));
            MovementStrategy movementStrategy = new StraightMovementStrategy(5 * (float)Math.cos(finalI*Math.PI), 0, 0);
            newComponent.getMovementStrategies().add(() -> movementStrategy);
            p.addComponent(newComponent);
        }
        return p;
    }

    private Plant buildCitron(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.CITRON);
        int damage = level >= 3 ? 950 : 800;
        int chargeTime = level >= 2 ? 80 : 90;
        Plant p = new Plant(PlantType.CITRON, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.CITRON);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.CITRON,
                ProjectileType.PLASMA,
                chargeTime,
                1,
                1,
                true,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                1,
                1,
                100,
                20);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildBowlingBulb(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.BOWLING_BULB);
        int regenReduce = level >= 2 ? 1 : 0;
        int damageAddition = level >= 3 ? 15 : 0;
        CombinedDamageStrategy first = new CombinedDamageStrategy(180 + damageAddition, ProjectileType.LARGE_BULB);
        CombinedDamageStrategy second = new CombinedDamageStrategy(120 + damageAddition, ProjectileType.MEDIUM_BULB);
        CombinedDamageStrategy third = new CombinedDamageStrategy(40 + damageAddition, ProjectileType.SMALL_BULB);
        CombinedDamageStrategy special = new CombinedDamageStrategy(180 + damageAddition, 90 + damageAddition/2, 100, ProjectileType.MEDIUM_BULB);
        Plant p = new Plant(PlantType.BOWLING_BULB, 300, 0);
        p.addComponent(new BowlingChargeComponent(20, first, second, third, () -> new BowlingMovementStrategy(5, 0), special, 2-regenReduce, 5-regenReduce, 10-regenReduce));
        return p;
    }

    private Plant buildCactus(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.CACTUS);
        int damage = level >= 3 ? 40 : 30;
        int pierce = level >= 2 ? 4 : 3;
        Plant p = new Plant(PlantType.CACTUS, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.CACTUS);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.CACTUS,
                ProjectileType.CACTUS_SPECIAL,
                15,
                1,
                20,
                true,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                20,
                pierce,
                100,
                4);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildFirePeashooter(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.FIRE_PEASHOOTER);
        int damage = level >= 2 ? 60 : 40;
        int health = level >= 3 ? 500 : 300;
        Plant p = new Plant(PlantType.FIRE_PEASHOOTER, health, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.FIRE_PEA);
        combinedDamageStrategy.setElement("FIRE");
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.FIRE_PEA,
                null,
                15,
                1,
                20,
                false,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                0,
                1,
                0,
                0);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildStarfruit(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.STARFRUIT);
        int shootingTime = level >= 2 ? 13 : 15;
        int damage = level >= 3 ? 30 : 20;
        Plant p = new Plant(PlantType.STARFRUIT, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.STAR);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.STAR,
                null,
                shootingTime,
                1,
                20,
                false,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                0,
                1,
                0,
                0);
        float angel = (float) -Math.PI/3;
        for (int i = 0; i < 5; i++){
            int changeFactor = i == 1 ? 2 : 3;
            final float finalAngel = angel;
            newComponent.getVisions().add(new RotatedVisionStrategy(angel, App.getCellHeight(), 1000));
            MovementStrategy movementStrategy = new StraightMovementStrategy((float) (5*Math.cos(finalAngel)), (float) (5*Math.sin(finalAngel)),0);
            newComponent.getMovementStrategies().add(() -> movementStrategy);
            angel += (float) (changeFactor*Math.PI/6);
        }
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildGooPeashooter(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.GOO_PEASHOOTER);
        int health = level >= 3 ? 30 : 20;
        Plant p = new Plant(PlantType.GOO_PEASHOOTER, health, 20);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(20, ProjectileType.GOO);
        CombinedDamageStrategy plantFoodStrategy = new CombinedDamageStrategy(20, ProjectileType.GOO_SPECIAL);
        combinedDamageStrategy.setElement("POISON");
        plantFoodStrategy.setElement("POISON");
        if (level >= 2){
            combinedDamageStrategy.setPoisonDamageOnTick(combinedDamageStrategy.getPoisonDamageOnTick() + 5);
            plantFoodStrategy.setPoisonDamageOnTick(combinedDamageStrategy.getPoisonDamageOnTick());
        }
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.GOO,
                ProjectileType.GOO_SPECIAL,
                15,
                1,
                20,
                true,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                20,
                1,
                1,
                1);
        newComponent.setPlantFoodStrategy(plantFoodStrategy);

        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildMegaGatlingPea(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.MEGA_GATLING_PEA);
        int damage = level >= 2 ? 30 : 20;
        int plantFoodChance = level >= 3 ? 10 : 5;
        Plant p = new Plant(PlantType.MEGA_GATLING_PEA, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.PEA);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.PEA,
                ProjectileType.GIANT_PEA,
                15,
                4,
                30,
                true,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                4,
                1,
                1,
                20);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        newComponent.setAttackCallback(owner -> {
            if (Math.random() < plantFoodChance/100f) {
                owner.activatePlantFood();
            }
        });
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildSeaShroom(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SEA_SHROOM);
        float range = (level >= 2 ? 5 : 4) * App.getCellWidth();
        int damage = level >= 3 ? 25 : 20;
        int lifespan = level >= 4 ? 700 : 600;
        Plant p = new Plant(PlantType.SEA_SHROOM, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.SMALL_SHROOM);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.SMALL_SHROOM,
                ProjectileType.SMALL_SHROOM,
                15,
                1,
                20,
                false,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                0,
                1,
                0,
                0);
        newComponent.getVisions().add(new StraightVisionStrategy(range, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        p.addComponent(new LifespanComponent(PlantType.SEA_SHROOM, lifespan));
        return p;
    }

    private Plant buildPuffShroom(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PUFF_SHROOM);
        float range = (level >= 4 ? 5 : 4) * App.getCellWidth();
        int damage = level >= 3 ? 30 : 20;
        int lifespan = level >= 2 ? 700 : 600;
        Plant p = new Plant(PlantType.PUFF_SHROOM, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.SMALL_SHROOM);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.SMALL_SHROOM,
                ProjectileType.SMALL_SHROOM,
                15,
                1,
                20,
                false,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                0,
                1,
                0,
                0);
        newComponent.getVisions().add(new StraightVisionStrategy(range, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(5, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        p.addComponent(new LifespanComponent(PlantType.PUFF_SHROOM, lifespan));
        return p;
    }

    private Plant buildFumeShroom(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.FUME_SHROOM);
        float range = (level >= 2 ? 6 : 5) * App.getCellWidth();
        int damage = level >= 3 ? 30 : 20;
        Plant p = new Plant(PlantType.FUME_SHROOM, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.FUME);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.FUME,
                ProjectileType.FUME_SPECIAL,
                15,
                1,
                1,
                true,
                () -> combinedDamageStrategy,
                new CheckStraightStrike(),
                1,
                100,
                100,
                2);
        CombinedDamageStrategy plantFoodStrategy = new CombinedDamageStrategy(damage, ProjectileType.FUME_SPECIAL);
        plantFoodStrategy.setElement("MOVE");
        newComponent.setPlantFoodStrategy(plantFoodStrategy);
        newComponent.getVisions().add(new StraightVisionStrategy(range, App.getCellHeight(), false));
        MovementStrategy movementStrategy = new StraightMovementStrategy(10, 0, 0);
        newComponent.getMovementStrategies().add(() -> movementStrategy);
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildCabbagePult(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.CABBAGE_PULT);
        int damage = level >= 2 ? 50 : 40;
        int shootingTime = level >= 3 ? 25 : 29;
        int health = level >= 4 ? 450 : 300;
        Plant p = new Plant(PlantType.CABBAGE_PULT, health, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, ProjectileType.CABBAGE);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.CABBAGE,
                ProjectileType.SPECIAL_CABBAGE,
                shootingTime,
                1,
                0,
                false,
                () -> combinedDamageStrategy,
                new CheckLobbedStrike(),
                0,
                1,
                0,
                2);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), true));
        newComponent.getMovementStrategies().add(LobbedMovementStrategy::new);
        newComponent.setPlantFoodBehavior(new RandomTargetPlantFood(6));
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildKernelPult(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.KERNEL_PULT);
        float butterChance = level >= 2 ? 0.25f : 0.2f;
        int damageAddition = level >= 3 ? 10 : 0;
        int health = level >= 4 ? 450 : 300;
        Plant p = new Plant(PlantType.KERNEL_PULT, health, 0);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.KERNEL,
                ProjectileType.BUTTER,
                29,
                1,
                0,
                false,
                null,
                new CheckLobbedStrike(),
                0,
                1,
                0,
                0);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), true));
        newComponent.getMovementStrategies().add(LobbedMovementStrategy::new);
        newComponent.setAttackCallback(owner -> {
            if (Math.random() < butterChance) {
                newComponent.setBulletType(ProjectileType.BUTTER);
            } else {
                newComponent.setBulletType(ProjectileType.KERNEL);
            }
        });
        CombinedDamageStrategy kernelStrategy = new CombinedDamageStrategy(20 + damageAddition, ProjectileType.KERNEL);
        CombinedDamageStrategy butterStrategy = new CombinedDamageStrategy(40 + damageAddition, ProjectileType.BUTTER);
        butterStrategy.setElement("STUN");
        newComponent.setDamageStrategy(() -> {
            if (newComponent.getBulletType() == ProjectileType.KERNEL){
                return kernelStrategy;
            }
            return butterStrategy;
        });
        newComponent.setPlantFoodStrategy(butterStrategy);
        newComponent.setPlantFoodBehavior(new RandomTargetPlantFood());
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildMelonPult(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.MELON_PULT);
        int damage = level >= 3 ? 110 : 80;
        int aoeDamage = level >= 4 ? 55 : 40;
        Plant p = new Plant(PlantType.MELON_PULT, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(damage, aoeDamage, 200, ProjectileType.MELON);
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.MELON,
                ProjectileType.SPECIAL_MELON,
                29,
                1,
                0,
                false,
                () -> combinedDamageStrategy,
                new CheckLobbedStrike(),
                0,
                1,
                0,
                2);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), true));
        newComponent.getMovementStrategies().add(LobbedMovementStrategy::new);
        newComponent.setPlantFoodBehavior(new RandomTargetPlantFood(6));
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildWinterMelonPult(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.WINTER_MELON);
        int aoeDamage = level >= 3 ? 55 : 40;
        Plant p = new Plant(PlantType.WINTER_MELON, 300, 80);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(80, aoeDamage, 200, ProjectileType.ICE_MELON);
        combinedDamageStrategy.setElement("ICE");
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.ICE_MELON,
                ProjectileType.SPECIAL_ICE_MELON,
                29,
                1,
                0,
                false,
                () -> combinedDamageStrategy,
                new CheckLobbedStrike(),
                0,
                1,
                0,
                2);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), true));
        newComponent.getMovementStrategies().add(LobbedMovementStrategy::new);
        newComponent.setPlantFoodBehavior(new RandomTargetPlantFood(6));
        p.addComponent(newComponent);
        return p;
    }

    private Plant buildPepperPult(){
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PEPPER_PULT);
        int damage = level >= 2 ? 65 : 50;
        int radius = level >= 3 ? 250 : 200;
        Plant p = new Plant(PlantType.PEPPER_PULT, 300, damage);
        CombinedDamageStrategy combinedDamageStrategy = new CombinedDamageStrategy(80, damage/2, radius, ProjectileType.PEPPER);
        combinedDamageStrategy.setElement("FIRE");
        ShooterComponent newComponent = new ShooterComponent(ProjectileType.PEPPER,
                ProjectileType.SPECIAL_PEPPER,
                29,
                1,
                0,
                false,
                () -> combinedDamageStrategy,
                new CheckLobbedStrike(),
                0,
                1,
                0,
                2);
        newComponent.getVisions().add(new StraightVisionStrategy(1000, App.getCellHeight(), true));
        newComponent.getMovementStrategies().add(LobbedMovementStrategy::new);
        newComponent.setPlantFoodBehavior(new RandomTargetPlantFood(6));
        p.addComponent(newComponent);
        return p;
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



}
