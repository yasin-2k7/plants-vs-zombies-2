package models.zombie.zombiesType;

import models.zombie.Zombie;

public class DeflectorZombie extends Zombie {
    public DeflectorZombie(int health, int speed, int damage) {
        super("Deflector Zombie", health, speed, damage);
    }

    // بازنویسی متد دریافت آسیب برای هندل کردن دفع پرتابه‌ها
    public void takeDamage(int amount, boolean isLobberAttack, String projectileType) {
        // اگر حمله قوسی نباشد و پرتابه فیزیکی باشد، آن را دفع می‌کند
        if (!isLobberAttack && isDeflectable(projectileType)) {
            deflectProjectile(projectileType);
        } else {
            // دریافت آسیب در صورت قوسی بودن حمله (مثل هندوانه یا کلم)
            super.health -= amount;
            if (super.health <= 0) {
                die();
            }
        }
    }

    private boolean isDeflectable(String projectileType) {
        // بررسی نوع پرتابه (مثلا لیزر یا پلاسما دفع نمی‌شود)
        return projectileType.equals("Pea") || projectileType.equals("Cabbage");
    }

    private void deflectProjectile(String projectileType) {
        // لاجیک برگرداندن پرتابه به سمت گیاهان
    }
}
