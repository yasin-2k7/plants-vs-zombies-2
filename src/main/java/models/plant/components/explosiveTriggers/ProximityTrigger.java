package models.plant.components.explosiveTriggers;

import controller.LevelMenuController;
import models.core.App;
import models.plant.Plant;
import models.plant.components.ExplosivesComponent;
import models.world.Cell;
import models.zombie.Zombie;

public class ProximityTrigger implements ExplosiveTrigger{
    private final float rangeX;

    public ProximityTrigger(float rangeX) {
        this.rangeX = rangeX;
    }

    @Override
    public boolean shouldTrigger(Plant owner, ExplosivesComponent component) {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            if (zombie.getY() == owner.getY() && Math.abs(zombie.getX() - owner.getX()) < rangeX/2){
                if (zombie.getX() > owner.getX() + App.getCellWidth()/2){
                    component.setTarget(Cell.nextCell(owner.getCell(), LevelMenuController.getGameCells()));
                }
                else if (zombie.getX() < owner.getX() - App.getCellWidth()/2){
                    component.setTarget(Cell.previousCell(owner.getCell(), LevelMenuController.getGameCells()));
                }
                else component.setTarget(owner.getCell());
                return true;
            }
        }
        return false;
    }
}
