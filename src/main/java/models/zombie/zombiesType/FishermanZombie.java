package models.zombie.zombiesType;

import models.zombie.Zombie;

public class FishermanZombie extends Zombie {
    private int hookCooldown; // زمان انتظار بین هر بار قلاب انداختن

    public FishermanZombie(String name, int health, int damage) {
        // سرعت  0 در نظر گرفته می‌شود تا حرکت نکند
        super(name, health, 0, damage);
        this.hookCooldown = 3;
    }

    @Override
    public void move() {
        // ماهیگیر حرکت نمی‌کند
    }

    // متد قلاب انداختن با بررسی شرایط داک
    public void hookPlant(String targetPlantName, boolean isNextToZombie, boolean isRightTileEmpty) {
        if (hookCooldown > 0) {
            hookCooldown--;
            return;
        }

        // اگر گیاه در کنار زامبی باشد، آن را پرتاب کرده و نابود می‌کند
        if (isNextToZombie) {
            // منطق نابودی گیاه باید اینجا اجرا شود
        }
        // اگر کنارش نباشد و خانه سمت راست گیاه خالی باشد، آن را یک خانه به جلو (سمت زامبی) می‌کشد
        else if (isRightTileEmpty) {
            // منطق جابجایی مکان گیاه باید اینجا اجرا شود
        } else {
        }

        // ریست کردن زمان استراحت قلاب
        hookCooldown = 3;
    }

    @Override
    public void takeDamage(int damageAmount, String damageType) {

    }

    @Override
    public void die() {
    }
}
