package com.pvz2.models.plant.components;

import com.pvz2.controller.LevelMenuController;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.zombiesType.ArmoredZombie;

import java.util.List;

public class MagnetShroomComponent implements GameComponent {
    int radius;
    float currentDisableTime = 0f;
    float disableTime = 15f;
    boolean disable = false;

    public MagnetShroomComponent(int radius) {
        this.radius = radius;
    }

    @Override
    public void update(Plant owner, float delta) {
        if (disable) {
            currentDisableTime-= delta;
            if (currentDisableTime <= 0) {
                disable = false;
                owner.setState(Plant.State.IDLE);
            }
            return;
        }
        List<Cell> cells = Cell.getNeighborCells(owner.getCell(), LevelMenuController.getGameCells(), radius);
        List<Zombie> zombies = Cell.getZombiesInCells(cells);
        for (Zombie zombie : zombies) {
            if (zombie instanceof ArmoredZombie armoredZombie) {
                armoredZombie.stripArmor();
                disable = true;
                owner.setState(Plant.State.BUSY);
                currentDisableTime = disableTime;
                return;
            }
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        List<Cell> cells = Cell.getNeighborCells(owner.getCell(), LevelMenuController.getGameCells(), radius);
        List<Zombie> zombies = Cell.getZombiesInCells(cells);
        int count = 0;
        for (Zombie zombie : zombies) {
            if (zombie instanceof ArmoredZombie armoredZombie) {
                count++;
                armoredZombie.stripArmor();
                if (count == 3) {
                    break;
                }
            }
        }
        disable = false;
    }
}
