package models.miniGame.bowling;

import models.miniGame.LevelSetupStrategy;
import models.world.GameWorld;

import java.util.List;

public class BowlingSetup implements LevelSetupStrategy {
    private int redLineCol;
    private List<BowlingBallType> availableBalls;
    @Override
    public void setupBoard(GameWorld world) {

    }
}
