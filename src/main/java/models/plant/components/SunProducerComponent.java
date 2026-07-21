package models.plant.components;

import controller.GameMenuController;
import models.core.App;
import models.enums.PlantType;
import models.plant.GameComponent;
import models.plant.Plant;
import models.world.Sun;

import java.util.ArrayList;

public class SunProducerComponent implements GameComponent {
    private int sunSize; //مقدار خورشید تولیدی
    private int sunNumber;
    private long lastProductionTime = System.currentTimeMillis();
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

    private final ArrayList<Sun> componentSuns = new ArrayList<>();

    public SunProducerComponent(int sunSize, int sunNumber, int productionTime, boolean doubleSunChance, boolean shroom, int sunNumberWithPlantFood, int growTimeToReduce) {
        this(sunSize, sunNumber, productionTime, doubleSunChance, shroom, sunNumberWithPlantFood, growTimeToReduce, false);
    }

    public SunProducerComponent(int sunSize, int sunNumber, int productionTime, boolean doubleSunChance, boolean shroom, int sunNumberWithPlantFood, int growTimeToReduce, boolean isInstant) {
        this.sunSize = sunSize;
        this.sunNumber = sunNumber;
        this.productionTime = productionTime;
        this.doubleSunChance = doubleSunChance;
        this.shroom = shroom;
        this.sunNumberWithPlantFood = sunNumberWithPlantFood;
        this.growTimeToReduce = growTimeToReduce;
        this.isInstant = isInstant;
        this.lastProductionTicks = productionTime * 10 - 10;
    }

    @Override
    public void update(Plant owner) {

        tick();

        if (shroom && checkShroomSize){
            if (plantationTime > 10*(72-growTimeToReduce)){
                checkShroomSize = false;
                setSunSize(75);
            }
            else if (plantationTime > 10*(24-growTimeToReduce)){
                setSunSize(50);
            }
        }

        if (lastProductionTicks >= productionTime*10){
            enable = false;
            lastProductionTicks = 0;
            for (int i = 0; i < sunNumber; i++){
                componentSuns.add(produceSun(owner));
            }
            if (isInstant) {
                if (owner.getCell() != null) {
                    owner.getCell().findAndRemovePlant();
                }
                owner.die();
                return;
            }
        }

        if (!isInstant && componentSuns.isEmpty()){
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
        GameMenuController.updateState("plant " + owner.getType().name() + " produced a sun at (" + owner.getX() + ", " + owner.getY() + ")");
        return newSun;
    }



    public void plantFoodEffect(Plant owner){
        if (shroom){
            setSunSize(75);
        }
        for (int i = 0; i < sunNumberWithPlantFood; i++){
            componentSuns.add(produceSun(owner));
        }
    }

    private void setSunSize(int newSize){
        this.sunSize = newSize;
    }

    private void tick(){
        plantationTime++;
        if (enable){
            lastProductionTicks++;
        }
    }

    public ArrayList<Sun> getComponentSuns() {
        return componentSuns;
    }
}