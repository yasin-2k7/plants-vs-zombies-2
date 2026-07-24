package models.plant.components;

import models.core.App;
import models.plant.GameComponent;
import models.plant.Plant;
import models.world.Cell;
import models.zombie.Zombie;

import java.util.List;

public class SquareMeleeComponent implements GameComponent {
    private final int maxStage;
    private final int stage2Ticks = 240;
    private final int stage3Ticks = 720;
    private final int stage4Ticks = 1440;
    private final int baseDamage;
    private int plantationTicks = 0;
    private int currentStage = 1;
    private boolean hasGrowing;
    private int attackIntervalTicks;
    private int lastAttackTick = 0;

    public SquareMeleeComponent(int baseDamage, int attackIntervalTicks, boolean hasGrowing, int maxStage) {
        this.baseDamage = baseDamage;
        this.maxStage = maxStage;
        this.hasGrowing = hasGrowing;
        this.attackIntervalTicks = attackIntervalTicks;
    }

    public SquareMeleeComponent(int baseDamage, int attackIntervalTicks) {
        this(baseDamage, attackIntervalTicks, false, 1);
    }

    @Override
    public void update(Plant owner) {
        plantationTicks++;
        lastAttackTick++;

        if (hasGrowing) checkGrowth();

        if (lastAttackTick >= attackIntervalTicks) {
            performSonicWaveAttack(owner);
            lastAttackTick = 0;
        }
    }

    private void checkGrowth() {
        if (currentStage == 1 && plantationTicks >= stage2Ticks) {
            currentStage = 2;
        } else if (currentStage == 2 && plantationTicks >= stage3Ticks) {
            currentStage = 3;
        } else if (currentStage == 3 && maxStage >= 4 && plantationTicks >= stage4Ticks) {
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

        List<Zombie> targets = Cell.getZombiesInCells(Cell.getNeighborCells(cell, App.getCurrentGame().getGrid(), radius));
        for (Zombie zombie : targets) {
            zombie.takeDamage(currentDmg, "NORMAL");
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        this.currentStage = maxStage;
        this.plantationTicks = (maxStage == 4) ? stage4Ticks : stage3Ticks;

        Cell cell = owner.getCell();
        if (cell != null) {
            int pfSlamDamage = 350;
            List<Zombie> targets = Cell.getZombiesInCells(Cell.getNeighborCells(cell, App.getCurrentGame().getGrid(), 2));
            for (Zombie zombie : targets) {
                zombie.takeDamage(pfSlamDamage, "NORMAL");
            }
        }
    }

    public int getCurrentStage() {
        return currentStage;
    }
}
