package models.miniGame.bowling;

import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;

import java.util.List;

public class BowlingSetup implements LevelSetup {
    private int redLineCol;
    private List<BowlingBallType> availableBalls;

    @Override
    public void groundSetup(GameWorld game) {

    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
