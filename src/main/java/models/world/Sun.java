package models.world;

import models.core.App;
import models.plant.GameComponent;
import models.plant.components.SunProducerComponent;
import models.pool.Resettable;

public class Sun implements Resettable {
    private float x, y;
    private float finalX, finalY;
    private float timeRemaining;
    private SunProducerComponent producer;
    private int size;
    private GameWorld game;


    @Override
    public void reset(float x, float y, int size, SunProducerComponent component) {
        this.finalX = x;
        this.finalY = y;
        this.size = size;
        this.producer = component;
        this.game = App.getCurrentGame();
    }

    public void Click(){
        game.getActiveSuns().remove(this);
        game.setSun(game.getSun() + size);
        producer.getComponentSuns().remove(this);
        game.getSunsPool().release(this);
    }
}
