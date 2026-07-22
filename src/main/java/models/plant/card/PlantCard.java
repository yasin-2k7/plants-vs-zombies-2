package models.plant.card;

import models.enums.PlantType;

public class PlantCard {
    private final PlantType type;
    private int sunCost;
    private int maxCooldownTicks;
    private int currentCooldownTicks = 0;
    private boolean ready = false;
    private boolean activeCooldown = true;

    public PlantCard(PlantType type, int sunCost, int maxCooldownTicks) {
        this.type = type;
        this.sunCost = sunCost;
        this.maxCooldownTicks = maxCooldownTicks;
    }

    public void update(){
        if (!activeCooldown) return;
        currentCooldownTicks++;

        if (currentCooldownTicks >= maxCooldownTicks){

            ready = true;
            currentCooldownTicks = 0;
        }
    }

    public void deactivateCooldown(){
        ready = true;
        activeCooldown = false;
    }

    public void setActiveCooldown(){
        activeCooldown = true;
    }

    public PlantType getType() {
        return type;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        if (!ready && !activeCooldown) return;
        this.ready = ready;
    }

    public void reset(){
        ready = true;
        currentCooldownTicks = 0;
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

    public int getCurrentCooldownTicks() {
        return currentCooldownTicks;
    }
}
