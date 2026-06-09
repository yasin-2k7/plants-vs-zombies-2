package models.zombie.zombiesType;

import models.zombie.Zombie;

import java.util.Timer;

public class ElementalZombie extends Zombie {
    private boolean isIgnited = true;
    private Timer dynamicTimer; // برای زامبی اکتشافگر
    private boolean isProspector;

    public ElementalZombie(int health, int speed, int damage, boolean isProspector) {
        super("Elemental Zombie", health, speed, damage);
        this.isProspector = isProspector;
    }

    public void applyIceEffect() {
        this.isIgnited = false; // خاموش شدن مشعل یا دینامیت با تیر یخی
    }

    public void applyFireEffect() {
        this.isIgnited = true; // روشن شدن مجدد مشعل
    }

    @Override
    public void update() {
        if (isIgnited && isProspector) {
            // TODO: بعد از ۱۰ ثانیه پرتاب به انتهای سطر
        }
    }
}
