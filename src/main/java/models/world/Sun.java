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
        long elapsed = System.currentTimeMillis() - spawnTime;
        return elapsed > 7000; //ms
    }

    public void collect(){
        isCollected = true;
    }

    public void setup(int row, int col, int amount, SunType type){
        this.x = row;
        this.y = col;
        this.amount = amount;
        this.type = type;
        this.spawnTime = System.currentTimeMillis();
        this.isCollected = false;
    }



    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {
        this.finalX = x;
        this.finalY = y;
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
