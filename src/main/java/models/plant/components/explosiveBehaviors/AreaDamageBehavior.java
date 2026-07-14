package models.plant.components.explosiveBehaviors;

import models.core.App;
import models.plant.Plant;
import models.plant.components.explosionRanges.ExplosionRange;
import models.world.Cell;
import models.zombie.Zombie;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public class AreaDamageBehavior implements ExplosiveBehavior{
    private int damage;
    private ExplosionRange area;
    private boolean finished;

    public AreaDamageBehavior(int damage, ExplosionRange area) {
        this.damage = damage;
        this.area = area;
    }

    @Override
    public void execute(Plant owner) {
        List<Cell> cells = area.getCells(owner);
        DoubleSummaryStatistics xStats = cells.stream()
                .mapToDouble(Cell::getX)
                .summaryStatistics();
        double minX = xStats.getMin() - App.getCellWidth()/2;
        double maxX = xStats.getMax() + App.getCellWidth()/2;
        DoubleSummaryStatistics yStats = cells.stream()
                .mapToDouble(Cell::getY)
                .summaryStatistics();
        double minY = yStats.getMin();
        double maxY = yStats.getMax();

        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            if (zombie.getY() <= maxY && zombie.getY() >= minY && zombie.getX() <= maxX && zombie.getX() >= minX){
                zombie.takeDamage(damage, "NORMAL");
            }
        }
        finished = true;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
