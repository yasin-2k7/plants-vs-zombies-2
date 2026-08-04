package com.pvz2.models.miniGame;

import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.world.loseCondition.LoseCondition;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.winCondition.WinCondition;

import java.util.ArrayList;

public class MiniGameWorld extends GameWorld {

    public MiniGameWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions,
                         WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
    }

    @Override
    protected void applyChapterRules() {

    }


}
