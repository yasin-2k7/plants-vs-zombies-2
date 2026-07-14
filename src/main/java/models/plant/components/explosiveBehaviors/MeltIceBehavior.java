package models.plant.components.explosiveBehaviors;

import models.plant.Plant;
import models.plant.components.explosionRanges.ExplosionRange;
import models.world.Cell;
import models.world.obstacles.IceBlock;

import java.util.List;

public class MeltIceBehavior implements ExplosiveBehavior{
    private ExplosionRange area;


    public MeltIceBehavior(ExplosionRange area) {
        this.area = area;
    }

    @Override
    public void execute(Plant owner) {
        List<Cell> affectedCells = area.getCells(owner);

        for (Cell cell : affectedCells) {
            if (cell.hasObstacle() && cell.getObstacle() instanceof IceBlock) {
                cell.removeObstacle();
            }
            if (!cell.isEmpty() && cell.getPlant().isFreeze()) {
                cell.getPlant().unfreeze();
            }
        }
    }

}
