package com.pvz2.models.plant.components;

import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.components.explosiveBehaviors.ExplosiveBehavior;
import com.pvz2.models.plant.components.explosiveTriggers.ExplosiveTrigger;
import com.pvz2.models.world.Cell;

public class ExplosivesComponent implements GameComponent {
    private ExplosiveBehavior explosiveBehavior;
    private ExplosiveTrigger triggerStrategy;
    private float postTriggerDelay = 2.0f;
    private float maxPostTriggerDelay = 2.0f;
    private boolean isArmed;
    private boolean isTriggered = false;
    private float armTimer;
    private int lives = 1;
    private Cell target;


    private ExplosivePlantFoodBehavior plantFoodBehavior;


    private ExplosiveBehavior delayedBehavior;
    private ExplodeCallback explodeCallback;

    public ExplosivesComponent(ExplosiveTrigger trigger, ExplosiveBehavior behavior,
                               float armTime, float maxPostTriggerDelay) {
        this.triggerStrategy = trigger;
        this.explosiveBehavior = behavior;
        this.armTimer = armTime;
        this.isArmed = (armTime <= 0);
        this.maxPostTriggerDelay = maxPostTriggerDelay;
    }


    public void update(Plant owner, float delta) {
        if (!isArmed) {
            if (owner.getState() != Plant.State.UNARMED) {
                owner.setState(Plant.State.UNARMED);
            }
            armTimer -= delta;
            if (armTimer <= 0) {
                isArmed = true;
                owner.setState(Plant.State.IDLE);
            }
            return;
        }

        if (isTriggered) {
            if (owner.getState() != Plant.State.TRIGGERED) {
                owner.setState(Plant.State.TRIGGERED);
            }
            postTriggerDelay -= delta;
            if (postTriggerDelay <= 0) {
                lives--;
                explosiveBehavior.execute(owner);

                if (explodeCallback != null) {
                    explodeCallback.onExplode(owner);
                }

                if (lives > 0) {
                    postTriggerDelay = maxPostTriggerDelay;
                    isTriggered = false;
                    owner.setState(Plant.State.IDLE);
                } else {
                    if (delayedBehavior != null) {
                        delayedBehavior.execute(owner);
                    }
                    if (owner.getType() == PlantType.HOT_POTATO) owner.setCell(null);
                    owner.die();
                }
            }
            return;
        }

        if (owner.getState() != Plant.State.IDLE) {
            owner.setState(Plant.State.IDLE);
        }

        if (triggerStrategy.shouldTrigger(owner, this)) {
            isTriggered = true;
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        if (this.plantFoodBehavior != null) {
            this.plantFoodBehavior.execute(owner, this);
        }
    }

    public void scheduleDelayedBehavior(ExplosiveBehavior behavior) {
        this.delayedBehavior = behavior;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setPlantFoodBehavior(ExplosivePlantFoodBehavior plantFoodBehavior) {
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

    public void setExplodeCallback(ExplodeCallback callback) {
        this.explodeCallback = callback;
    }

    public interface ExplodeCallback {
        void onExplode(Plant owner);
    }
}
