package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.state.WalkingState; // اضافه شدن ایمپورت ضروری

import java.util.ArrayList;
import java.util.List;

public class WizardZombie extends Zombie {
    private static final float COOLDOWN_MAX = 3.0f;
    private List<Plant> transformedPlants;
    private float cooldown;

    public WizardZombie(int health, double speed, int damage) {
        super(Zombies.WIZARD, health, speed, damage);
        this.transformedPlants = new ArrayList<>();
        this.cooldown = 0f;
    }

    @Override
    public void update(float delta) {
        if (isDead() || getIceHealth() > 0 || getFreezedTicksRemaining() > 0 || getDisabledTicksRemaining() > 0) {
            super.update(delta);
            return;
        }

        if (cooldown <= 0) {
            castSpell();
            cooldown = COOLDOWN_MAX;
        } else {
            cooldown -= delta;
        }

        super.update(delta);

        if (!isDead() && getCurrentState() != null && !(getCurrentState() instanceof WalkingState)) {
            GameWorld game = App.getCurrentGame();
            if (game != null) {
                int row = (int) ((this.y - App.getFirstCellY()) / App.getCellHeight());
                Plant target = game.getNearestPlantInRow(row, this.x + 50);

                if (target != null && target.isSheep()) {
                    float dist = this.x - target.getX();
                    if (dist > -80 && dist < 120) {
                        setState(new WalkingState());
                        move(delta);
                    }
                }
            }
        }
    }

    private void castSpell() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        int row = (int) ((this.y - App.getFirstCellY()) / App.getCellHeight());
        Plant target = game.getNearestPlantInRow(row, this.x + 10);

        if (target != null && !target.isDead() && !target.isSheep()) {
            float distance = this.x - target.getX();
            if (distance > 0 && distance <= 200) {
                target.setSheep(true);
                transformedPlants.add(target);
                GameMenuController.updateState("Wizard turned a " + target.getType().name() + " into a sheep!");
            }
        }
    }

    @Override
    public void die() {
        for (Plant p : transformedPlants) {
            if (p != null && !p.isDead()) {
                p.setSheep(false);
            }
        }
        transformedPlants.clear();
        super.die();
    }
}
