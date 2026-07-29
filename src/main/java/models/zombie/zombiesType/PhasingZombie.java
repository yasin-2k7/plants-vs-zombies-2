package models.zombie.zombiesType;

import controller.GameMenuController;
import models.enums.Zombies;
import models.zombie.Zombie;
import models.zombie.state.EatingState;

public class PhasingZombie extends Zombie {
    private boolean isPhaseChanged;
    private boolean isNewspaper;
    private int shieldHealth;
    private boolean hasKilledPlant;

    public PhasingZombie(int health, double speed, int damage, int shieldHealth, boolean isNewspaper) {
        super(Zombies.PHASING, health, speed, damage);
        this.shieldHealth = shieldHealth;
        this.isNewspaper = isNewspaper;
        this.isPhaseChanged = false;
        this.hasKilledPlant = false;

        if (!isNewspaper) {
            this.originalSpeed = speed * 3;
            this.speed = this.originalSpeed;
            this.damage = 9999;
        } else {
            this.originalSpeed = speed;
            this.speed = this.originalSpeed;
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;

        if (!isPhaseChanged && shieldHealth > 0) {
            shieldHealth -= amount;
            if (shieldHealth <= 0) {
                triggerPhaseChange();
            }
        } else {
            super.takeDamage(amount, damageType);
        }
    }

    private void triggerPhaseChange() {
        this.isPhaseChanged = true;
        if (isNewspaper) {
            this.speed = this.originalSpeed * 10.0;
            this.damage = (int) (this.damage * 3);
            GameMenuController.updateState("Newspaper is angry! Speed and damage increased.");
        }
    }

    @Override
    public void update() {
        if (isDead) return;

        boolean wasEating = (this.currentState instanceof EatingState);

        super.update();

        if (!isNewspaper && !hasKilledPlant) {
            if (wasEating && !(this.currentState instanceof EatingState)) {
                hasKilledPlant = true;
                this.speed = this.originalSpeed * 0.3;
                this.damage = 20;
                GameMenuController.updateState("All-Star killed a plant and slowed down.");
            }
        }
    }

    public boolean isPhaseChanged() {
        return isPhaseChanged;
    }
}