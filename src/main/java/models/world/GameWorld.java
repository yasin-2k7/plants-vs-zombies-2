package models.world;

import models.enums.PlantType;
import models.miniGame.MechanicsStrategy;
import models.pool.GenericObjectPool;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class GameWorld {
    protected int rows;
    protected int cols;
    protected Cell[][] grid;
    private MechanicsStrategy mechanicsStrategy;
    private int Sun;

    private LevelSetup levelSetup;
    private ArrayList<LoseCondition> loseConditions;
    private WinCondition winCondition;
    private ArrayList<Mechanic> mechanics;

    private Queue<PlantType> conveyorBelt;
    private ArrayList<PlantType> plantLists;

    protected List<Zombie> activeZombies;
    protected List<Sun> activeSuns;

    public GenericObjectPool<Sun> SunsPool;


    public GameWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions, WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        this.levelSetup = levelSetup;
        this.loseConditions = loseConditions;
        this.winCondition = winCondition;
        this.mechanics = mechanics;

        this.levelSetup.groundSetup(this);
    }

    public GameWorld() {

    }

    public void update(){

    }

    public void ShowDetails(){

    }

    protected abstract void applyChapterRules();

    public void tick(){}
}