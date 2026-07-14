package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;
import models.plant.components.explosiveBehaviors.ExplosiveBehavior;
import models.plant.components.explosiveTriggers.ExplosiveTrigger;
import models.world.Cell;

public class ExplosivesComponent implements GameComponent {
    private ExplosiveBehavior explosiveBehavior;
    private ExplosiveTrigger triggerStrategy;
    private int postTriggerDelay = 10;
    private int maxPostTriggerDelay = 10;
    private boolean isArmed;
    private boolean isTriggered = false;
    private int armTimer;
    private int lives = 1;
    private Cell target;


    private ExplosivePlantFoodBehavior plantFoodBehavior;


    private ExplosiveBehavior delayedBehavior;
    private int delayTicksLeft = -2;

    public ExplosivesComponent(ExplosiveTrigger trigger, ExplosiveBehavior behavior, int armTime) {
        this.triggerStrategy = trigger;
        this.explosiveBehavior = behavior;
        this.armTimer = armTime;
        this.isArmed = (armTime <= 0);
    }

    public ExplosivesComponent(ExplosiveTrigger trigger, ExplosiveBehavior behavior, int armTime, int maxPostTriggerDelay) {
        this.triggerStrategy = trigger;
        this.explosiveBehavior = behavior;
        this.armTimer = armTime;
        this.isArmed = (armTime <= 0);
        this.maxPostTriggerDelay = maxPostTriggerDelay;
    }

    public void update(Plant owner) {

        if (!isArmed) {
            armTimer--;
            if (armTimer <= 0) {
                isArmed = true;
            }
            return;
        }

        if (isTriggered) {
            postTriggerDelay--;
            if (postTriggerDelay <= 0) {
                lives--;
                if (lives > 0){
                    postTriggerDelay = maxPostTriggerDelay;
                    isTriggered = false;
                }
                explosiveBehavior.execute(owner);
            }
            return;
        }

        if (triggerStrategy.shouldTrigger(owner, this)) {
            isTriggered = true;
        }
        if (delayTicksLeft > 0) {
            delayTicksLeft--;
            if (delayTicksLeft == 0) {
                delayedBehavior.execute(owner);
                delayTicksLeft = -1;
            }
        }

        if (isTriggered && delayTicksLeft == -1){
            owner.die();
        }
    }

    public void scheduleDelayedBehavior(ExplosiveBehavior behavior, int ticks) {
        this.delayedBehavior = behavior;
        this.delayTicksLeft = ticks;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setPlantFoodBehavior(ExplosivePlantFoodBehavior plantFoodBehavior){
        this.plantFoodBehavior = plantFoodBehavior;
    }

    public Cell getTarget() {
        return target;
    }

    public void setTarget(Cell target) {
        this.target = target;
    }
}
