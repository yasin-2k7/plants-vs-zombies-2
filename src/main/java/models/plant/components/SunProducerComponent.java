package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class SunProducerComponent implements GameComponent {
    private int sunSize; //مقدار خورشید تولیدی
    private int sunNumber;
    private long lastProductionTime = System.currentTimeMillis();
    private int lastProductionTicks;
    private int productionTime;
    private boolean doubleSunChance;
    private boolean shroom;
    private long plantationTime;

    @Override
    public void update(Plant owner) {
        // TODO: بررسی زمان گذشته شده (مثلا هر 20 ثانیه یک بار خورشید تولید شود)
        produceSun();
    }

    private void produceSun() {
        // TODO: اضافه کردن خورشید به زمین بازی یا مستقیم به امتیاز کاربر
        // System.out.println("Produced " + sunAmount + " sun!");
    }

    public void plantFoodEffect(){

    }

    private void setSunSize(int newSize){
        this.sunSize = newSize;
    }



}