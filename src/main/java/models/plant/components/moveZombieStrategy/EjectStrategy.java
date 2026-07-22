package models.plant.components.moveZombieStrategy;

import controller.LevelMenuController;
import models.core.App;
import models.plant.Plant;
import models.world.Cell;
import models.zombie.Zombie;

import java.util.List;

public class EjectStrategy implements MoveZombieStrategy{
    @Override
    public void onUpdate(Plant owner) {

    }

    @Override
    public void onTakeDamage(Plant owner, int damage, Zombie attacker) {
        ejectZombie(owner, attacker);
    }

    private void ejectZombie(Plant owner, Zombie attacker) {
        if (owner.getCell().getRow() == 0){
            attacker.setY(attacker.getY() + App.getCellHeight());
        }
        else if (owner.getCell().getRow() == App.getCurrentGame().getRows()-1){
            attacker.setY(attacker.getY() - App.getCellHeight());
        }
        else {
            attacker.setY(attacker.getY() + (Math.random() < 0.5 ? 1 : -1 ) * App.getCellHeight());
        }
    }

    @Override
    public void onPlantFood(Plant owner) {
        List<Cell> cells = Cell.getCellsInRow(owner.getCell(), LevelMenuController.getGameCells());
        List<Zombie> zombieList = Cell.getZombiesInCells(cells);
        for (Zombie zombie : zombieList){
            ejectZombie(owner, zombie);
        }
    }
}
