package models.world;

import models.plant.components.SunProducerComponent;
import models.pool.Resettable;

public class Sun implements Resettable {
    private float x, y;
    private float finalX, finalY;
    private float timeRemaining;
    private SunProducerComponent producer;
    private int size;
}
