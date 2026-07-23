package models.zombie.zombiesType;

import controller.GameMenuController;
import models.core.App;
import models.enums.Zombies;
import models.projectile.Projectile;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.movementStrategies.StraightMovementStrategy;
import models.projectile.strikeStrategies.CheckStraightStrike;
import models.world.GameWorld;
import models.zombie.Zombie;

public class DeflectorZombie extends Zombie {
    private final boolean isJuggler;
    private boolean isSpinning = false;
    private int spinTicks = 0;
    private double originalSpeed;
    private static final double SPIN_SPEED_MULTIPLIER = 1.8;  // افزایش سرعت در حالت چرخش

    public DeflectorZombie(int health, double speed, int damage, boolean isJuggler) {
        super(Zombies.DEFLECTOR, health, speed, damage);
        this.isJuggler = isJuggler;
        this.originalSpeed = this.speed;
    }

    public boolean isJuggler() {
        return isJuggler;
    }

    @Override
    public void update() {
        if (isDead) return;

        if (isJuggler) {
            if (isSpinning) {
                spinTicks--;
                if (spinTicks <= 0) {
                    stopSpinning();
                }
                this.speed = originalSpeed * SPIN_SPEED_MULTIPLIER;
            } else {
                this.speed = originalSpeed;
            }
        }

        super.update();  // حرکت و خوردن معمولی
    }

    public boolean tryDeflect(Projectile projectile) {
        if (!isJuggler || isDead) return false;
        if (!isSpinning) {
            startSpinning();
        }
        // فقط پرتابه‌های مستقیم (STRAIGHT) را بازتاب می‌دهیم
        if (projectile.getType().movement.equals("STRAIGHT")) {
            deflectProjectile(projectile);
            return true;
        }
        return false;
    }

    private void startSpinning() {
        isSpinning = true;
        spinTicks = 30;  // حداقل ۳۰ تیک می‌چرخد، با هر پرتابه جدید دوباره reset می‌شود
        GameMenuController.updateState("Juggler starts spinning!");
    }

    private void stopSpinning() {
        isSpinning = false;
        this.speed = originalSpeed;
        GameMenuController.updateState("Juggler stops spinning.");
    }

    private void deflectProjectile(Projectile original) {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        // ایجاد پرتابه جدید با همان مشخصات ولی در جهت مخالف (به سمت چپ)
        Projectile deflected = game.getProjectilesPool().acquire();
        StraightMovementStrategy movement = new StraightMovementStrategy(
                -1f * 5, 0, 0);  // سرعت منفی = حرکت به چپ
        HitStrategy hitStrategy = original.getHitStrategy();
        CheckStraightStrike strike = new CheckStraightStrike();
        deflected.reset(original.getX(), original.getY(), hitStrategy, movement, strike, original.getType());
        deflected.setPlantType(original.getPlantType());
        game.getActiveProjectiles().add(deflected);
        GameMenuController.updateState("Juggler deflected a projectile back to plants!");
    }
}