package com.pvz2.models.plant.card;

import com.pvz2.models.enums.PlantType;

public class PlantCard {
    private final PlantType type;
    private int sunCost;
    private int maxCooldownTicks;
    private int currentCooldownTicks = 0;
    private boolean ready = true;
    private boolean activeCooldown = true;

    public PlantCard(PlantType type, int sunCost, int maxCooldownTicks) {
        this.type = type;
        this.sunCost = sunCost;
        this.maxCooldownTicks = maxCooldownTicks;
    }

    public void update(float delta) {
        if (!activeCooldown) return;
        if (ready) return;
        currentCooldownTicks++;

        if (currentCooldownTicks >= maxCooldownTicks) {

            ready = true;
            currentCooldownTicks = 0;
        }
    }

    public void deactivateCooldown() {
        ready = true;
        activeCooldown = false;
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

    public void reset() {
        ready = true;
        currentCooldownTicks = 0;
    }

    public int getSunCost() {
        return sunCost;
    }

    public void setSunCost(int sunCost) {
        this.sunCost = sunCost;
    }

    public int getMaxCooldownTicks() {
        return maxCooldownTicks;
    }

    public void setMaxCooldownTicks(int maxCooldownTicks) {
        this.maxCooldownTicks = maxCooldownTicks;
    }

    public int getCurrentCooldownTicks() {
        return currentCooldownTicks;
    }


}
