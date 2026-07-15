package models.zombie.zombiesType;

import models.enums.Zombies;
import models.zombie.Zombie;

public class DeflectorZombie extends Zombie {
    private boolean isJuggler;

    public DeflectorZombie(int health, double speed, int damage, boolean isJuggler) {
        super(Zombies.DEFLECTOR, health, speed, damage);
        this.isJuggler = isJuggler;

    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        boolean isLobber = "LOBBER".equals(damageType);
        if (isJuggler) {
            if (!isLobber && isDeflectable(damageType)) {
                deflectProjectile(damageType);
                return; // آسیب نمی‌بیند
            }
        } else { // چتردار
            if (isLobber) {
                // دفع می‌کند (آسیب نمی‌بیند)
                return;
            }
        }
        super.takeDamage(amount, damageType);
    }

    private boolean isDeflectable(String projectileType) {
        // لیست پرتابه‌های قابل بازتاب
        return projectileType.equals("PEA") || projectileType.equals("CABBAGE") ||
                projectileType.equals("MELON") || projectileType.equals("BUTTER");
    }

    private void deflectProjectile(String projectileType) {
        // در این نقطه، شی پرتابه جدیدی به سمت چپ (گیاهان) شلیک می‌شود
        // Projectile reversed = new DirectlyProjectile();
        // reversed.setSpeed(-currentSpeed);
    }
}
