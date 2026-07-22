package models.zombie.zombiesType;

import models.enums.Zombies;
import models.zombie.Zombie;
import models.zombie.state.EatingState;

public class PhasingZombie extends Zombie {
    private boolean isPhaseChanged;
    private boolean isNewspaper; // true: newspaper, false: all-star
    private int shieldHealth; // جان روزنامه
    private boolean hasKilledPlant; // برای آل‌استار

    public PhasingZombie(int health, double speed, int damage, int shieldHealth, boolean isNewspaper) {
        super(Zombies.PHASING, health, speed, damage);
        this.shieldHealth = shieldHealth;
        this.isNewspaper = isNewspaper;
        this.isPhaseChanged = false;
        this.hasKilledPlant = false;

        if (!isNewspaper) {
            // آل‌استار با سرعت بالا شروع می‌کند و آسیب کشنده دارد
            this.originalSpeed = speed * 2.5;
            this.speed = this.originalSpeed;
            this.damage = 9999;
        } else {
            // نیوزپیپر سرعت عادی دارد
            this.originalSpeed = speed;
            this.speed = this.originalSpeed;
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;

        if (!isPhaseChanged && shieldHealth > 0) {
            // نیوزپیپر: روزنامه آسیب می‌بیند
            shieldHealth -= amount;
            if (shieldHealth <= 0) {
                triggerPhaseChange();
            }
        } else {
            // آسیب به خود زامبی (یا آل‌استار که زره ندارد)
            super.takeDamage(amount, damageType);
        }
    }

    private void triggerPhaseChange() {
        this.isPhaseChanged = true;
        if (isNewspaper) {
            // نیوزپیپر عصبانی می‌شود
            this.speed = this.originalSpeed * 3.0;
            this.damage = (int)(this.damage * 2);
            System.out.println("Newspaper is angry! Speed and damage increased.");
        }
    }

    @Override
    public void update() {
        if (isDead) return;

        boolean wasEating = (this.currentState instanceof EatingState);

        super.update(); // حرکت و خوردن معمولی

        // مدیریت آل‌استار: پس از کشتن گیاه، کند می‌شود
        if (!isNewspaper && !hasKilledPlant) {
            // اگر قبل از به‌روزرسانی در حالت خوردن بود و اکنون نیست، یعنی گیاه را کشته است
            if (wasEating && !(this.currentState instanceof EatingState)) {
                hasKilledPlant = true;
                // کند شدن
                this.speed = this.originalSpeed * 0.3;
                this.damage = 20; //برگشت به آسیب عادی
                System.out.println("All-Star killed a plant and slowed down.");
            }
        }
    }

    public boolean isPhaseChanged() {
        return isPhaseChanged;
    }
}