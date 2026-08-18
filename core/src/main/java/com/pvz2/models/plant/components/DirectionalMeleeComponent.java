package com.pvz2.models.plant.components;

import com.pvz2.controller.LevelMenuController;
import com.pvz2.models.Damageable;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.obstacles.Obstacle;
import com.pvz2.models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

public class DirectionalMeleeComponent implements GameComponent {
    private final int damage;
    private final float attackIntervalTicks;
    private final float rangeX;
    private float lastAttackTick = 0;

    public DirectionalMeleeComponent(int damage, float attackIntervalTicks, float rangeX) {
        this.damage = damage;
        this.attackIntervalTicks = attackIntervalTicks;
        this.rangeX = rangeX;
    }

    @Override
    public void update(Plant owner, float delta) {
        lastAttackTick += delta;
        if (owner.getState() != Plant.State.IDLE) owner.setState(Plant.State.IDLE);


        if (lastAttackTick >= attackIntervalTicks) {
            List<Damageable> rightTargets;
            List<Damageable> leftTargets;
            if ((rightTargets = checkRight(owner)) != null) {
                if (!rightTargets.isEmpty()) {
                    attack(rightTargets);
                    lastAttackTick = 0;
                    if (owner.getState() != Plant.State.HIT_RIGHT) owner.setState(Plant.State.HIT_RIGHT);
                }
            }
            if ((leftTargets = checkLeft(owner)) != null) {
                if (!leftTargets.isEmpty()) {
                    attack(leftTargets);
                    lastAttackTick = 0;
                    if (rightTargets != null && !rightTargets.isEmpty()){
                        owner.setState(Plant.State.HIT_RIGHT_AND_LEFT);
                        return;
                    }
                    if (owner.getState() != Plant.State.HIT_LEFT) owner.setState(Plant.State.HIT_LEFT);
                }
            }
            if ((rightTargets != null && !rightTargets.isEmpty()) || (leftTargets != null && !leftTargets.isEmpty())){
                return;
            }
        }
    }

    public List<Damageable> checkRight(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return null;

        List<Cell> cells = Cell.getCellsInRow(cell, LevelMenuController.getGameCells());
        List<Zombie> rowZombies = Cell.getZombiesInCells(cells);
        List<Damageable> targets = new ArrayList<>();
        for (Zombie zombie : rowZombies) {
            if (zombie.getX() >= owner.getX() && zombie.getX() - owner.getX() <= rangeX) {
                targets.add(zombie);
            }
        }
        Cell rightCell = Cell.nextCell(cell, LevelMenuController.getGameCells());
        if (rightCell != null){
            Obstacle obstacle = rightCell.getObstacle();
            if (obstacle != null) targets.add(obstacle);
        }
        return targets;
    }

    public List<Damageable> checkLeft(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return null;

        List<Cell> cells = Cell.getCellsInRow(cell, LevelMenuController.getGameCells());
        List<Zombie> rowZombies = Cell.getZombiesInCells(cells);
        List<Damageable> targets = new ArrayList<>();
        for (Zombie zombie : rowZombies) {
            if (zombie.getX() <= owner.getX() && owner.getX() - zombie.getX() <= rangeX) {
                targets.add(zombie);
            }
        }
        Cell rightCell = Cell.previousCell(cell, LevelMenuController.getGameCells());
        if (rightCell != null){
            Obstacle obstacle = rightCell.getObstacle();
            if (obstacle != null) targets.add(obstacle);
        }
        return targets;
    }

    private void attack(List<Damageable> targets) {
        for (Damageable damageable : targets) {
            damageable.takeDamage(damage, "NORMAL");
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
