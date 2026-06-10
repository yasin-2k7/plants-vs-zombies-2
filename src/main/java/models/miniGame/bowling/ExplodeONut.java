package models.miniGame.bowling;

import models.zombie.Zombie;

public class ExplodeONut extends BowlingBall{
    private int explosionRadius = 1;

    @Override
    public void onHitWall(BowlingLevel level) {

    }

    @Override
    public void onHitZombie(Zombie zombie, BowlingLevel level) {

    }
}

