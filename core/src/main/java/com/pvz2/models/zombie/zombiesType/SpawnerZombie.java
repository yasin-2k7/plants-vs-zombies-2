package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.ZombieFactory;

public class SpawnerZombie extends Zombie {
    private boolean isGargantuar;
    private float spawnCooldown;
    private float currentCooldown;
    private boolean hasThrownImp;

    public SpawnerZombie(int health, double speed, int damage, boolean isGargantuar) {
        super(Zombies.SPAWNER, health, speed, damage);
        this.isGargantuar = isGargantuar;
        this.spawnCooldown = 5.0f;
        this.currentCooldown = spawnCooldown;
        this.hasThrownImp = false;

        if (!isGargantuar) {
            this.speed = 0;
            this.originalSpeed = 0;
        }
    }

    @Override
    public void update(float delta) {
        if (isDead) return;
        super.update(delta);

        if (isGargantuar) {
            if (!hasThrownImp && this.health <= this.maxHealth / 2) {
                throwImp();
                hasThrownImp = true;
            }
        } else {
            if (currentCooldown <= 0) {
                knightNearbyZombie();
                currentCooldown = spawnCooldown;
            } else {
                currentCooldown-= delta;
            }
        }
    }

    private void throwImp() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        ImpZombie imp = (ImpZombie) new ZombieFactory().createZombie("ZombieImp");
        float targetX = 2 * App.getCellWidth() + App.getCellWidth() / 2;
        float targetY = this.y;
        imp.throwImp(targetX, targetY);
        game.getActiveZombies().add(imp);
        GameMenuController.updateState("Gargantuar threw an Imp to column 3 at (" + targetX + ", " + targetY + ")");
    }

    private void knightNearbyZombie() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        for (Zombie z : game.getActiveZombies()) {
            if (z == this) continue;

            boolean isBasic = z.getName() == Zombies.ZOMBIE &&
                    !(z instanceof ArmoredZombie) &&
                    Math.abs(z.getY() - this.y) < 10;

            if (isBasic) {
                ArmoredZombie knight = new ArmoredZombie(
                    (int) z.getHealth(),
                        z.getSpeed(),
                        z.getDamage(),
                        1600, // armorHealth
                        true  // isMagnetic
                );
                knight.setX(z.getX());
                knight.setY(z.getY());
                knight.setSpecificName("ZombieDarkArmor3");

                game.getActiveZombies().remove(z);
                game.getActiveZombies().add(knight);
                GameMenuController.updateState(
                        "King turned a zombie into a knight at (" + knight.getX() + ", " + knight.getY() + ")");
                break;
            }
        }
    }

    @Override
    public void move(float delta) {
        if (!isGargantuar) {
            return;
        }
        super.move(delta);
    }
}
