package models.miniGame.bowling;

import models.plant.Plant;
import models.world.GameWorld;

import java.util.List;
import java.util.Queue;

public class BowlingLevel extends GameWorld {
    private List<BowlingBall> activeBalls;
    private Queue<Plant> conveyorBelt;
    private int redLineCol;

    @Override
    protected void applyChapterRules() {

    }
}
