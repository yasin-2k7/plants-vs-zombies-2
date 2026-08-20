package com.pvz2.models.plant.factory;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantLayer;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.AnimationDurations;
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
            private static final String PLANT_FOOD_INTRO_CLIP = "plantfood_intro";
            private static final String PLANT_FOOD_CLIP = "plantfood";
            private static final String PLANT_FOOD_OUTRO_CLIP = "plantfood_outro";
            private static final float DEFAULT_PLANT_FOOD_DURATION = 1.0f;

            private float plantFoodTimer = 0f;
            private float introOutroTimer = 0f;

            private float plantFoodIntroDuration = -1f;
            private float plantFoodDuration = -1f;
            private float plantFoodOutroDuration = -1f;

            private void ensurePlantFoodDurationsLoaded(Plant owner) {
                if (plantFoodDuration >= 0f) return;
                plantFoodIntroDuration = AnimationDurations.hasClip(owner.getType(), PLANT_FOOD_INTRO_CLIP)
                    ? AnimationDurations.getDuration(owner.getType(), PLANT_FOOD_INTRO_CLIP, 0f) : 0f;
                plantFoodDuration = AnimationDurations.getDuration(owner.getType(), PLANT_FOOD_CLIP, DEFAULT_PLANT_FOOD_DURATION);
                plantFoodOutroDuration = AnimationDurations.hasClip(owner.getType(), PLANT_FOOD_OUTRO_CLIP)
                    ? AnimationDurations.getDuration(owner.getType(), PLANT_FOOD_OUTRO_CLIP, 0f) : 0f;
            }

            @Override
            public void update(Plant owner, float delta) {
                ensurePlantFoodDurationsLoaded(owner);

                if (owner.getState() == Plant.State.PLANT_FOOD_INTRO) {
                    introOutroTimer -= delta;
                    if (introOutroTimer <= 0f) {
                        spawnClones();
                        plantFoodTimer = 0f;
                        owner.setState(Plant.State.PLANT_FOOD);
                    }
                    return;
                }

                if (owner.getState() == Plant.State.PLANT_FOOD) {
                    plantFoodTimer += delta;
                    if (plantFoodTimer >= plantFoodDuration) {
                        if (plantFoodOutroDuration > 0f) {
                            owner.setState(Plant.State.PLANT_FOOD_OUTRO);
                            introOutroTimer = plantFoodOutroDuration;
                        } else {
                            owner.setState(Plant.State.IDLE);
                        }
                    }
                    return;
                }

                if (owner.getState() == Plant.State.PLANT_FOOD_OUTRO) {
                    introOutroTimer -= delta;
                    if (introOutroTimer <= 0f) {
                        owner.setState(Plant.State.IDLE);
                    }
                }
            }

            @Override
            public void activatePlantFood(Plant owner) {
                ensurePlantFoodDurationsLoaded(owner);

                if (plantFoodIntroDuration > 0f) {
                    owner.setState(Plant.State.PLANT_FOOD_INTRO);
                    introOutroTimer = plantFoodIntroDuration;
                } else {
                    spawnClones();
                    plantFoodTimer = 0f;
                    owner.setState(Plant.State.PLANT_FOOD);
                }
            }

            private void spawnClones() {
                List<Cell> emptyCells = App.getCurrentGame().findTwoEmptyCell(true);

                for (Cell cell : emptyCells) {
                    Plant cloneLilyPad = buildLilyPad();
                    cell.setPlant(cloneLilyPad, PlantLayer.BASE);
                    cloneLilyPad.setCell(cell);
                    cloneLilyPad.setX((int) cell.getX());
                    cloneLilyPad.setY((int) cell.getY());
                    App.getCurrentGame().getActivePlants().add(cloneLilyPad);
                    GameMenuController.updateScreenPlants(cloneLilyPad);
                }
            }
        };
        p.addComponent(component);
        return p;
    }


}
