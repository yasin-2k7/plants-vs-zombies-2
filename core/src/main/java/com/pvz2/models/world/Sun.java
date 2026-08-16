package com.pvz2.models.world;

import com.badlogic.gdx.math.Rectangle;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.plant.components.SunProducerComponent;
import com.pvz2.models.pool.Resettable;
import com.pvz2.view.SunGraphic;

public class Sun implements Resettable {
    private float x, y;
    private float finalX, finalY;
    private float fallSpeed = 200f;
    private float groundedTimer = 0f;
    private static final float DESPAWN_TIME = 10f;

    private boolean isCollected;
    private boolean isExploded;
    private boolean isExpired;
    private SunProducerComponent producer;
    private int size;
    private GameWorld game;
    private SunType type;
    private float animTime = 0f;

    public void setup(int row, int col, SunType type) {
        if (game == null) {
            game = App.getCurrentGame();
        }
        this.finalX = App.getFirstCellX() + col * App.getCellWidth() + App.getCellWidth() / 2f;
        this.finalY = App.getFirstCellY() + row * App.getCellHeight() + App.getCellHeight() / 2f;

        this.x = finalX;
        this.y = 1050f;

        this.type = type;
        this.size = type.amount;
        this.isCollected = false;
        this.isExploded = false;
        this.isExpired = false;
        this.groundedTimer = 0f;
        this.animTime = 0f;
        this.producer = null;

        GameMenuController.updateState("New " + type + " sun dropping at (" + finalX + ", " + finalY + ")");
    }

    public void update(float delta) {
        if (isCollected || isExploded || isExpired) return;

        animTime += delta;

        if (y > finalY) {
            y -= fallSpeed * delta;
            if (y <= finalY) {
                y = finalY;
                onLand();
            }
        } else {
            groundedTimer += delta;
            if (groundedTimer >= DESPAWN_TIME) {
                isExpired = true;
            }
        }
    }

    private void onLand() {
        GameMenuController.updateState("Sun landed at (" + finalX + ", " + finalY + ")");
        if (type == SunType.RADIOACTIVE) {
            explode();
        }
    }

    public void explode() {
        if (isExploded || isExpired) return;
        this.isExploded = true;
        GameMenuController.updateState("Radioactive sun exploded at (" + x + ", " + y + ")");

        Cell[][] grid = App.getCurrentGame().getGrid();
        Cell sunCell = Cell.findCell(x, y, grid);
        if (sunCell != null) {
            var zombieCells = Cell.getNeighborCells(sunCell, grid, 2);
            var zombies = Cell.getZombiesInCells(zombieCells);
            for (var zombie : zombies) {
                zombie.takeDamage(150, "NORMAL");
            }

            var plantCells = Cell.getNeighborCells(sunCell, grid, 1);
            for (Cell cell : plantCells) {
                if (cell.getPlant(com.pvz2.models.enums.PlantLayer.BASE) != null)
                    cell.getPlant(com.pvz2.models.enums.PlantLayer.BASE).takeDamage(80);
                if (cell.getPlant(com.pvz2.models.enums.PlantLayer.MAIN) != null)
                    cell.getPlant(com.pvz2.models.enums.PlantLayer.MAIN).takeDamage(80);
                if (cell.getPlant(com.pvz2.models.enums.PlantLayer.SHIELD) != null)
                    cell.getPlant(com.pvz2.models.enums.PlantLayer.SHIELD).takeDamage(80);
            }
        }
    }

    public void collect() {
        if (type == SunType.RADIOACTIVE) {
            explode();
            return;
        }
        this.isCollected = true;
        this.isExpired = true;
    }

    public boolean isExpired() { return isExpired || isCollected || isExploded; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getSize() { return size; }
    public boolean isCollected() { return isCollected; }
    public boolean isExploded() { return isExploded; }
    public void setExpired(boolean expired) { this.isExpired = expired; }
    public SunProducerComponent getProducer() { return producer; }
    public SunType getType() { return type; }
    public float getAnimTime() { return animTime; }

    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {
        this.x = x;
        this.y = y;
        this.finalX = x;
        this.finalY = y;
        this.size = size;
        this.producer = component;
        this.game = App.getCurrentGame();
        this.type = SunType.NORMAL;
        this.isCollected = false;
        this.isExploded = false;
        this.isExpired = false;
        this.groundedTimer = 0f;
    }

    @Override
    public void reset(float x, float y) {
        this.isCollected = false;
        this.isExploded = false;
        this.isExpired = false;
    }

    @Override
    public void reset(float x, float y, com.pvz2.models.projectile.hitStrategies.HitStrategy hitStrategy,
                      com.pvz2.models.projectile.movementStrategies.MovementStrategy movementStrategy,
                      com.pvz2.models.projectile.strikeStrategies.CheckStrike checkStrike,
                      com.pvz2.models.enums.ProjectileType type) {
        this.isCollected = false;
        this.isExploded = false;
        this.isExpired = false;
    }

    public Rectangle getBounds() {
        float size = 80f * SunGraphic.getScale(type);
        return new Rectangle(x - size / 2f, y - size / 2f, size, size);
    }
}
