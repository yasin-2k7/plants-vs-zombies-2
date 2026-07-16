package models.miniGame.vaseBreaker;

import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;

public class VaseBreakerSetup implements LevelSetup {
    private int rows;
    private int cols;

    public VaseBreakerSetup(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void groundSetup(GameWorld game) {

    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
