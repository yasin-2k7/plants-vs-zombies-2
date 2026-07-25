package models.plant.factory;

import controller.LevelMenuController;
import models.core.App;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.components.ExplosivesComponent;
import models.plant.components.explosionRanges.CircularRange;
import models.plant.components.explosionRanges.LineRange;
import models.plant.components.explosiveBehaviors.*;
import models.plant.components.explosiveTriggers.InstantTrigger;
import models.plant.components.explosiveTriggers.ProximityTrigger;
import models.world.Cell;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ExplosiveFactory {

    static void register(Map<PlantType, Supplier<Plant>> registry) {
        registry.put(PlantType.POTATO_MINE, ExplosiveFactory::buildPotatoMine);
        registry.put(PlantType.PRIMAL_POTATO_MINE, ExplosiveFactory::buildPrimalPotatoMine);
        registry.put(PlantType.CHERRY_BOMB, ExplosiveFactory::buildCherryBomb);
        registry.put(PlantType.SQUASH, ExplosiveFactory::buildSquash);
        registry.put(PlantType.GRAPESHOT, ExplosiveFactory::buildGrapeshot);
        registry.put(PlantType.JALAPENO, ExplosiveFactory::buildJalapeno);
        registry.put(PlantType.DOOM_SHROOM, ExplosiveFactory::buildDoomShroom);
        registry.put(PlantType.TANGLE_KELP, ExplosiveFactory::buildTangleKelp);
        registry.put(PlantType.ICEBERG_LETTUCE, ExplosiveFactory::buildIcebergLettuce);
        registry.put(PlantType.ICE_SHROOM, ExplosiveFactory::buildIceShroom);
        registry.put(PlantType.HOT_POTATO, ExplosiveFactory::buildHotPotato);
        registry.put(PlantType.GRAVE_BUSTER, ExplosiveFactory::buildGraveBuster);
    }


    private static Plant buildPotatoMine() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.POTATO_MINE);
        int armTime = level >= 2 ? 120 : 150;
        int damage = level >= 4 ? 2400 : 1800;
        Plant p = new Plant(PlantType.POTATO_MINE, 1000, damage);
        ExplosivesComponent component = new ExplosivesComponent(
                new ProximityTrigger(App.getCellWidth()),
                new AreaDamageBehavior(damage, new CircularRange(0)), armTime);
        component.setPlantFoodBehavior((owner, comp) -> {
            comp.instantArm();

            List<Cell> emptyCells = App.getCurrentGame().findTwoEmptyCell(false);

            for (Cell cell : emptyCells) {
                Plant cloneMine = buildPotatoMine();
                cloneMine.getComponent(ExplosivesComponent.class).instantArm();
                cloneMine.getComponent(ExplosivesComponent.class).setPlantFoodBehavior(null);
                cell.setPlant(cloneMine, PlantLayer.MAIN);
            }
        });
        p.addComponent(component);
        return p;
    }

    private static Plant buildPrimalPotatoMine() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PRIMAL_POTATO_MINE);
        int armTime = level >= 2 ? 40 : 50;
        int damage = level >= 4 ? 2800 : 2400;
        Plant p = new Plant(PlantType.PRIMAL_POTATO_MINE, 1000, damage);
        ExplosivesComponent component = new ExplosivesComponent(
                new ProximityTrigger(App.getCellWidth()),
                new AreaDamageBehavior(damage, new CircularRange(1)), armTime);
        component.setPlantFoodBehavior((owner, comp) -> {
            comp.instantArm();

            List<Cell> emptyCells = App.getCurrentGame().findTwoEmptyCell(false);

            for (Cell cell : emptyCells) {
                Plant cloneMine = buildPotatoMine();
                cloneMine.getComponent(ExplosivesComponent.class).instantArm();
                cloneMine.getComponent(ExplosivesComponent.class).setPlantFoodBehavior(null);
                cell.setPlant(cloneMine, PlantLayer.MAIN);
            }
        });
        p.addComponent(component);
        return p;
    }

    private static Plant buildCherryBomb() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.CHERRY_BOMB);
        int damage = level >= 3 ? 2400 : 1800;
        Plant p = new Plant(PlantType.CHERRY_BOMB, 1000, damage);
        p.addComponent(new ExplosivesComponent(
                InstantTrigger.INSTANCE,
                new AreaDamageBehavior(damage, new CircularRange(1)), 0));
        return p;
    }

    private static Plant buildSquash() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SQUASH);
        int damage = level >= 3 ? 2400 : 1800;
        Plant p = new Plant(PlantType.SQUASH, 1000, damage);
        ExplosivesComponent component = new ExplosivesComponent(
                new ProximityTrigger(App.getCellWidth() * 3),
                new AreaDamageBehavior(damage, new CircularRange(0)), 0);
        if (level >= 4) component.setLives(2);
        p.addComponent(component);
        component.setPlantFoodBehavior((owner, comp) -> {
            List<Zombie> allZombies = new ArrayList<>(App.getCurrentGame().getActiveZombies());
            Collections.shuffle(allZombies);
            allZombies.stream()
                    .limit(2)
                    .forEach(Zombie::die);
        });
        return p;
    }

    private static Plant buildGrapeshot() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.GRAPESHOT);
        int damage = level >= 2 ? 2400 : 1800;
        int bounceMax = level >= 3 ? 4 : 3;
        Plant p = new Plant(PlantType.GRAPESHOT, 1000, damage);
        p.addComponent(new ExplosivesComponent(InstantTrigger.INSTANCE,
                new CompositeBehavior(new AreaDamageBehavior(
                        damage, new CircularRange(1)), new GrapeshotBehavior(bounceMax)), 0));
        return p;
    }

    private static Plant buildJalapeno() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.JALAPENO);
        int damage = level >= 3 ? 2400 : 1800;
        Plant p = new Plant(PlantType.JALAPENO, 1000, damage);
        p.addComponent(new ExplosivesComponent(InstantTrigger.INSTANCE,
                new CompositeBehavior(new AreaDamageBehavior(damage, LineRange.INSTANCE),
                        new MeltIceBehavior(LineRange.INSTANCE)), 0));
        return p;
    }

    private static Plant buildDoomShroom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.DOOM_SHROOM);
        int damage = level >= 3 ? 2600 : 1800;
        Plant p = new Plant(PlantType.DOOM_SHROOM, 1000, damage);
        p.addComponent(new ExplosivesComponent(InstantTrigger.INSTANCE,
                new CompositeBehavior(new AreaDamageBehavior(damage,
                        new CircularRange(2)), new MakeUnplantableBehavior(new CircularRange(0))), 0));
        return p;
    }

    private static Plant buildTangleKelp() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.TANGLE_KELP);
        Plant p = new Plant(PlantType.TANGLE_KELP, 1000, 20000);
        ExplosivesComponent component = new ExplosivesComponent(
                new ProximityTrigger(App.getCellWidth()),
                new SingleTargetDamageBehavior(new CircularRange(0)), 0);
        if (level >= 3) component.setLives(2);
        component.setPlantFoodBehavior((owner, comp) -> {
            List<Zombie> waterZombies = App.getCurrentGame().getActiveZombies().stream()
                    .filter(zombie -> {
                        Cell zombieCell = Cell.findZombieCell(LevelMenuController.getGameCells(), zombie);
                        return zombieCell != null && zombieCell.isWater();
                    })
                    .collect(Collectors.toList());
            Collections.shuffle(waterZombies);
            waterZombies.stream()
                    .limit(3)
                    .forEach(Zombie::die);
        });
        p.addComponent(component);
        return p;
    }

    private static Plant buildIcebergLettuce() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.ICEBERG_LETTUCE);
        int freezeTime = level >= 3 ? 60 : 40;
        Plant p = new Plant(PlantType.ICEBERG_LETTUCE, 1000, 0);
        ExplosivesComponent component = new ExplosivesComponent(
                new ProximityTrigger(App.getCellWidth()),
                new FreezeZombieBehavior(new CircularRange(0), freezeTime), 0);
        component.setPlantFoodBehavior((owner, comp) -> {
            App.getCurrentGame().getActiveZombies()
                    .forEach(zombie -> zombie.freeze(40));
        });
        p.addComponent(component);
        return p;
    }

    private static Plant buildIceShroom() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.ICE_SHROOM);
        int freezeTime = level >= 2 ? 60 : 40;
        int damage = level >= 4 ? 50 : 0;
        Plant p = new Plant(PlantType.ICE_SHROOM, 1000, damage);
        p.addComponent(new ExplosivesComponent(InstantTrigger.INSTANCE,
                new CompositeBehavior(new FreezeZombieBehavior(new CircularRange(10), freezeTime),
                        new AreaDamageBehavior(damage, new CircularRange(10))), 0));
        return p;
    }

    private static Plant buildHotPotato() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.HOT_POTATO);
        int radius = level >= 3 ? 1 : 0;
        Plant p = new Plant(PlantType.HOT_POTATO, 1000, 0);
        ExplosivesComponent component = new ExplosivesComponent(InstantTrigger.INSTANCE,
                new MeltIceBehavior(new CircularRange(radius)), 0);
        if (level >= 4) {
            component.scheduleDelayedBehavior(new AreaDamageBehavior(100, new CircularRange(1)), 20);
        }
        return p;
    }

    private static Plant buildGraveBuster() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.GRAVE_BUSTER);
        int delay = level >= 2 ? 30 : 20;
        Plant p = new Plant(PlantType.GRAVE_BUSTER, 300, 0);
        ExplosivesComponent component = new ExplosivesComponent(
                InstantTrigger.INSTANCE, new RemoveGraveBehavior(), 0, delay);
        if (level >= 4) {
            component.scheduleDelayedBehavior(new AreaDamageBehavior(100, new CircularRange(1)), delay);
        }
        p.addComponent(component);
        return p;
    }
}
