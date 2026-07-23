package models.world;

import controller.GameMenuController;
import models.Damageable;
import models.core.App;
import models.core.User;
import models.core.UserDataManager;
import models.enums.Chapter;
import models.enums.PlantFamily;
import models.enums.PlantType;
import models.lawnMower.LawnMowerManager;
import models.mupoint.KillEvent;
import models.mupoint.MupointManager;
import models.plant.Plant;
import models.plant.card.PlantCard;
import models.plant.components.LifespanComponent;
import models.pool.GenericObjectPool;
import models.projectile.Projectile;
import models.quest.QuestStats;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.mechanics.NormalMechanic;
import models.world.obstacles.Grave;
import models.world.obstacles.Obstacle;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;
import models.zombie.wave.WaveManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.*;

public abstract class GameWorld {
    protected Random random = new Random();
    private GameState state;
    private int currentTick = 0;
    private Chapter currentChapter;
    private boolean willUnlockLevel = false;
    protected int plantFoods;
    protected int rows;
    protected int cols;
    protected Cell[][] grid;
    private int currentSun;
    private LevelSetup levelSetup;
    private ArrayList<LoseCondition> loseConditions;
    private WinCondition winCondition;
    private ArrayList<Mechanic> mechanics;
    private List<PlantCard> conveyorBelt;
    private List<PlantCard> plantLists;
    private boolean isConveyorMode;
    protected List<Collectable> activeCollectables;
    protected List<Zombie> activeZombies;
    protected List<Plant> activePlants;
    protected List<Sun> activeSuns;
    protected List<Projectile> activeProjectiles;
    protected List<Obstacle> activeObstacles;
    protected List<Damageable> activeTargets;
    protected LawnMowerManager lawnMowerManager;
    private GenericObjectPool<Sun> sunsPool = new GenericObjectPool<>(Sun::new);
    private GenericObjectPool<Projectile> projectilesPool = new GenericObjectPool<>(Projectile::new);
    private final List<LifespanComponent> smallShrooms = new ArrayList<>();
    private boolean sandstormActive = false;
    private final List<Runnable> zombieKillListeners = new ArrayList<>();
    private final List<Runnable> plantEatenListeners = new ArrayList<>();
    private MupointManager mupointManager;
    private boolean isPlantSelected = false;
    private PlantType selectedPlant = null;

    public void registerZombieKillListener(Runnable listener) {
        zombieKillListeners.add(listener);
    }
    public void notifyZombieKilled() {
        zombieKillListeners.forEach(Runnable::run);
    }
    public void registerPlantEatenListener(Runnable listener) {
        plantEatenListeners.add(listener);
    }
    public void notifyPlantEaten() {
        plantEatenListeners.forEach(Runnable::run);
    }
    public void registerShroom(LifespanComponent observer) {
        smallShrooms.add(observer);
    }
    public void unregisterPuffShroom(LifespanComponent observer) {
        smallShrooms.remove(observer);
    }
    public MupointManager getMupointManager() {
        return mupointManager;
    }
    public void setMupointManager(MupointManager mupointManager) {
        this.mupointManager = mupointManager;
    }

    public void triggerSmallShroomsPlantFood(PlantType type) {
        for (LifespanComponent observer : smallShrooms) {
            observer.onGlobalPlantFoodActivated(type);
        }
    }

    public GameWorld(LevelSetup levelSetup,
                     ArrayList<LoseCondition> loseConditions,
                     WinCondition winCondition,
                     ArrayList<Mechanic> mechanics) {
        App.getCurrentUser().setGamesPlayed(App.getCurrentUser().getGamesPlayed()+1);
        UserDataManager.saveUser(App.getCurrentUser());

        this.levelSetup = levelSetup;
        this.loseConditions = loseConditions;
        this.winCondition = winCondition;
        this.mechanics = mechanics;

        this.activeZombies = new ArrayList<>();
        this.activePlants = new ArrayList<>();
        this.activeSuns = new ArrayList<>();
        this.activeProjectiles = new ArrayList<>();
        this.activeCollectables = new ArrayList<>();
        this.activeObstacles = new ArrayList<>();
        this.activeTargets = new ArrayList<>();
        this.lawnMowerManager = new LawnMowerManager();
        this.sunsPool = new GenericObjectPool<>(Sun::new);
        currentSun = 50;
        this.state = GameState.PLAYING;
        this.plantLists = new ArrayList<>();
        this.levelSetup.groundSetup(this);
        this.plantFoods = App.getCurrentUser().getPlantFoods();
        App.getCurrentUser().setPlantFoods(0);
    }

    public GameWorld() {

    }

        public Plant getPlantAtPosition(float x, float y) {
            int col = (int)(x / App.getCellWidth());
            int row = (int)(y / App.getCellHeight());
            if (row >= 0 && row < rows && col >= 0 && col < cols) {
                if (Math.abs(x-grid[row][col].getX()) < App.getCellWidth()/4) {
                    return grid[row][col].getPlant();
                }
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
                float dist = Math.abs(p.getX() - x);
                if (dist < minDist) {
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

    public void createGrave(int x, int y) {
        int col = (int) (x / App.getCellWidth());
        int row = (int) (y / App.getCellHeight());
        if (row < 0 || row >= rows || col < 0 || col >= cols) return;
        Cell cell = grid[row][col];
        if (cell.hasObstacle() || !cell.isEmpty()) {
            GameMenuController.updateState("Cannot place grave at (" + col + ", " + row + ") - cell not empty.");
            return;
        }
        float graveX = cell.getX();
        float graveY = cell.getY();
        Grave grave = new Grave(graveX, graveY, row, col, Grave.GraveType.NORMAL);
        cell.setObstacle(grave);
        cell.setPlantable(false);
        activeObstacles.add(grave);
        GameMenuController.updateState("A grave has been created at (" + col + ", " + row + ")");
    }

    public Cell getRandomEmptyCellInRow(int row) {
        if (row < 0 || row >= rows) return null;
        List<Cell> emptyCells = new ArrayList<>();
        for (int c = 0; c < cols; c++) {
            Cell cell = grid[row][c];
            if (cell.isEmpty() && !cell.hasObstacle()) {
                emptyCells.add(cell);
            }
        }
        if (emptyCells.isEmpty()) return null;
        Random rand = new Random();
        return emptyCells.get(rand.nextInt(emptyCells.size()));
    }

    public Cell getRandomEmptyCellInRowAfterColumn(int row, float zombieX) {
        if (row < 0 || row >= rows) return null;
        int minCol = (int) (zombieX / App.getCellWidth()) + 1; // ستون جلوی زامبی
        List<Cell> emptyCells = new ArrayList<>();
        for (int c = minCol; c < cols; c++) {
            Cell cell = grid[row][c];
            if (cell.isEmpty() && !cell.hasObstacle()) {
                emptyCells.add(cell);
            }
        }
        if (emptyCells.isEmpty()) return null;
        Random rand = new Random();
        return emptyCells.get(rand.nextInt(emptyCells.size()));
    }

    public int stealSunFromPlayer(int amount) {
        int stolen = Math.min(amount, currentSun);
        currentSun -= stolen;
        return stolen;
    }
    public void addSunToPlayer(int amount) {
        currentSun += amount;
    }

    public int collectSunInRadius(float x, float y, int radius) {
        int collected = 0;
        for (Sun sun : activeSuns) {
            if (sun.isCollected()) continue;
            float dx = sun.getX() - x;
            float dy = sun.getY() - y;
            if (dx*dx + dy*dy <= radius*radius) {
                sun.collect();
                collected += sun.getSize();
                currentSun += sun.getSize();
            }
        }
        return collected;
    }

    private void processZombieDeath(Zombie zombie) {
        this.notifyZombieKilled();
        User user = App.getCurrentUser();
        if (user == null) return;
        QuestStats stats = user.getQuestStats();
        stats.addZombiesKilledToday(1);
        stats.addTotalZombiesKilled(1);
        String chapter = currentChapter.name();
        stats.addZombiesKilledByChapter(chapter, 1);
        if (zombie.getKillerPlantType() != null) {
            stats.addZombiesKilledByPlant(zombie.getKillerPlantType(), 1);
            PlantFamily family = zombie.getKillerPlantType().family;
            stats.addZombieKilledByFamily(family);
        }
        NormalMechanic normal = getMechanic(NormalMechanic.class);
        if (normal != null && normal.getWaveManager() != null) {
            WaveManager wm = normal.getWaveManager();
            if (wm.getCurrentWaveIndex() == 0 && wm.getCurrentWave() != null) {
                if (!stats.isFirstWaveStarted()) {
                    stats.setFirstWaveStartTime(System.currentTimeMillis());
                }
                stats.incrementZombiesKilledInFirstWave();
            }
        }
        user.getQuestManager().checkAllQuests(user);
        processZombieDeathMu(zombie);
    }

    public void processZombieDeathMu(Zombie zombie){
        if (this.mupointManager != null) {
            int simultaneousKills = (int) activeZombies.stream().filter(Zombie::isDead).count();
            boolean isSplashDamage = false;
            if (zombie.getKillerPlantType() != null) {
                PlantType killer = zombie.getKillerPlantType();
                isSplashDamage = (killer == PlantType.CHERRY_BOMB ||
                        killer == PlantType.JALAPENO ||
                        killer == PlantType.POTATO_MINE);
            }
            KillEvent event = new KillEvent(
                    zombie,
                    zombie.getSpawnTick(),
                    this.currentTick,
                    simultaneousKills,
                    isSplashDamage,
                    zombie.hasEatenPlant()
            );
            this.mupointManager.onZombieDeath(event);
        }
    }


    private void cleanupDeadZombies() {
        Iterator<Zombie> zombieIterator = activeZombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            if (zombie.isDead()) {
                processZombieDeath(zombie);
                zombieIterator.remove();
            }
        }
    }

    public void initialize(){
        applyChapterRules();
    }

    protected abstract void applyChapterRules();

    public void tick() {
        if (state != GameState.PLAYING) return;
        currentTick++;
        activePlants.forEach(Plant::update);
        activeCollectables.forEach(Collectable::update);
        activeZombies.forEach(Zombie::update);
        activeProjectiles.forEach(Projectile::update);
        List<Zombie> zombieSnapshot = new ArrayList<>(activeZombies);
        zombieSnapshot.forEach(Zombie::update);
        List<Projectile> projectileSnapshot = new ArrayList<>(activeProjectiles);
        projectileSnapshot.forEach(Projectile::update);
        if (!isConveyorMode) {
            for (PlantCard card : plantLists) {
                card.update();
            }
        }
        for (Sun sun : activeSuns){
            if (sun.getProducer() == null && sun.isExpired()){
                sun.collect();
            }
        }
        activeSuns.removeIf(sun -> {
            if(sun.isCollected()){
                sunsPool.release(sun);
                return true;
            }
            return false;
        });
        lawnMowerManager.updateMowers(activeZombies);
        activeZombies.removeIf(zombie -> {
            if (zombie.isDead()) {
                this.notifyZombieKilled();
                return true;
            }
            return false;
        });
        activePlants.removeIf(Plant::isDead);
        activeCollectables.removeIf(Collectable::isDead);
        activeObstacles.removeIf(Obstacle::isDestroyed);
        activeProjectiles.removeIf(projectile -> {
            if(projectile.isDead()){
                projectilesPool.release(projectile);
                return true;
            }
            return false;
        });
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.hasObstacle() && cell.getObstacle() instanceof Grave grave) {
                    if (!grave.blocksProjectiles()) {
                        cell.setPlantable(true);
                        cell.removeObstacle();
                    }
                }
            }
        }
        cleanupDeadZombies();
        for(Mechanic mechanic : mechanics){
            mechanic.applyMechanic(this);
        }
        if(winCondition.checkWin(this)){
            state = GameState.WON;
            User user = App.getCurrentUser();
            if (user != null) {
                QuestStats stats = user.getQuestStats();
                stats.setLevelWon(true);
                stats.setFinalSunCount(this.currentSun);   //  کوئست 6
                boolean symmetric = isGardenSymmetricExceptMiddleRow();  // بررسی تقارن (کوئست ۹)
                stats.setSymmetryAchieved(symmetric);
                int difficulty = user.getGameDifficulty();  // کوئست ۱۳: برد با بیشترین سختی
                if (difficulty == 5) {
                    stats.incrementConsecutiveWinsMaxDifficulty();
                } else {
                    stats.resetConsecutiveWinsMaxDifficulty();
                }
                for (int c = 0; c < cols; c++) { //کوئست 17
                    boolean hasPlant = false;
                    for (int r = 0; r < rows; r++) {
                        if (!grid[r][c].isEmpty()) {
                            hasPlant = true;
                            break;
                        }
                    }
                    if (!hasPlant) {
                        stats.addEmptyColumnInLevel(c);
                    }
                }
                for (int r = 0; r < rows; r++) { // کوئست 18
                    boolean hasPlant = false;
                    for (int c = 0; c < cols; c++) {
                        if (!grid[r][c].isEmpty()) {
                            hasPlant = true;
                            break;}}
                    if (!hasPlant) {
                        stats.addEmptyRowInLevel(r);}}
                int minDim = Math.min(rows, cols); // کوئست ۱۹: صلیب بی دفاع
                for (int n = 0; n < minDim; n++) {
                    if (stats.getEmptyColumnsInLevel().contains(n) && stats.getEmptyRowsInLevel().contains(n)) {
                        stats.setEmptyColumnForCross(n);
                        stats.setEmptyRowForCross(n);
                        break; // فقط کوچکترین n را ثبت می‌کنیم
                    }
                }
                user.getQuestManager().checkAllQuests(user);
                user.getQuestStats().setLevelWon(true);
                GameMenuController.handleWinning(this, mupointManager);
            }
        }
        for(LoseCondition lose : loseConditions){
            if(lose.checkLose(this)){
                state = GameState.LOST;
                GameMenuController.handleLosing(this, mupointManager);
            }
        }
    }
//    public boolean isGardenSymmetric() {
//        if (grid == null || rows == 0 || cols == 0) return false;
//        int middleRow = rows / 2;
//        for (int r = 0; r < rows; r++) {
//            for (int c = 0; c < cols / 2; c++) {
//                Plant left = grid[r][c].getPlant();
//                Plant right = grid[r][cols - 1 - c].getPlant();
//                if (left == null && right == null) continue;
//                if (left == null || right == null) return false;
//                if (left.getType() != right.getType()) return false;
//            }
//        }
//        return true;
//    }

    public boolean isGardenSymmetricExceptMiddleRow() {
        if (grid == null || rows == 0 || cols == 0) return false;
        int middleRow = rows / 2;
        for (int r = 0; r < rows; r++) {
            if (r == middleRow) continue;  // ردیف وسط را نادیده می‌گیریم
            for (int c = 0; c < cols / 2; c++) {
                Plant left = grid[r][c].getPlant();
                Plant right = grid[r][cols - 1 - c].getPlant();
                if (left == null && right == null) continue;
                if (left == null || right == null) return false;
                if (left.getType() != right.getType()) return false;
            }
        }
        return true;
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
    public LawnMowerManager getLawnMowerManager() { return lawnMowerManager; }
    public int getCurrentTick() {
        return currentTick;
    }
    public void addZombie(Zombie zombie) { activeZombies.add(zombie); }
    public void addGrave(Grave grave) { activeObstacles.add(grave); }
    public void addTarget() {
        activeTargets.addAll(activeZombies);
        activeTargets.addAll(activeObstacles);
    }
    public GenericObjectPool<Projectile> getProjectilesPool() {
        return projectilesPool;
    }
    public void addMechanic(Mechanic mechanic){
        mechanics.add(mechanic);
    }
    public List<PlantCard> getConveyorBelt() {
        return conveyorBelt;
    }
    public List<PlantCard> getPlantLists() {
        return plantLists;
    }
    public void setPlantLists(List<PlantCard> plantLists) {
        this.plantLists = plantLists;
    }
    public <T extends Mechanic> T getMechanic(Class<T> type) {
        return mechanics.stream()
                .filter(m -> type.isInstance(m))
                .map(m -> type.cast(m))
                .findFirst()
                .orElse(null);
    }
    public void setPlantFoods(int plantFoods) {
        this.plantFoods = plantFoods;
    }
    public int getPlantFoods() {
        return plantFoods;
    }
    public ArrayList<LoseCondition> getLoseConditions() {
        return loseConditions;
    }
    public ArrayList<Mechanic> getMechanics() {
        return mechanics;
    }
    public boolean isConveyorMode() {
        return isConveyorMode;
    }
    public Cell getCellAt(float x, float y) {
        return Cell.findCell(x,y,grid);
    }
    public List<Cell> findTwoEmptyCell(boolean water){
        List<Cell> emptyCells = new ArrayList<>();
        for (Cell[] cells : grid){
            for (Cell cell : cells){
                if (cell.isEmpty() && cell.isPlantable() && !cell.hasObstacle()){
                    if (water==cell.getTerrain().isWater()) emptyCells.add(cell);
                }
            }
        }
        if (emptyCells.size() <= 2){
            return emptyCells;
        }
        Collections.shuffle(emptyCells);
        return new ArrayList<>(emptyCells.subList(0, 2));
    }
    public boolean isSandstormActive() {
        return sandstormActive;
    }
    public List<Collectable> getActiveCollectables() {
        return activeCollectables;
    }
    public void setSandstormActive(boolean sandstormActive) {
        this.sandstormActive = sandstormActive;
    }
    private boolean plantingPhase = false;
    public LevelSetup getLevelSetup() {
        return levelSetup;
    }
    public boolean isPlantingPhase() { return plantingPhase; }
    public void setPlantingPhase(boolean plantingPhase) { this.plantingPhase = plantingPhase; }
    public Chapter getCurrentChapter() {return currentChapter;}
    public void setCurrentChapter(Chapter chapter) {this.currentChapter = chapter;}
    public void setWillUnlockLevel(boolean willUnlockLevel) {
        this.willUnlockLevel = willUnlockLevel;
    }
    public boolean isWillUnlockLevel() { return willUnlockLevel;}
    public void setPlantSelected(boolean plantSelected) {isPlantSelected = plantSelected;}
    public boolean isPlantSelected() {return isPlantSelected;}

    public void setSelectedPlant(PlantType selectedPlant) {this.selectedPlant = selectedPlant;}

    public PlantType getSelectedPlant() {return selectedPlant;}

    public void addProjectile(Projectile projectile) {
        if (projectile != null) {
            this.activeProjectiles.add(projectile);
        }
    }
    public List<Obstacle> getActiveObstacles() {return activeObstacles;}
}