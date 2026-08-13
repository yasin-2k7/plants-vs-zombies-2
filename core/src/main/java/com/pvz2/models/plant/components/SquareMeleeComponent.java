package com.pvz2.models.plant.components;

import com.pvz2.models.core.App;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.zombie.Zombie;

import java.util.List;

public class SquareMeleeComponent implements GameComponent {
    private final int maxStage;
    private final float stage2Time = 24f;
    private final float stage3Time = 72f;
    private final float stage4Time = 144f;
    private final int baseDamage;
    private float plantationTime = 0f;
    private int currentStage = 1;
    private boolean hasGrowing;
    private float attackIntervalTime;
    private float lastAttackTime = 0f;

    public SquareMeleeComponent(int baseDamage, float attackIntervalTime, boolean hasGrowing, int maxStage) {
        this.baseDamage = baseDamage;
        this.maxStage = maxStage;
        this.hasGrowing = hasGrowing;
        this.attackIntervalTime = attackIntervalTime;
    }

    public SquareMeleeComponent(int baseDamage, float attackIntervalTime) {
        this(baseDamage, attackIntervalTime, false, 1);
    }

    @Override
    public void update(Plant owner, float delta) {
        plantationTime+= delta;
        lastAttackTime+= delta;

        if (hasGrowing) checkGrowth();

        if (lastAttackTime >= attackIntervalTime) {
            performSonicWaveAttack(owner);
            lastAttackTime = 0f;
        }
    }

    private void checkGrowth() {
        if (currentStage == 1 && plantationTime >= stage2Time) {
            currentStage = 2;
        } else if (currentStage == 2 && plantationTime >= stage3Time) {
            currentStage = 3;
        } else if (currentStage == 3 && maxStage >= 4 && plantationTime >= stage4Time) {
            currentStage = 4;
        }
    }

    private int getCurrentDamage() {
        return baseDamage + (currentStage - 1) * 15;
    }

    private void performSonicWaveAttack(Plant owner) {
        Cell cell = owner.getCell();
        if (cell == null) return;

        int currentDmg = getCurrentDamage();
        int radius = (currentStage >= 3) ? 2 : 1;

        List<Zombie> targets = Cell.getZombiesInCells(Cell.getNeighborCells(
                cell, App.getCurrentGame().getGrid(), radius));
        for (Zombie zombie : targets) {
            zombie.takeDamage(currentDmg, "NORMAL");
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        this.currentStage = maxStage;
        this.plantationTime = (maxStage == 4) ? stage4Time : stage3Time;

        Cell cell = owner.getCell();
        if (cell != null) {
            int pfSlamDamage = 350;
            List<Zombie> targets = Cell.getZombiesInCells(Cell.getNeighborCells(
                    cell, App.getCurrentGame().getGrid(), 2));
            for (Zombie zombie : targets) {
                zombie.takeDamage(pfSlamDamage, "NORMAL");
            }
        }
    }
}
