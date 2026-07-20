package models.miniGame;

import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;

import java.util.ArrayList;

public class MiniGameWorld extends GameWorld {
    protected int levelNumber;

    public MiniGameWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions,
                         WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

    @Override
    protected void applyChapterRules() {

    }


}
