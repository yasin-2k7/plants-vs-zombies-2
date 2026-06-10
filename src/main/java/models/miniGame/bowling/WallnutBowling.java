package models.miniGame.bowling;

import models.zombie.Zombie;

public class WallnutBowling extends BowlingBall{
    private int hitCount;

    @Override
    public void onHitWall(BowlingLevel level) {

    }

    @Override
    public void onHitZombie(Zombie zombie, BowlingLevel level) {

    }
}
