package models.plant.components;

import controller.LevelMenuController;
import models.plant.GameComponent;
import models.plant.Plant;
import models.world.Cell;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

public class DirectionalMeleeComponent implements GameComponent {
    private final int damage;
    private final int attackIntervalTicks;
    private final float rangeX;
    private int lastAttackTick = 0;

    public DirectionalMeleeComponent(int damage, int attackIntervalTicks, float rangeX) {
        this.damage = damage;
        this.attackIntervalTicks = attackIntervalTicks;
        this.rangeX = rangeX;
    }

    @Override
    public void update(Plant owner) {
        lastAttackTick++;

        if (lastAttackTick >= attackIntervalTicks) {
            List<Zombie> targets;
            if ((targets = checkRight(owner)) != null) {
                if (!targets.isEmpty()) {
                    attack(targets);
                    lastAttackTick = 0;
                    return;
                }
            }
            if ((targets = checkLeft(owner)) != null) {
                if (!targets.isEmpty()) {
                    attack(targets);
                    lastAttackTick = 0;
                }
            }
        }
    }

    public List<Zombie> checkRight(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return null;

        List<Cell> cells = Cell.getCellsInRow(cell, LevelMenuController.getGameCells());
        List<Zombie> rowZombies = Cell.getZombiesInCells(cells);
        List<Zombie> targets = new ArrayList<>();
        for (Zombie zombie : rowZombies) {
            if (zombie.getX() >= owner.getX() && zombie.getX() - owner.getX() <= rangeX) {
                targets.add(zombie);
            }
        }
        return targets;
    }

    public List<Zombie> checkLeft(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return null;

        List<Cell> cells = Cell.getCellsInRow(cell, LevelMenuController.getGameCells());
        List<Zombie> rowZombies = Cell.getZombiesInCells(cells);
        List<Zombie> targets = new ArrayList<>();
        for (Zombie zombie : rowZombies) {
            if (zombie.getX() <= owner.getX() && owner.getX() - zombie.getX() <= rangeX) {
                targets.add(zombie);
            }
        }
        return targets;
    }

    private void attack(List<Zombie> targets) {
        for (Zombie zombie : targets) {
            zombie.takeDamage(damage, "NORMAL");
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return;

        int plantFoodDamage = 300;
        List<Cell> cells = Cell.getNeighborCells(cell, LevelMenuController.getGameCells(), 1);
        List<Zombie> targets = Cell.getZombiesInCells(cells);
        for (Zombie zombie : targets) {
            zombie.takeDamage(plantFoodDamage, "NORMAL");
        }
    }

}
