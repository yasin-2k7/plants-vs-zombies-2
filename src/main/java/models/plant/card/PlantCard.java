package models.plant.card;

import models.enums.PlantType;

public class PlantCard {
    private final PlantType type;
    private int sunCost;
    private int maxCooldownTicks;
    private int currentCooldownTicks;

    public PlantCard(PlantType type, int sunCost, int maxCooldownTicks) {
        this.type = type;
        this.sunCost = sunCost;
        this.maxCooldownTicks = maxCooldownTicks;
    }

    public int getSunCost() {
        return sunCost;
    }

    public int getMaxCooldownTicks() {
        return maxCooldownTicks;
    }

    public void setSunCost(int sunCost) {
        this.sunCost = sunCost;
    }

    public void setMaxCooldownTicks(int maxCooldownTicks) {
        this.maxCooldownTicks = maxCooldownTicks;
    }
}
