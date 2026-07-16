package models.miniGame.vaseBreaker;

import models.zombie.Zombie;

public class Vase {
    private int row;
    private int col;
    private boolean isBroken;
    private VaseType type;
    private Zombie hiddenZombie;
    private SeedPacket hiddenSeed;


    public Vase(int row, int col, VaseType type, Zombie hiddenZombie, SeedPacket hiddenSeed) {
        this.row = row;
        this.col = col;
        this.isBroken = false;
        this.type = type;
        this.hiddenZombie = hiddenZombie;
        this.hiddenSeed = hiddenSeed;
    }

    public void breakVase(VaseBreakerLevel level){
        if(isBroken) return;
        isBroken = true;

        float spawnX = col * 100 + 50;
        float spawnY = row * 100 + 50;

        if(hiddenZombie != null){
            hiddenZombie.setX(spawnX);
            hiddenZombie.setY(spawnY);
            level.addZombie(hiddenZombie);
        }

        if (hiddenSeed != null) {
            level.getDroppedSeeds().add(hiddenSeed);
        }

    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isBroken() { return isBroken; }
    public VaseType getType() { return type; }
    public Zombie getHiddenZombie() { return hiddenZombie; }
    public SeedPacket getHiddenSeed() { return hiddenSeed; }
}
