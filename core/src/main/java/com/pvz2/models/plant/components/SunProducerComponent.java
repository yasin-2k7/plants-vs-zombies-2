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
    private float lastProductionTime;
    private float productionTime;
    private boolean doubleSunChance;
    private boolean shroom;
    private float plantationTime;
    private int sunNumberWithPlantFood;
    private boolean checkShroomSize;
    private boolean enable = true;
    private float growTimeToReduce;
    private boolean isInstant;
    private final float actionTimeInterval;
    private final float actionTime;
    private float currentActionTimer = 0;

    public SunProducerComponent(int sunSize, int sunNumber, float productionTime, boolean doubleSunChance,
                                boolean shroom, int sunNumberWithPlantFood, float growTimeToReduce, float actionTime, float actionTimeInterval) {
        this(sunSize, sunNumber, productionTime, doubleSunChance, shroom, sunNumberWithPlantFood,
                growTimeToReduce, false, actionTime, actionTimeInterval);
    }

    public SunProducerComponent(int sunSize, int sunNumber, float productionTime, boolean doubleSunChance,
                                boolean shroom, int sunNumberWithPlantFood,
                                float growTimeToReduce, boolean isInstant, float actionTime,
                                float actionTimeInterval) {
        this.sunSize = sunSize;
        this.sunNumber = sunNumber;
        this.productionTime = productionTime;
        this.doubleSunChance = doubleSunChance;
        this.shroom = shroom;
        this.checkShroomSize = shroom;
        this.sunNumberWithPlantFood = sunNumberWithPlantFood;
        this.growTimeToReduce = growTimeToReduce;
        this.isInstant = isInstant;
        this.lastProductionTime = productionTime - 2;
        this.actionTimeInterval = actionTimeInterval;
        this.actionTime = actionTime;
    }

    @Override
    public void update(Plant owner, float delta) {

        if (App.getCurrentGame() instanceof IZombieLevel) return;
        tick(owner, delta);

        if (shroom && checkShroomSize) {
            if (plantationTime > (72 - growTimeToReduce)) {
                checkShroomSize = false;
                setSunSize(75);
            } else if (plantationTime > (24 - growTimeToReduce)) {
                setSunSize(50);
            }
        }

        if (lastProductionTime >= productionTime || isInstant) {
            if (owner.getState() == Plant.State.IDLE){
                owner.setState(Plant.State.SPECIAL);
            }
            currentActionTimer += delta;
            if (currentActionTimer < actionTime) return;

            enable = false;
            lastProductionTime = 0;
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

    private void tick(Plant owner, float delta) {
        plantationTime += delta;
        if (currentActionTimer >= actionTime){
            currentActionTimer += delta;
            if (currentActionTimer >= actionTimeInterval){
                currentActionTimer = 0;
                owner.setState(Plant.State.IDLE);
            }
        }
        if (enable) {
            lastProductionTime += delta;
        }
    }

    public ArrayList<Sun> getComponentSuns() {
        return componentSuns;
    }
}
