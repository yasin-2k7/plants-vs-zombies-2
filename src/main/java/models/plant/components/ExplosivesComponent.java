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
    private int maxPostTriggerDelay = 7;
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
                explosiveBehavior.execute(owner);
                if (lives > 0){
                    postTriggerDelay = maxPostTriggerDelay;
                    isTriggered = false;
                }
                else{
                    owner.getCell().findAndRemovePlant();
                }
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
                owner.getCell().findAndRemovePlant();
            }
        }

    }

    @Override
    public void activatePlantFood(Plant owner) {
        if (this.plantFoodBehavior != null){
            this.plantFoodBehavior.execute(owner, this);
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

    public void setPostTriggerDelay(int postTriggerDelay) {
        this.postTriggerDelay = postTriggerDelay;
    }

    public Cell getTarget() {
        return target;
    }

    public void setTarget(Cell target) {
        this.target = target;
    }

    public void instantArm() {
        isArmed = true;
    }
}
