package models.miniGame.IZombie;

import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

public class IZombieLevel extends GameWorld {
    private List<Zombie> availableZombies;
    private List<Brain> brains;
    private List<SunProducer> sunProducers;
    private int redLineCol;

    public IZombieLevel(LevelSetup levelSetup,
                        ArrayList<LoseCondition> loseConditions,
                        WinCondition winCondition,
                        ArrayList<Mechanic> mechanics) {
        super(levelSetup, loseConditions, winCondition, mechanics);
        this.brains = new ArrayList<>();
        this.sunProducers = new ArrayList<>();
        this.availableZombies = new ArrayList<>();
        this.redLineCol = 5;
        setSun(150);
    }

    @Override
    protected void applyChapterRules() {
        setSun(150);
    }

    @Override
    public void tick(){
        super.tick();

        for (SunProducer sp : sunProducers) {
            if (!sp.isDead() && getActiveZombies().contains(sp)) {
                sp.updateSunGeneration(this);
            }
        }

        for (Zombie zombie : getActiveZombies()) {
            if (zombie.isDead()) continue;

            int row = (int) (zombie.getY() / 100);
            Brain brain = getBrainAtRow(row);
            if (brain != null && !brain.isEaten()) {
                if (zombie.getX() <= brain.getX() + 20) {
                    brain.eat();
                    zombie.setDead(true);
                }
            }
        }


    }

    public boolean canPlaceZombie(Zombie zombie, float x, float y){
        int col = (int) (x / 100);
        if (col < redLineCol) return false;

        int cost = getZombieCost(zombie);
        return getSun() >= cost;
    }

    public void placeZombie(Zombie zombie, float x, float y) {
        if (canPlaceZombie(zombie, x, y)) {
            setSun(getSun() - getZombieCost(zombie));
            zombie.setX(x);
            zombie.setY(y);
            addZombie(zombie);
        }
    }

    public int getZombieCost(Zombie zombie) {
        String typeName = zombie.getName().name();
        return switch (typeName) {
            case "NORMAL", "ZombieDefault" -> 50;
            default -> 75;
        };
    }

    public Brain getBrainAtRow(int row) {
        for (Brain brain : brains) {
            if (brain.getRow() == row) return brain;
        }
        return null;
    }

    public int getMinZombieCost() {
        int minCost = Integer.MAX_VALUE;
        for (Zombie z : availableZombies) {
            int cost = getZombieCost(z);
            if (cost < minCost) minCost = cost;
        }
        return minCost == Integer.MAX_VALUE ? 50 : minCost;
    }

    @Override
    public void addSunToPlayer(int amount) {
        setSun(getSun() + amount);
    }

    public List<Brain> getBrains() { return brains; }
    public List<SunProducer> getSunProducers() { return sunProducers; }
    public List<Zombie> getAvailableZombies() { return availableZombies; }
    public void setAvailableZombies(List<Zombie> availableZombies) { this.availableZombies = availableZombies; }


}
