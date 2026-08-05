package com.pvz2.models.plant.components;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.miniGame.IZombie.IZombieLevel;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Sun;

import java.util.ArrayList;

public class SunProducerComponent implements GameComponent {
    private final ArrayList<Sun> componentSuns = new ArrayList<>();
    private int sunSize; //مقدار خورشید تولیدی
    private int sunNumber;
    private int lastProductionTicks;
    private int productionTime;
    private boolean doubleSunChance;
    private boolean shroom;
    private long plantationTime;
    private int sunNumberWithPlantFood;
    private boolean checkShroomSize;
    private boolean enable;
    private int growTimeToReduce;
    private boolean isInstant;

    public SunProducerComponent(int sunSize, int sunNumber, int productionTime, boolean doubleSunChance,
                                boolean shroom, int sunNumberWithPlantFood, int growTimeToReduce) {
        this(sunSize, sunNumber, productionTime, doubleSunChance, shroom, sunNumberWithPlantFood,
                growTimeToReduce, false);
    }

    public SunProducerComponent(int sunSize, int sunNumber, int productionTime, boolean doubleSunChance,
                                boolean shroom, int sunNumberWithPlantFood, int growTimeToReduce, boolean isInstant) {
        this.sunSize = sunSize;
        this.sunNumber = sunNumber;
        this.productionTime = productionTime;
        this.doubleSunChance = doubleSunChance;
        this.shroom = shroom;
        this.checkShroomSize = shroom;
        this.sunNumberWithPlantFood = sunNumberWithPlantFood;
        this.growTimeToReduce = growTimeToReduce;
        this.isInstant = isInstant;
        this.lastProductionTicks = productionTime * 10 - 10;
    }

    @Override
    public void update(Plant owner) {

        if (App.getCurrentGame() instanceof IZombieLevel) return;
        tick();

        if (shroom && checkShroomSize) {
            if (plantationTime > 10 * (72 - growTimeToReduce)) {
                checkShroomSize = false;
                setSunSize(75);
            } else if (plantationTime > 10 * (24 - growTimeToReduce)) {
                setSunSize(50);
            }
        }

        if (lastProductionTicks >= productionTime * 10 || isInstant) {
            enable = false;
            lastProductionTicks = 0;
            for (int i = 0; i < sunNumber; i++) {
                if (doubleSunChance && Math.random() < 0.2) componentSuns.add(produceSun(owner));
                componentSuns.add(produceSun(owner));
            }
            if (isInstant) {
                owner.die();
                return;
            }
        }

        if (!isInstant && componentSuns.isEmpty()) {
            enable = true;
        }


    }

    @Override
    public void activatePlantFood(Plant owner) {
        plantFoodEffect(owner);
    }

    private Sun produceSun(Plant owner) {
        Sun newSun = App.getCurrentGame().getSunsPool().acquire();
        newSun.reset(owner.getX(), owner.getY(), sunSize, this);
        App.getCurrentGame().getActiveSuns().add(newSun);
        GameMenuController.updateState("plant " + owner.getType().name() +
                " produced a sun at (" + owner.getX() + ", " + owner.getY() + ")");
        return newSun;
    }


    public void plantFoodEffect(Plant owner) {
        if (shroom) {
            setSunSize(75);
        }
        for (int i = 0; i < sunNumberWithPlantFood; i++) {
            componentSuns.add(produceSun(owner));
        }
    }

    private void setSunSize(int newSize) {
        this.sunSize = newSize;
    }

    private void tick() {
        plantationTime++;
        if (enable) {
            lastProductionTicks++;
        }
    }

    public ArrayList<Sun> getComponentSuns() {
        return componentSuns;
    }
}
