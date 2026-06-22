package models.zombie.state;

import models.core.App;
import models.plant.Plant;
import models.zombie.Zombie;
import models.world.GameWorld;

public class WalkingState implements ZombieState {
    @Override
    public void handleAction(Zombie zombie) {
        zombie.move();

        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        Plant targetPlant = game.getPlantAtPosition(zombie.getX(), zombie.getY());
        if (targetPlant != null && !targetPlant.isDead()) {
            zombie.setState(new EatingState(targetPlant));
        }
    }
}
