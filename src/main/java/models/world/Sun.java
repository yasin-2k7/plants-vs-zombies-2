package models.world;

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
    private long spawnTime;
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
        long elapsed = game.getCurrentTick() - spawnTime;
        return elapsed > 10; //ms
    }

    public void collect(){
        isCollected = true;
    }

    public void setup(int row, int col, int amount, SunType type){
        this.x = col * App.getCellHeight() + App.getCellHeight()/2;
        this.y = row * App.getCellWidth() + App.getCellWidth()/2;
        this.size = amount;
        this.type = type;
        this.spawnTime = game.getCurrentTick();
        this.isCollected = false;
        this.producer = null;
    }



    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.producer = component;
        this.game = App.getCurrentGame();
    }

    @Override
    public void reset(float x, float y) {

    }

    @Override
    public void reset(float x, float y, HitStrategy hitStrategy, MovementStrategy movementStrategy, CheckStrike checkStrike, ProjectileType type) {

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

    public SunProducerComponent getProducer() {
        return producer;
    }

    public void Click(){
        game.getActiveSuns().remove(this);
        game.setSun(game.getSun() + size);
        producer.getComponentSuns().remove(this);
        game.getSunsPool().release(this);
    }
}
