package models.world;

import controller.GameMenuController;
import models.core.App;
import models.enums.ProjectileType;
import models.plant.GameComponent;
import models.plant.components.SunProducerComponent;
import models.plant.visions.VisionStrategy;
import models.pool.Resettable;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStrike;

public class Sun implements Resettable {
    private float x, y;
    private float finalX, finalY;
    private int spawnTime;
    private int amount;
    private boolean isCollected;
    private SunProducerComponent producer;
    private int size;
    private GameWorld game;
    private SunType type;

    public int getAmount() {
        return amount;
    }

    public boolean isExpired(){
        if (y != finalY){
            y += 10;
            if (y >= finalY){
                if (type == SunType.RADIOACTIVE){
                    type = SunType.NORMAL;
                }
                y = finalY;
                GameMenuController.updateState("Sun reached the ground at position (" + finalX + ", " + finalY + ")");
            }
            else{
                GameMenuController.updateState("Dropping sun position (" + x + ", " + y + ")");
            }
        }

        int elapsed = game.getCurrentTick() - spawnTime;
        return elapsed > 100; //ms
    }

    public void collect(){
        isCollected = true;
    }

    public void setup(int row, int col, SunType type){
        this.finalX = col * App.getCellWidth() + App.getCellWidth()/2;
        this.finalY = row * App.getCellHeight() + App.getCellHeight()/2;
        this.x = finalX;
        this.y = 0;
        this.type = type;
        this.size = type.amount;
        this.spawnTime = game.getCurrentTick();
        this.isCollected = false;
        this.producer = null;
        GameMenuController.updateState("New " + type + " sun is dropping at position (" + finalX + ", " + finalY + ")");
    }



    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {
        this.x = x;
        this.y = y;
        finalX = x;
        finalY = y;
        this.size = size;
        this.producer = component;
        this.game = App.getCurrentGame();
        this.type = SunType.NORMAL;
        this.isCollected = false;
        System.out.println(this);
    }

    @Override
    public void reset(float x, float y) {
        this.isCollected = false;
    }

    @Override
    public void reset(float x, float y, HitStrategy hitStrategy, MovementStrategy movementStrategy, CheckStrike checkStrike, ProjectileType type) {
        this.isCollected = false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public SunProducerComponent getProducer() {
        return producer;
    }

    public SunType getType() {
        return type;
    }
}
