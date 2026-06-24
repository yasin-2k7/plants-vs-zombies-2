package models.pool;

import models.plant.components.SunProducerComponent;
import models.projectile.strategy.HitStrategy;

public interface Resettable {
    void reset(float x, float y, int size, SunProducerComponent component);
    void reset(float x, float y);
    void reset(float x, float y, HitStrategy strategy);
}
