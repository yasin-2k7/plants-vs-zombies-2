package models.world;

import models.plant.Plant;

public class Cell {
    private int row;
    private int col;
    private Plant plant;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isEmpty(){
        return plant == null;
    }

    public void plantPlant(Plant p){
        this.plant = p;
    }

    public void removePlant(){
        this.plant = null;
    }

    public Plant getPlant() {
        return plant;
    }
}