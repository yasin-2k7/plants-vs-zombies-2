package models.plant.components.explosiveBehaviors;

import models.plant.Plant;
import models.plant.components.explosionRanges.ExplosionRange;
import models.world.Cell;

import java.util.List;

public class SingleTargetDamageBehavior implements ExplosiveBehavior{
    private ExplosionRange area;
    private boolean finished;

    public SingleTargetDamageBehavior(ExplosionRange area) {
        this.area = area;
    }

    @Override
    public void execute(Plant owner) {
        List<Cell> cells = area.getCells(owner);

        Cell.getZombiesInCells(cells).stream()
                .findFirst()
                .ifPresent(targetZombie -> {
                    targetZombie.pullUnderWater();
                    owner.die();
                });

        finished = true;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
