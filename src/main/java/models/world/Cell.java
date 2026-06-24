package models.world;

import models.plant.Plant;

public class Cell {

    private Plant plant;

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public boolean isEmpty() {
        return plant == null;
    }
}