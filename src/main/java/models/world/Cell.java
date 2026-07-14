package models.world;

import models.core.App;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.components.PlacementBehaviorComponent;
import models.plant.components.ShooterComponent;
import models.world.obstacles.Obstacle;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Cell {
    private int row;
    private int col;
    private int x;
    private int y;
    private Plant basePlant;
    private Plant mainPlant;
    private Plant shieldPlant;
    private Obstacle obstacle;

    private boolean plantable = true;



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

    public static List<Cell> getNeighborCells(Cell inputCell, Cell[][] grid, int radius) {
        List<Cell> neighbors = new ArrayList<>();

        int centerCol = inputCell.getCol();
        int centerLane = inputCell.getRow();

        int totalLanes = grid.length;
        int totalCols = grid[0].length;

        int minLane = Math.max(0, centerLane - radius);
        int maxLane = Math.min(totalLanes - 1, centerLane + radius);

        int minCol = Math.max(0, centerCol - radius);
        int maxCol = Math.min(totalCols - 1, centerCol + radius);

        for (int l = minLane; l <= maxLane; l++) {
            neighbors.addAll(Arrays.asList(grid[l]).subList(minCol, maxCol + 1));
        }

        return neighbors;
    }

    public static List<Cell> getCellsInRow(Cell inputCell, Cell[][] grid){
        return new ArrayList<>(Arrays.asList(grid[inputCell.getRow()]).subList(0, grid[0].length));
    }

    public static List<Zombie> getZombiesInCells(List<Cell> affectedCells) {
        List<Zombie> activeZombies = App.getCurrentGame().getActiveZombies();

        return activeZombies.stream()
                .filter(zombie -> affectedCells.stream().anyMatch(cell ->
                        zombie.getY() == cell.getY() &&
                                cell.containsX(zombie.getX())
                ))
                .toList();
    }



    public void removePlant(){
        this.mainPlant = null;
    }

    public boolean containsX(float x){
        return (x >= this.x - App.getCellWidth()/2 && x <= this.x + App.getCellWidth()/2);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasObstacle() { return obstacle != null; }
    public Obstacle getObstacle() { return obstacle; }
    public void setObstacle(Obstacle obstacle) { this.obstacle = obstacle; }
    public void removeObstacle() { this.obstacle = null; }

    public boolean isPlantable() {
        return plantable;
    }

    public void setPlantable(boolean plantable) {
        this.plantable = plantable;
    }
}