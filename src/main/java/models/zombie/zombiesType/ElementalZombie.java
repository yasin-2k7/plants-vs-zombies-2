package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.zombie.Zombie;

import java.util.Timer;

public class ElementalZombie extends Zombie {
    private boolean isExplorer;
    private boolean isIgnited;  // مشعل روشن / دینامیت فعال
    private int fuseTimer; // برای پروسپکتور (تعداد تیک تا انفجار)

    public ElementalZombie(int health, double speed, int damage, boolean isProspector) {
        super(Zombies.ELEMENTAL, health, speed, damage);
        this.isExplorer = isExplorer;
        this.isIgnited = true;
        this.fuseTimer = 100;
    }

    @Override
    public void update() {
        if (isDead) return;
        if (!isExplorer && isIgnited) {
            fuseTimer--;
            if (fuseTimer <= 0) {
                // انفجار: پرتاب به انتهای سطر (سمت چپ)
                // این منطق باید توسط GameWorld مدیریت شود
                // پس از انفجار، زامبی همچنان زنده است اما به سمت راست حرکت می‌کند
                this.isIgnited = false;
                this.speed = -this.speed; // حرکت به راست
            }
        }
        super.update();
    }

    // خاموش کردن آتش (تیر یخی)
    public void extinguish() {
        this.isIgnited = false;
    }

    // روشن کردن آتش (تیر آتشین)
    public void ignite() {
        this.isIgnited = true;
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        // اگر نوع آسیب یخی باشد و زامبی اکسپلورر باشد، مشعل خاموش می‌شود
        if ("ICE".equals(damageType) && isExplorer) {
            extinguish();
        } else if ("FIRE".equals(damageType) && isExplorer) {
            ignite();
        }
        super.takeDamage(amount, damageType);
    }

    public boolean isExplorer() { return isExplorer; }
    public boolean isIgnited() { return isIgnited; }
}
