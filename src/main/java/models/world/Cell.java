package models.world;

import models.core.App;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.components.PlacementBehaviorComponent;
import models.plant.components.ShooterComponent;


public class Cell {
    private int row;
    private int col;
    private int x;
    private int y;
    private Plant basePlant;
    private Plant mainPlant;
    private Plant shieldPlant;

    private boolean isWater;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isLayerEmpty(PlantLayer layer) {
        return switch (layer) {
            case BASE -> basePlant == null;
            case MAIN -> mainPlant == null;
            case SHIELD -> shieldPlant == null;
        };
    }

    public void setPlant(Plant plant, PlantLayer layer) {
        switch (layer) {
            case BASE -> this.basePlant = plant;
            case MAIN -> this.mainPlant = plant;
            case SHIELD -> this.shieldPlant = plant;
        }
    }

    public Plant getPlant(PlantLayer layer) {
        switch (layer) {
            case BASE -> {
                return basePlant;
            }
            case MAIN -> {
                return mainPlant;
            }
            case SHIELD -> {
                return shieldPlant;
            }
        }
        return null;
    }

    public Plant getPlant(){
        if (shieldPlant != null){
            return shieldPlant;
        }
        else if (mainPlant != null){
            return mainPlant;
        }
        else {
            return basePlant;
        }
    }

    public boolean isEmpty(){
        return basePlant == null && mainPlant == null && shieldPlant == null;
    }

    public void handlePlanting(PlantType type) {
        Plant newPlant = App.getFactory().createPlant(type, x, y);
        PlacementBehaviorComponent behavior = newPlant.getComponent(PlacementBehaviorComponent.class);

        PlantLayer layer = (behavior != null) ? behavior.getTargetLayer() : PlantLayer.MAIN;

        if (behavior != null) {
            if (behavior.isWaterOnly() && !isWater){
                return;
            }
            if (!behavior.isWaterOnly() && isWater){
                if (isLayerEmpty(PlantLayer.BASE)){
                    return;
                }
            }

            if (behavior.isStackable() && !isLayerEmpty(layer)) {
                Plant existingPlant = getPlant(layer);
                if (existingPlant.getType() == type) {
                    PlacementBehaviorComponent existingBehavior = existingPlant.getComponent(PlacementBehaviorComponent.class);
                    if (existingBehavior.tryIncrementStack()) {
                        ShooterComponent shooterComp = existingPlant.getComponent(ShooterComponent.class);
                        shooterComp.setBurstProjectileNumber(existingBehavior.getCurrentStack());
                        shooterComp.setBurstProjectileNumberOnPlantFood(existingBehavior.getCurrentStack());
                        shooterComp.setGiantCount(existingBehavior.getCurrentStack());
                        // update visuals...
                        return;
                    }
                }
            }
        } else {
            if (isWater && isLayerEmpty(PlantLayer.BASE)){
                return;
            }
        }

        if (isLayerEmpty(layer)) {
            setPlant(newPlant, layer);
            App.getCurrentGame().getActivePlants().add(newPlant);
        }
    }


    public void removePlant(){
        this.plant = null;
    }


}