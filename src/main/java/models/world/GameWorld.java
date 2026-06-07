package models.world;

import models.miniGame.MechanicsStrategy;
import models.pool.GenericObjectPool;

public abstract class GameWorld {
    protected int rows;
    protected int cols;
    protected Cell[][] grid;
    private MechanicsStrategy mechanicsStrategy;




    public void update(){

    }

    protected abstract void applyChapterRules();

    public void tick(){}
}