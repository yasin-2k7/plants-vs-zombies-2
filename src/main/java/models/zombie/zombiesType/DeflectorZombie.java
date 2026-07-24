package models.zombie.zombiesType;

import controller.GameMenuController;
import models.core.App;
import models.enums.Zombies;
import models.projectile.Projectile;
import models.projectile.hitStrategies.HitStrategy;
import models.projectile.hitStrategies.PlantDamageStrategy;
import models.projectile.movementStrategies.StraightMovementStrategy;
import models.projectile.strikeStrategies.CheckPlantStrike;
import models.projectile.strikeStrategies.CheckStraightStrike;
import models.projectile.strikeStrategies.CheckStrike;
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
            boolean approaching = false;
            models.world.GameWorld game = App.getCurrentGame();

            if (game != null) {
                for (Projectile p : game.getActiveProjectiles()) {
                    if (p.getType() != null && "STRAIGHT".equals(p.getType().movement) && !(p.getHitStrategy() instanceof PlantDamageStrategy)) {
                        if (Math.abs(p.getY() - this.y) < 50 && p.getX() < this.x && p.getX() > this.x - 250) {
                            approaching = true;
                            break;
                        }
                    }
                }
            }

            if (approaching) {
                if (!isSpinning) {
                    startSpinning();
                } else {
                    spinTicks = 30;
                }
            }

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

        super.update();
    }

    public boolean tryDeflect(Projectile projectile) {
        if (isDead) return false;

        if (isJuggler) {
            if ("STRAIGHT".equals(projectile.getType().movement)) {
                if (!isSpinning) {
                    startSpinning();
                } else {
                    spinTicks = 30;
                }
                deflectProjectile(projectile);
                return true;
            }
        } else {
            if ("LOBBED".equals(projectile.getType().movement)) {
                System.out.println("Parasol deflected a lobbed projectile!");
                return true;
            }
        }
        return false;
    }

    private void startSpinning() {
        isSpinning = true;
        spinTicks = 30;
        System.out.println("Juggler starts spinning!");
        spinTicks = 30;
        GameMenuController.updateState("Juggler starts spinning!");
    }

    private void stopSpinning() {
        isSpinning = false;
        if (!isSlowed()) {
            this.speed = originalSpeed;
        }
        System.out.println("Juggler stops spinning.");
        this.speed = originalSpeed;
        GameMenuController.updateState("Juggler stops spinning.");
    }

    private void deflectProjectile(Projectile original) {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        Projectile deflected = game.getProjectilesPool().acquire();
        StraightMovementStrategy movement = new StraightMovementStrategy(-1f * 5, 0, 0);

        String element = "NORMAL";
        int dmg = 20;
        if (original.getHitStrategy() != null) {
            element = original.getHitStrategy().getElement();
            dmg = original.getHitStrategy().getDamage();
        }

        HitStrategy hitStrategy = new PlantDamageStrategy(dmg, element);
        CheckStrike strike = new CheckPlantStrike();

        deflected.reset(original.getX(), original.getY(), hitStrategy, movement, strike, original.getType());
        deflected.setPlantType(original.getPlantType());

        game.getActiveProjectiles().add(deflected);
        GameMenuController.updateState("Juggler deflected a projectile back to plants!");
    }
}