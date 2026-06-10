package models.miniGame.vaseBreaker;

import models.zombie.Zombie;

public class Vase {
    private int row;
    private int col;
    private boolean isBroken;
    private VaseType type;
    private Zombie hiddenZombie;
    private SeedPacket hiddenSeed;
}
