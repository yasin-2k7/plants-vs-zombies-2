package models.world.ChapterWorld;

import models.world.Cell;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.obstacles.Grave;
import models.world.winCondition.WinCondition;

import java.util.ArrayList;
import java.util.Random;

public class AncientEgyptWorld extends GameWorld {
    private static final int MIN_GRAVES = 2;
    private static final int MAX_GRAVES = 5;
    private int deadLineCol;

    public AncientEgyptWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions,
                             WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

    @Override
    protected void applyChapterRules() {
        spawnInitialGraves();
        setSandstormActive(true);
    }

    private void spawnInitialGraves() {
        Random random = new Random();
        int graveCount = MIN_GRAVES + random.nextInt(MAX_GRAVES - MIN_GRAVES + 1);
        Cell[][] grid = getGrid();

        int spawned = 0;
        int attempts = 0;
        int maxAttempts = graveCount * 10;

        int minCol = 4;
        int maxCol = 8;

        while (spawned < graveCount && attempts < maxAttempts) {
            attempts++;
            int row = random.nextInt(getRows());
            int col = minCol + random.nextInt(maxCol - minCol + 1);

            Cell cell = grid[row][col];
            if (cell.hasObstacle() || !cell.isEmpty()) continue;

            float x = col * 100f + 50f;
            float y = row * 100f + 50f;

            Grave grave = new Grave(x, y, row, col, Grave.GraveType.NORMAL);
            cell.setObstacle(grave);
            cell.setPlantable(false);

            addGrave(grave);
            spawned++;
        }
    }

    public void setDeadLineCol(int deadLineCol) {
        this.deadLineCol = deadLineCol;
    }

}
