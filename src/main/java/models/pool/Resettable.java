package models.pool;

import models.plant.components.SunProducerComponent;

public interface Resettable {
    void reset(float x, float y, int size, SunProducerComponent component);
}
