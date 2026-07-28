package models.plant.components.explosiveBehaviors;

import models.plant.Plant;
import models.plant.components.explosionRanges.ExplosionRange;
import models.world.Cell;

import java.util.List;

public class MakeUnplantableBehavior implements ExplosiveBehavior {
    private final ExplosionRange range;

    public MakeUnplantableBehavior(ExplosionRange range) {
        this.range = range;
    }

    @Override
    public void execute(Plant owner) {
        List<Cell> affectedCells = range.getCells(owner);

        for (Cell cell : affectedCells) {
            cell.setCraterTime(300);
        }
    }
}
