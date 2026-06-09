package models.zombie.zombiesType;

import models.zombie.Zombie;

public class RangedZombie extends Zombie {
    private String projectileType;

    public RangedZombie(int health, int speed, int damage, String projectileType) {
        super("Ranged Zombie", health, speed, damage);
        this.projectileType = projectileType;
    }

    // متد پرتاب بر اساس نوع
    public void throwProjectile() {
        switch (this.projectileType) {
            case "OCTOPUS":
                System.out.println("Throwing an Octopus!");
                // TODO: ساخت شیء اختاپوس و قفل کردن یک گیاه در لاین
                break;

            case "ICE_BLOCK":
                System.out.println("Throwing Ice Wind/Block!");
                // TODO: اعمال افکت یخ‌زدگی و توقف عملکرد گیاه
                break;

            default:
                System.out.println("Unknown projectile.");
                break;
        }
    }

    @Override
    public void update() {
        super.update();

        // TODO: منطق چک کردن فاصله تا گیاه برای شلیک
        // if (isPlantInRange && cooldownReady) {
        //     throwProjectile();
        // }
    }
}
