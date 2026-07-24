package models.plant.components.explosiveBehaviors;

import models.plant.Plant;
import models.world.Cell;
import models.world.obstacles.Grave;

public class RemoveGraveBehavior implements ExplosiveBehavior {
    @Override
    public void execute(Plant owner) {
        Cell currentCell = owner.getCell();
        if (currentCell != null && currentCell.hasObstacle()) {
            if (currentCell.getObstacle() instanceof Grave) currentCell.removeObstacle();
        }
    }
}
