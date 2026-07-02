package models.world.levelsSpecial;

import models.world.GameWorld;

public class DeadLineLevel extends GameWorld {
    private int deadLineCol;

    @Override
    protected void applyChapterRules() {}

    public void setDeadLineCol(int deadLineCol) {
        this.deadLineCol = deadLineCol;
    }
}
