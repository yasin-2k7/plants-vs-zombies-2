package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.world.GameWorld;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;

public class SpawnerZombie extends Zombie {
    private boolean isGargantuar;
    private int spawnCooldown;
    private int currentCooldown = 50;
    private boolean hasThrownImp;

    public SpawnerZombie(int health, double speed, int damage, boolean isGargantuar) {
        super(Zombies.SPAWNER, health, speed, damage);
        this.isGargantuar = isGargantuar;
        this.spawnCooldown = 50; // هر ۵۰ تیک یک بار (برای پادشاه)
        this.currentCooldown = spawnCooldown;
        this.hasThrownImp = false;

        if (!isGargantuar) {
            // پادشاه: سرعت را صفر می‌کنیم تا حرکت نکند
            this.speed = 0;
            this.originalSpeed = 0;
        }
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update(); // حرکت و خوردن (پادشاه سرعت ۰ دارد)

        if (isGargantuar) {
            // اگر به نصف جان رسید و هنوز ایمپ پرتاب نشده
            if (!hasThrownImp && this.health <= this.maxHealth / 2) {
                throwImp();
                hasThrownImp = true;
            }
        } else {
            // پادشاه: هر چند ثانیه یک زامبی ساده را شوالیه می‌کند
            if (currentCooldown <= 0) {
                knightNearbyZombie();
                currentCooldown = spawnCooldown;
            } else {
                currentCooldown--;
            }
        }
    }

    private void throwImp() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        // ایجاد ایمپ (غیر اژدها)
        ImpZombie imp = (ImpZombie) new ZombieFactory().createZombie("ZombieImp");
        // ستون سوم از چپ = ایندکس ۲
        float targetX = 2 * App.getCellWidth() + App.getCellWidth() / 2;
        float targetY = this.y;
        imp.throwImp(targetX, targetY);
        game.getActiveZombies().add(imp);
        System.out.println("Gargantuar threw an Imp to column 3 at (" + targetX + ", " + targetY + ")");
    }

    private void knightNearbyZombie() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        for (Zombie z : game.getActiveZombies()) {
            // ۱. خود پادشاه را نادیده بگیر
            if (z == this) continue;

            // ۲. فقط زامبی‌های ساده (بدون زره و با نام ZombieDefault) را بپذیر
            boolean isBasic = z.getName() == Zombies.ZOMBIE &&
                    !(z instanceof ArmoredZombie) &&
                    Math.abs(z.getY() - this.y) < 10;

            if (isBasic) {
                // تبدیل به شوالیه
                ArmoredZombie knight = new ArmoredZombie(
                        z.getHealth(),
                        z.getSpeed(),
                        z.getDamage(),
                        1600, // armorHealth
                        true  // isMagnetic
                );
                knight.setX(z.getX());
                knight.setY(z.getY());
                knight.setSpecificName("ZombieDarkArmor3");

                // جایگزینی در لیست
                game.getActiveZombies().remove(z);
                game.getActiveZombies().add(knight);
                System.out.println("King turned a zombie into a knight at (" + knight.getX() + ", " + knight.getY() + ")");
                break;
            }
        }
    }

    @Override
    public void move() {
        if (!isGargantuar) {
            // پادشاه حرکت نمی‌کند
            return;
        }
        super.move();
    }
}