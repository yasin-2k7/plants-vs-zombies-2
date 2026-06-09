package models.plant.state;

import models.plant.Plant;

public class IdlePlantState implements PlantState{
    @Override
    public void action(Plant owner) {
        // TODO: بررسی اینکه آیا زامبی در لاین وجود دارد یا خیر
        // اگر زامبی بود، وضعیت گیاه را به ActionPlantState تغییر بده
        //System.out.println("Plant is idle, watching for zombies...");
    }
}
