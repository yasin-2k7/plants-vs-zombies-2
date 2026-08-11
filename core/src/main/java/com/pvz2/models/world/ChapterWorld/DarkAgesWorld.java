package com.pvz2.models.world.ChapterWorld;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.cellTerrains.LandTerrain;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.world.loseCondition.LoseCondition;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.mechanics.SunSpawnMechanic;
import com.pvz2.models.world.obstacles.Grave;
import com.pvz2.models.world.winCondition.WinCondition;

import java.util.ArrayList;
import java.util.Random;

public class DarkAgesWorld extends GameWorld {

    private static final int MIN_GRAVES = 3;
    private static final int MAX_GRAVES = 6;
    private Random random;

    public DarkAgesWorld(LevelSetup levelSetup,
                         ArrayList<LoseCondition> loseConditions,
                         WinCondition winCondition,
                         ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

    @Override
    protected void applyChapterRules() {
        this.random = new Random();

        removeSunSpawnMechanic();
        spawnInitialGraves();
        markNecromancyCells();

        // زمین معمولی
        Cell[][] grid = getGrid();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                cell.setTerrain(new LandTerrain());
            }
        }
    }

    private void removeSunSpawnMechanic() {
        getMechanics().removeIf(m -> m instanceof SunSpawnMechanic);
    }

    private void spawnInitialGraves() {
        int graveCount = MIN_GRAVES + random.nextInt(MAX_GRAVES - MIN_GRAVES + 1);
//        GameMenuController.updateState(graveCount + " graves have risen from the dark ages!");

        Cell[][] grid = getGrid();
        int spawned = 0;
        int attempts = 0;
        int maxAttempts = graveCount * 10;

        while (spawned < graveCount && attempts < maxAttempts) {
            attempts++;
            int row = random.nextInt(getRows());
            int col = random.nextInt(getCols());
            Cell cell = grid[row][col];

            if (cell.hasObstacle() || !cell.isEmpty()) continue;

            // ایجاد قبر با محتوای احتمالی
            Grave grave = createRandomGrave(col, row);
            cell.setObstacle(grave);
            cell.setPlantable(false);
            addGrave(grave);
            spawned++;
        }
    }

    private Grave createRandomGrave(int col, int row) {
        float x = col * 100f + 50f;
        float y = row * 100f + 50f;

        // احتمال ۲۰٪ قبر دارای خورشید، ۱۰٪ دارای غذای گیاه، بقیه معمولی
        int rand = random.nextInt(100);
        if (rand < 20) {
            return new Grave(x, y, row, col, Grave.GraveType.SUN);
        } else if (rand < 30) {
            return new Grave(x, y, row, col, Grave.GraveType.PLANT_FOOD);
        } else {
            return new Grave(x, y, row, col, Grave.GraveType.NORMAL);
        }
    }

    private void markNecromancyCells() {
        // ۲۰٪ از سلول‌ها پتانسیل نکرو‌منسی پیدا می‌کنند
        Cell[][] grid = getGrid();
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getCols(); c++) {
                if (random.nextDouble() < 0.2) {
                    grid[r][c].setNecromancyPotential(true);
                }
            }
        }
    }

    // متد کمکی برای ایجاد قبر جدید در ابتدای موج
    public void spawnWaveGraves() {
        Cell[][] grid = getGrid();
        int newGraves = 1 + random.nextInt(3); // ۱ تا ۳ قبر جدید

        for (int i = 0; i < newGraves; i++) {
            int row = random.nextInt(getRows());
            int col = random.nextInt(getCols());
            Cell cell = grid[row][col];

            // فقط در خانه‌های خالی و بدون مانع
            if (!cell.hasObstacle() && cell.isEmpty()) {
                Grave grave = createRandomGrave(col, row);
                cell.setObstacle(grave);
                cell.setPlantable(false);
                addGrave(grave);
//                GameMenuController.updateState("A new grave has risen at (" + col + ", " + row + ")");
            }
        }
    }

    // متد برای نکرو‌منسی: در ابتدای هر موج، از قبرهای روی سلول‌های دارای پتانسیل، زامبی خارج می‌شود
    public void triggerNecromancy() {
        Cell[][] grid = getGrid();
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getCols(); c++) {
                Cell cell = grid[r][c];
                if (cell.isNecromancyPotential() && cell.hasObstacle() && cell.getObstacle() instanceof Grave) {

                    cell.setNecromancyTriggered(true);
                }
            }
        }
    }
}
