package models.world;

import models.pool.GenericObjectPool;

public abstract class GameWorld {
    protected int rows;
    protected int cols;
    protected Cell[][] grid;




    public void update(){

    }

    protected abstract void applyChapterRules();
}