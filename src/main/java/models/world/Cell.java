package models.world;

import models.core.App;
import models.core.User;
import models.enums.PlantFamily;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.components.PlacementBehaviorComponent;
import models.plant.components.ShooterComponent;
import models.plant.factory.PlantFactory;
import models.quest.QuestStats;
import models.world.cellTerrains.CellTerrain;
import models.world.obstacles.Grave;
import models.world.obstacles.Obstacle;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class Cell {
    private int row;
    private int col;
    private float x;
    private float y;
    private Plant basePlant;
    private Plant mainPlant;
    private Plant shieldPlant;
    private Obstacle obstacle;
    private CellTerrain terrain;
    private boolean lowLyingCoast;
    private int slippingDir = 0;

    private boolean plantable = true;
    private boolean necromancyPotential = false;
    private boolean necromancyTriggered = false;


    public Cell(int row, int col, CellTerrain initialTerrain) {
        this.row = row;
        this.col = col;
        x = (col) * App.getCellWidth() + App.getCellWidth()/2;
        y = (row) * App.getCellHeight() + App.getCellHeight()/2;
        this.terrain = initialTerrain;
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

    public String handlePlanting(PlantType type, boolean boost){
        if (!this.isPlantable()) {
            if (!(this.obstacle instanceof Grave && type == PlantType.GRAVE_BUSTER)){
                return "you cannot plant in that place!";
            }
        }

        Plant newPlant = PlantFactory.createPlant(type, (int)x, (int)y, this);
        if (boost) newPlant.setPlantFoodInStart(true);

        if (!((this.obstacle instanceof Grave && type == PlantType.GRAVE_BUSTER))
           || !(this.terrain.canPlant(newPlant, this))
           || (this.hasIcyZombie())){
            return "you cannot plant in that place!";
        }

        PlacementBehaviorComponent behavior = newPlant.getComponent(PlacementBehaviorComponent.class);
        PlantLayer layer = (behavior != null) ? behavior.getTargetLayer() : PlantLayer.MAIN;



        if (behavior != null && behavior.isStackable() && !isLayerEmpty(layer)) {
            Plant existingPlant = getPlant(layer);
            if (existingPlant.getType() == type) {
                PlacementBehaviorComponent existingBehavior = existingPlant.getComponent(PlacementBehaviorComponent.class);
                if (existingBehavior.tryIncrementStack()) {
                    ShooterComponent shooterComp = existingPlant.getComponent(ShooterComponent.class);
                    shooterComp.setBurstProjectileNumber(existingBehavior.getCurrentStack());
                    shooterComp.setBurstProjectileNumberOnPlantFood(existingBehavior.getCurrentStack());
                    shooterComp.setGiantCount(existingBehavior.getCurrentStack());
                    // update visuals...
                    return null;
                }
            }
        }

        if (isLayerEmpty(layer)) {
            setPlant(newPlant, layer);
            App.getCurrentGame().getActivePlants().add(newPlant);

            User user = App.getCurrentUser();
            if (user != null) {
                QuestStats stats = user.getQuestStats();
                // برای کوئست ۱۱
                stats.addFamilyUsedInLevel(newPlant.getType().family);
                // برای کوئست ۱۲
                stats.incrementTotalPlantsUsed();
                if (Plant.isMushroom(newPlant.getType())) {
                    stats.incrementMushroomPlantsUsed();
                }
                // برای کوئست ۸ (انفجاری)
                if (newPlant.getType().family == PlantFamily.EXPLOSIVE) {
                    stats.incrementExplosivePlantsUsed();
                }
                if (newPlant.getType().family == PlantFamily.SUN_PRODUCER) {
                    stats.incrementSunProducerPlantsInLevel();
                }
                user.getQuestManager().checkAllQuests(user);
            }
            return null;
        }
        return "that place isn't empty!";
    }

    public String handlePlanting(PlantType type) {
        return handlePlanting(type, false);
    }

    public static Cell findCell(float x, float y, Cell[][] grid){
        for (Cell[] cellRows : grid){
            for (Cell cell : cellRows){
                if (cell.containsX(x) && cell.containsY(y)){
                    return cell;
                }
            }
        }
        return null;

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

    public static List<Zombie> getZombiesInCell(Cell affectedCell) {
        List<Zombie> activeZombies = App.getCurrentGame().getActiveZombies();

        return activeZombies.stream()
                .filter(zombie ->
                        zombie.getY() == affectedCell.getY() &&
                                affectedCell.containsX(zombie.getX())
                )
                .toList();
    }

    public static Cell findZombieCell(Cell[][] grid, Zombie zombie){
        for (Cell[] cellRows : grid){
            for (Cell cell : cellRows){
                if (cell.containsX(zombie.getX()) && cell.getY() == zombie.getY()){
                    return cell;
                }
            }
        }
        return null;
    }

    public static Cell nextCell(Cell origin, Cell[][] grid){
        int row = origin.getRow();
        if (origin.getCol() >= grid[0].length - 1) {
            return null;
        }
        return grid[row][origin.getCol() + 1];
    }

    public static Cell previousCell(Cell origin, Cell[][] grid){
        int row = origin.getRow();
        if (origin.getCol() <= 0) {
            return null;
        }
        return grid[row][origin.getCol() - 1];
    }



    public void removePlant(){
        this.mainPlant = null;
    }

    public Plant findPlant(){
        if (this.shieldPlant != null) {
            return this.shieldPlant;
        }
        if (this.mainPlant != null) {
            return this.mainPlant;
        }
        if (this.basePlant != null) {
            return this.basePlant;
        }
        return null;
    }

    public boolean findAndRemovePlant(){
        if (this.shieldPlant != null) {
            this.shieldPlant.die();
            this.shieldPlant = null;
            return true;
        }
        if (this.mainPlant != null) {
            this.mainPlant.die();
            this.mainPlant = null;
            return true;
        }
        if (this.basePlant != null) {
            this.basePlant.die();
            this.basePlant = null;
            return true;
        }
        return false;
    }

    public boolean containsX(float x){
        return (x > this.x - App.getCellWidth()/2 && x <= this.x + App.getCellWidth()/2);
    }

    public boolean containsY(float y){
        return (y > this.y - App.getCellHeight()/2 && y <= this.y + App.getCellHeight()/2);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean hasObstacle() { return obstacle != null; }
    public Obstacle getObstacle() { return obstacle; }
    public void setObstacle(Obstacle obstacle) { this.obstacle = obstacle; }
    public void removeObstacle() { this.obstacle = null; }

    public void setTerrain(CellTerrain terrain) {
        this.terrain = terrain;
    }

    public boolean canPlant(Plant plant) {
        return this.plantable && terrain.canPlant(plant, this);
    }

    public boolean isWater() {
        return terrain.isWater();
    }

    public boolean isPlantable() {
        return plantable;
    }

    public void setPlantable(boolean plantable) {
        this.plantable = plantable;
    }

    public CellTerrain getTerrain() {
        return terrain;
    }

    public boolean blocksProjectile() {
        return hasObstacle() && obstacle.isDestroyed();
    }

    public boolean isNecromancyPotential() { return necromancyPotential; }
    public void setNecromancyPotential(boolean value) { this.necromancyPotential = value; }
    public boolean isNecromancyTriggered() { return necromancyTriggered; }
    public void setNecromancyTriggered(boolean value) { this.necromancyTriggered = value; }


    public boolean isLowLyingCoast() {
        return lowLyingCoast;
    }

    public void setLowLyingCoast(boolean lowLyingCoast) {
        this.lowLyingCoast = lowLyingCoast;
    }

    public int getSlippingDir() {
        return slippingDir;
    }

    public boolean hasIcyZombie(){
        for (Zombie zombie: Cell.getZombiesInCell(this)){
            if (zombie.getIceHealth() > 0) return true;
        }
        return false;
    }

    public void setSlippingDir(int slippingDir) {
        this.slippingDir = slippingDir;
    }
}