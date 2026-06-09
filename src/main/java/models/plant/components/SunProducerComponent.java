package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class SunProducerComponent implements GameComponent {
    private int sunAmount = 50; //مقدار خورشید تولیدی
    private long lastProductionTime = System.currentTimeMillis();

    @Override
    public void update(Plant owner) {
        // TODO: بررسی زمان گذشته شده (مثلا هر 20 ثانیه یک بار خورشید تولید شود)
        produceSun();
    }

    private void produceSun() {
        // TODO: اضافه کردن خورشید به زمین بازی یا مستقیم به امتیاز کاربر
        // System.out.println("Produced " + sunAmount + " sun!");
    }
}