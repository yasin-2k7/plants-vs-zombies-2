package models.world;

import models.core.User;
import models.enums.PlantType;
import models.lawnMower.LawnMower;
import models.miniGame.MechanicsStrategy;
import models.plant.Plant;
import models.pool.GenericObjectPool;
import models.projectile.Projectile;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class GameWorld {
    private long startTime;
    private long currentTime;

    private GameState state;

    protected int rows;
    protected int cols;
    protected Cell[][] grid;
    private MechanicsStrategy mechanicsStrategy;
    private int currentSun;

    private LevelSetup levelSetup;
    private ArrayList<LoseCondition> loseConditions;
    private WinCondition winCondition;
    private ArrayList<Mechanic> mechanics;

    private List<PlantType> conveyorBelt;
    private List<PlantType> plantLists;
    private boolean isConveyorMode;

    protected List<Zombie> activeZombies;
    protected List<Plant> activePlants;
    protected List<Sun> activeSuns;
    protected List<Projectile> activeProjectiles;
    protected List<LawnMower> lawnMowers;

    private GenericObjectPool<Sun> sunsPool = new GenericObjectPool<>(Sun::new);


    public GameWorld(LevelSetup levelSetup,
                     ArrayList<LoseCondition> loseConditions,
                     WinCondition winCondition,
                     ArrayList<Mechanic> mechanics) {
        this.levelSetup = levelSetup;
        this.loseConditions = loseConditions;
        this.winCondition = winCondition;
        this.mechanics = mechanics;

        this.activeZombies = new ArrayList<>();
        this.activePlants = new ArrayList<>();
        this.activeSuns = new ArrayList<>();
        this.activeProjectiles = new ArrayList<>();
        this.lawnMowers = new ArrayList<>();
        this.sunsPool = new GenericObjectPool<>(Sun::new);

        this.startTime = System.currentTimeMillis();
        this.state = GameState.PLAYING;

        this.levelSetup.groundSetup(this);
    }

    public void update(){

    }

    public void ShowDetails(){

    }

    protected abstract void applyChapterRules();

    public void tick(){
        if(state != GameState.PLAYING) return;

        for(Mechanic mechanic : mechanics){
            mechanic.applyMechanic(this);
        }

        if(winCondition.checkWin(this)){
            state = GameState.WON;
        }

        for(LoseCondition lose : loseConditions){
            if(lose.checkLose(this)){
                state = GameState.LOST;
            }
        }
    }


    public List<Sun> getActiveSuns() {
        return activeSuns;
    }

    public GenericObjectPool<Sun> getSunsPool() {
        return sunsPool;
    }

    public void setSun(int sun) {
        currentSun = sun;
    }

    public int getSun() {
        return currentSun;
    }

    public List<Plant> getActivePlants() {
        return activePlants;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public void setConveyorMode(boolean conveyorMode) {
        isConveyorMode = conveyorMode;
    }

    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }

    public List<Zombie> getActiveZombies() { return activeZombies; }
    public List<Projectile> getActiveProjectiles() { return activeProjectiles; }
    public List<LawnMower> getLawnMowers() { return lawnMowers; }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
}