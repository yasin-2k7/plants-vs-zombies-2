package models.miniGame.IZombie;

import models.world.GameWorld;
import models.zombie.Zombie;

import java.util.List;

public class IZombieLevel extends GameWorld {
    private int playerSun;
    private List<Zombie> availableZombies;
    private List<Brain> brains;
//    private List<SunProducer> sunProducers;
    private int redLineCol;

    public IZombieLevel(){
        super();

    }

    @Override
    protected void applyChapterRules() {

    }

    @Override
    public void tick(){
        super.tick();
    }

    public boolean canPlaceZombie(Zombie zombie){
        return true;
    }

    public boolean isLost(){
        return true;
    }
}
