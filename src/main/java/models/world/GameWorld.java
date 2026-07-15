package models.world;

import models.Damageable;
import models.enums.PlantType;
import models.lawnMower.LawnMower;
import models.miniGame.MechanicsStrategy;
import models.plant.Plant;
import models.plant.card.PlantCard;
import models.plant.components.LifespanComponent;
import models.pool.GenericObjectPool;
import models.projectile.Projectile;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.obstacles.Grave;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

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

    private List<PlantCard> conveyorBelt;
    private List<PlantCard> plantLists;
    private boolean isConveyorMode;

    protected List<Zombie> activeZombies;
    protected List<Plant> activePlants;
    protected List<Sun> activeSuns;
    protected List<Projectile> activeProjectiles;
    protected List<Grave> activeGrave;
    protected List<Damageable> activeTargets;
    protected List<LawnMower> lawnMowers;

    private GenericObjectPool<Sun> sunsPool = new GenericObjectPool<>(Sun::new);
    private GenericObjectPool<Projectile> projectilesPool = new GenericObjectPool<>(Projectile::new);

    private final List<LifespanComponent> smallShrooms = new ArrayList<>();

    public void registerShroom(LifespanComponent observer) {
        smallShrooms.add(observer);
    }
    public void unregisterPuffShroom(LifespanComponent observer) {
        smallShrooms.remove(observer);
    }

    public void triggerPlantFood(PlantType type) {
        for (LifespanComponent observer : smallShrooms) {
            observer.onGlobalPlantFoodActivated(type);
        }
    }

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
        this.activeGrave = new ArrayList<>();
        this.activeTargets = new ArrayList<>();
        this.lawnMowers = new ArrayList<>();
        this.sunsPool = new GenericObjectPool<>(Sun::new);

        this.startTime = System.currentTimeMillis();
        this.state = GameState.PLAYING;

        this.levelSetup.groundSetup(this);
    }

    public GameWorld() {

    }

    // متد پیدا کردن گیاه بر اساس مختصات حرکتی زامبی
        public Plant getPlantAtPosition(float x, float y) {
            int col = (int)(x / 100); //100 مثلا عرض هر سلول
            int row = (int)(y / 100);
            if (row >= 0 && row < rows && col >= 0 && col < cols) {
                return grid[row][col].getPlant();
            }
            return null;
        }


    public Plant getNearestPlantInRow(int row, float x) {
        if (row < 0 || row >= rows) return null;
        Plant nearest = null;
        float minDist = Float.MAX_VALUE;
        for (int c = 0; c < cols; c++) {
            Plant p = grid[row][c].getPlant();
            if (p != null && !p.isDead()) {
                float dist = p.getX() - x;
                if (dist > 0 && dist < minDist) {
                    minDist = dist;
                    nearest = p;
                }
            }
        }
        return nearest;
    }

    public boolean isTileEmpty(float x, float y) {
        int col = (int)(x / 100);
        int row = (int)(y / 100);
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grid[row][col].isEmpty();
        }
        return false;
    }

    public int collectSunInRadius(float x, float y, int radius) {
        // منطق جمع‌آوری خورشیدهای روی زمین
        return 0;
    }

    public int stealSunFromPlayer(int amount) {
        // کسر از ذخیره بازیکن
        return 0;
    }

    public void addSunToPlayer(int amount) {
        // اضافه به ذخیره
    }

    public void createGrave(int x, int y) {
        // ایجاد قبر در مختصات داده‌شده
    }

    public void update(){

    }

    public void ShowDetails(){

    }

    protected abstract void applyChapterRules();

    public void tick(){
        if(state != GameState.PLAYING) return;

        activePlants.forEach(Plant::update);
        activeZombies.forEach(Zombie::update);
        activeProjectiles.forEach(Projectile::update);

        activeSuns.removeIf(sun -> {
            if(sun.isExpired()){
                sunsPool.release(sun);
                return true;
            }
            return false;
        });

        activeZombies.removeIf(Zombie::isDead);
        activePlants.removeIf(Plant::isDead);

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

    public List<Damageable> getActiveTargets() {
        return activeTargets;
    }

    public List<Projectile> getActiveProjectiles() { return activeProjectiles; }
    public List<LawnMower> getLawnMowers() { return lawnMowers; }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public void addZombie(Zombie zombie) { activeZombies.add(zombie); }
    public void addGrave(Grave grave) { activeGrave.add(grave); }
    public void addTarget() {
        activeTargets.addAll(activeZombies);
        activeTargets.addAll(activeGrave);
    }

    public GenericObjectPool<Projectile> getProjectilesPool() {
        return projectilesPool;
    }

    public void addMechanic(Mechanic mechanic){

    }

    public List<PlantCard> getConveyorBelt() {
        return conveyorBelt;
    }

    public <T extends Mechanic> T getMechanic(Class<T> type) {
        return mechanics.stream()
                .filter(m -> type.isInstance(m))
                .map(m -> type.cast(m))
                .findFirst()
                .orElse(null);
    }
}