package models.miniGame.zombotany;

import controller.GameMenuController;
import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;

public class JalapenoZombie extends Zombie {
    private int ticksSinceSpawn = 0;
    private static final int EXPLODE_AFTER_TICKS = 50;
    private boolean exploded = false;

    public JalapenoZombie(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
    }

    @Override
    public void update() {
        if (isDead || exploded) return;
        super.update();
        if (isDead) return;

        ticksSinceSpawn++;
        if (ticksSinceSpawn >= EXPLODE_AFTER_TICKS) {
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
