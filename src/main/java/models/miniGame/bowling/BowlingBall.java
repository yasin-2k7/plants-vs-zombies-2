package models.miniGame.bowling;

import models.zombie.Zombie;

public abstract class BowlingBall {
    private int row;
    private int col;
    private int damage;
    private boolean isActive;

    public abstract void onHitZombie(Zombie zombie, BowlingLevel level);
    public abstract void onHitWall(BowlingLevel level);
}
