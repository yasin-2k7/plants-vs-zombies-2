package models.plant.components;

import controller.LevelMenuController;
import models.core.App;
import models.plant.GameComponent;
import models.plant.Plant;
import models.world.Cell;
import models.zombie.Zombie;
import models.zombie.zombiesType.ArmoredZombie;

import java.util.List;

public class MagnetShroomComponent implements GameComponent {
    int radius;
    int currentDisableTick = 0;
    int disableTicks = 150;
    boolean disable = false;

    public MagnetShroomComponent(int radius) {
        this.radius = radius;
    }

    @Override
    public void update(Plant owner) {
        if (disable){
            currentDisableTick--;
            if (currentDisableTick <= 0){
                disable = false;
            }
            return;
        }
        List<Cell> cells = Cell.getNeighborCells(owner.getCell(), LevelMenuController.getGameCells(), radius);
        List<Zombie> zombies = Cell.getZombiesInCells(cells);
        for (Zombie zombie : zombies){
            if (zombie instanceof ArmoredZombie armoredZombie){
                armoredZombie.stripArmor();
                disable = true;
                currentDisableTick = disableTicks;
                return;
            }
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        List<Cell> cells = Cell.getNeighborCells(owner.getCell(), LevelMenuController.getGameCells(), radius);
        List<Zombie> zombies = Cell.getZombiesInCells(cells);
        int count = 0;
        for (Zombie zombie : zombies){
            if (zombie instanceof ArmoredZombie armoredZombie){
                count++;
                armoredZombie.stripArmor();
                if (count == 3){
                    break;
                }
            }
        }
        disable = false;
    }
}
