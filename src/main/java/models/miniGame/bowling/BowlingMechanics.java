package models.miniGame.bowling;

import models.world.GameWorld;
import models.world.mechanics.Mechanic;

public class BowlingMechanics implements Mechanic {

    @Override
    public void applyMechanic(GameWorld world) {

    }

    public String throwBall(GameWorld world, BowlingBallType type, float x, float y) {
        int row = (int) (y / 100);
        int col = (int) (x / 100);

        if (row < 0 || row >= world.getRows() || col < 0 || col >= world.getCols()) {
            return "you cannot place a bowling ball there!";
        }

        if (!world.getGrid()[row][col].isPlantable()) {
            return "you cannot place beyond the red line!";
        }

        BowlingBallFactory.create(type, x, y);
        return "A " + type.name() + " starts rolling!";
    }
}
