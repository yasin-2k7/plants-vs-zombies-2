package com.pvz2.models.plant.components;

import controller.LevelMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.zombie.Zombie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChomperMeleeComponent implements GameComponent {
    private final int digestTimeTicks;
    private boolean isDigesting = false;
    private int digestProgressTicks = 0;

    public ChomperMeleeComponent(int digestTimeTicks) {
        this.digestTimeTicks = digestTimeTicks;
    }

    @Override
    public void update(Plant owner) {
        if (isDigesting) {
            digestProgressTicks++;
            if (digestProgressTicks >= digestTimeTicks) {
                isDigesting = false;
                digestProgressTicks = 0;
            }
            return;
        }

        Zombie target = findTargetZombie(owner);
        if (target != null) {
            swallowZombie(target);
        }
    }

    private Zombie findTargetZombie(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return null;

        List<Cell> cells = Cell.getCellsInRow(cell, LevelMenuController.getGameCells());
        List<Zombie> rowZombies = Cell.getZombiesInCells(cells);
        List<Zombie> targets = new ArrayList<>();
        for (Zombie zombie : rowZombies) {
            if (zombie.getX() >= owner.getX() && zombie.getX() - owner.getX() <= App.getCellWidth() * 1.5f) {
                targets.add(zombie);
            }
        }
        if (!targets.isEmpty()) {
            return targets.getFirst();
        }
        return null;
    }

    private void swallowZombie(Zombie zombie) {
        if (zombie.isBoss()) return;
        zombie.takeDamage(zombie.getHealth(), "NORMAL");
        this.isDigesting = true;
        this.digestProgressTicks = 0;
    }

    @Override
    public void activatePlantFood(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return;

        List<Cell> cells = Cell.getCellsInRow(cell, LevelMenuController.getGameCells());
        List<Zombie> rowZombies = Cell.getZombiesInCells(cells);
        if (rowZombies.isEmpty()) return;

        List<Zombie> targets = rowZombies.stream()
                .filter(zombie -> zombie.getX() >= owner.getX())
                .sorted(Comparator.comparingDouble(Zombie::getX))
                .limit(3)
                .toList();

        for (Zombie target : targets) {
            if (target.isBoss()) continue;
            target.takeDamage(target.getHealth(), "NORMAL");
        }

        this.isDigesting = false;
        this.digestProgressTicks = 0;
    }

}
