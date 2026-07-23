package models.pool;

import models.enums.ProjectileType;
import models.plant.components.SunProducerComponent;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.strikeStrategies.CheckStrike;

public interface Resettable {
    void reset(float x, float y, int size, SunProducerComponent component);
    void reset(float x, float y);
    void reset(float x, float y, HitStrategy hitStrategy, MovementStrategy movementStrategy, CheckStrike checkStrike, ProjectileType type);
}
