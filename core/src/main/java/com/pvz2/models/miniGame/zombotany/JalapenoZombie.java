package com.pvz2.models.miniGame.zombotany;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;

public class JalapenoZombie extends Zombie {
    private static final float EXPLODE_AFTER_TIME = 10f;
    private float timeSinceSpawn = 0f;
    private boolean exploded = false;

    public JalapenoZombie(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
    }

    @Override
    public void update(float delta) {
        if (isDead || exploded) return;
        super.update(delta);
        if (isDead) return;

        timeSinceSpawn+= delta;
        if (timeSinceSpawn >= EXPLODE_AFTER_TIME) {
            explodeRow();
        }
    }

    private void explodeRow() {
        exploded = true;
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        int row = (int) (this.y / App.getCellHeight());
        for (int col = 0; col < game.getCols(); col++) {
            Plant plant = game.getGrid()[row][col].getPlant();
            if (plant != null && !plant.isDead()) {
                plant.takeDamage(9999);
            }
        }
        GameMenuController.updateState("Jalapeno zombie sets the row on fire!");
        die();
    }
}
