package models.miniGame.bowling;

import models.core.App;
import models.enums.ProjectileType;
import models.projectile.Projectile;
import models.projectile.hitStrategies.CombinedDamageStrategy;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.movementStrategies.BowlingMovementStrategy;
import models.projectile.movementStrategies.MovementStrategy;
import models.projectile.movementStrategies.StraightMovementStrategy;
import models.projectile.strikeStrategies.CheckStraightStrike;
import models.zombie.ZombieRegistry;

public class BowlingBallFactory {

    private static int normalZombieHealth() {
        return ZombieRegistry.getZombieProperties("ZombieDefault").getObjdata().getHitpoints();
    }

    public static Projectile create(BowlingBallType type, float x, float y) {
        Projectile ball = App.getCurrentGame().getProjectilesPool().acquire();

        HitStrategy hitStrategy = createHitStrategy(type);
        MovementStrategy movementStrategy = createMovementStrategy(type);

        ball.reset(x, y, hitStrategy, movementStrategy, new CheckStraightStrike(), ProjectileType.BOWLING_STRAIGHT);
        ball.setPierce(getPierce(type));

        return ball;
    }

    private static HitStrategy createHitStrategy(BowlingBallType type) {
        int normalHealth = normalZombieHealth();
        return switch (type) {
            case NORMAL -> new CombinedDamageStrategy(normalHealth, ProjectileType.BOWLING_STRAIGHT);
            case EXPLODE_O_NUT -> new CombinedDamageStrategy(
                    normalHealth,
                    normalHealth,
                    App.getCellWidth() * 1.5f,
                    ProjectileType.BOWLING_STRAIGHT
            );
            case GIANT_WALLNUT -> new CombinedDamageStrategy(normalHealth * 2, ProjectileType.BOWLING_STRAIGHT);
        };
    }

    private static MovementStrategy createMovementStrategy(BowlingBallType type) {
        return switch (type) {
            case NORMAL -> new BowlingMovementStrategy(5f, 0f);
            case EXPLODE_O_NUT -> new StraightMovementStrategy(5f, 0f, 0f);
            case GIANT_WALLNUT -> new StraightMovementStrategy(5f, 0f, 0f);
        };
    }

    private static int getPierce(BowlingBallType type) {
        return switch (type) {
            case NORMAL -> 100;
            case EXPLODE_O_NUT -> 1;
            case GIANT_WALLNUT -> 100;
        };
    }
}