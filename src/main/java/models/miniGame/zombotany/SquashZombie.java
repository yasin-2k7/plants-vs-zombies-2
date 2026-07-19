package models.miniGame.zombotany;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;

public class SquashZombie extends Zombie {

    public SquashZombie(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
    }

    @Override
    public void update() {
        if (isDead) return;

        GameWorld game = App.getCurrentGame();
        if (game != null) {
            Plant plant = game.getPlantAtPosition(this.x, this.y);
            if (plant != null && !plant.isDead()) {
                plant.takeDamage(9999);
                this.die();
                return;
            }
        }

        super.update();
    }
}
