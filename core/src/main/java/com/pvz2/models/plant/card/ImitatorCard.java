package com.pvz2.models.plant.card;

import com.pvz2.models.enums.PlantType;

public class ImitatorCard extends PlantCard {
    private final PlantType targetType;

    public ImitatorCard(PlantType targetType, int sunCost, int maxCooldownTicks) {
        super(PlantType.IMITATOR, sunCost, maxCooldownTicks);
        this.targetType = targetType;
    }

    public PlantType getTargetType() {
        return targetType;
    }
}
