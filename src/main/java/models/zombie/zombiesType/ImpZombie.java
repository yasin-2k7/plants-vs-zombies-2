package models.zombie.zombiesType;

import models.zombie.Zombie;

public class ImpZombie extends Zombie {
    private boolean isThrown; // آیا پرتاب شده است؟

    public ImpZombie(int health, int speed, int damage, boolean isThrown) {
        super("Imp", health, speed, damage);
        this.isThrown = isThrown;
    }

    // متد فرود آمدن در یک خانه خاص پس از پرتاب شدن
    public void land(int gridX, int gridY) {
        if (isThrown) {
            //System.out.println("Imp landed safely at grid (" + gridX + ", " + gridY + ") bypassing frontline defenses!");
            this.isThrown = false; // پس از فرود، مثل یک زامبی عادی رفتار می‌کند
            // آپدیت کردن موقعیت زامبی در زمین بازی
        }
    }

    // ایمپ‌ها معمولا قابلیت خاص دیگری ندارند و فقط سریع گاز می‌گیرند
    public void bite(Object plant) {
        // plant.takeDamage(damageAmount);
    }

}
