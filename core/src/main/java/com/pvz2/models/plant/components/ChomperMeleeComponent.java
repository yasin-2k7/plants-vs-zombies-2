package com.pvz2.models.plant.components;

import com.pvz2.controller.LevelMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.plant.AnimationDurations;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.zombie.Zombie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChomperMeleeComponent implements GameComponent {
    private static final String BITE_CLIP = "special";
    private static final float DEFAULT_BITE_DURATION = 0.6f;

    private final float digestTime;
    private boolean isDigesting = false;
    private float digestProgressTime = 0f;
    private float biteAnimTimer = 0f;

    // resolved lazily once the owner's PlantType is known
    private float biteDuration = -1f;

    public ChomperMeleeComponent(float digestTime) {
        this.digestTime = digestTime;
    }

    private void ensureBiteDurationLoaded(Plant owner) {
        if (biteDuration >= 0f) return;
        biteDuration = AnimationDurations.getDuration(owner.getType(), BITE_CLIP, DEFAULT_BITE_DURATION);
    }

    @Override
    public void update(Plant owner, float delta) {
        ensureBiteDurationLoaded(owner);

        if (isDigesting) {
            digestProgressTime += delta;

            if (owner.getState() == Plant.State.SPECIAL) {
                biteAnimTimer += delta;
                if (biteAnimTimer >= biteDuration) {
                    owner.setState(Plant.State.SPECIAL_IDLE);
                }
            }

            if (digestProgressTime >= digestTime) {
                isDigesting = false;
                digestProgressTime = 0f;
                biteAnimTimer = 0f;
                owner.setState(Plant.State.IDLE);
            }
            return;
        }

        if (owner.getState() != Plant.State.IDLE) {
            owner.setState(Plant.State.IDLE);
        }

        Zombie target = findTargetZombie(owner);
        if (target != null) {
            swallowZombie(owner, target);
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

    private void swallowZombie(Plant owner, Zombie zombie) {
        if (zombie.isBoss()) return;
        zombie.takeDamage((int) zombie.getHealth(), "NORMAL");
        this.isDigesting = true;
        this.digestProgressTime = 0f;
        this.biteAnimTimer = 0f;
        owner.setState(Plant.State.SPECIAL);
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

        boolean swallowedAny = false;
        for (Zombie target : targets) {
            if (target.isBoss()) continue;
            target.takeDamage((int) target.getHealth(), "NORMAL");
            swallowedAny = true;
        }

        if (swallowedAny) {
            this.isDigesting = true;
            this.digestProgressTime = 0f;
            this.biteAnimTimer = 0f;
            owner.setState(Plant.State.SPECIAL);
        }
    }
}
