package models.miniGame.bowling;

import models.enums.PlantType;
import models.plant.card.PlantCard;
import models.projectile.Projectile;
import models.world.GameWorld;
import models.world.mechanics.Mechanic;

import java.util.List;

public class BowlingMechanics implements Mechanic {

    @Override
    public void applyMechanic(GameWorld world) {

    }

    public String throwBall(GameWorld world, PlantType plantType, float x, float y) {
        int row = (int) (y / 100);
        int col = (int) (x / 100);

        List<PlantCard> conveyor = world.getConveyorBelt();

        PlantCard cardToUse = null;
        for (PlantCard card : conveyor) {
            if (card.getType() == plantType) {
                cardToUse = card;
                break;
            }
        }

        if (cardToUse == null) {
            return "you dont have " + plantType + "in conveyor belt";
        }

        conveyor.remove(cardToUse);

        if (row < 0 || row >= world.getRows() || col < 0 || col >= world.getCols()) {
            return "you cannot place a bowling ball there!";
        }

        if (!world.getGrid()[row][col].isPlantable()) {
            return "you cannot place beyond the red line!";
        }

        Projectile ball = BowlingBallFactory.create(plantType, x, y);
        world.addProjectile(ball);

        return "A " + plantType.name() + " starts rolling!";
    }
}
