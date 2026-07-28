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
import models.world.obstacles.OctopusObstacle;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;
import models.zombie.wave.WaveManager;

import java.util.*;

public abstract class GameWorld {
    private final List<LifespanComponent> smallShrooms = new ArrayList<>();
    private final List<Runnable> zombieKillListeners = new ArrayList<>();
    private final List<Runnable> plantEatenListeners = new ArrayList<>();
    protected Random random = new Random();
    protected int plantFoods;
    protected int rows;
    protected int cols;
    protected Cell[][] grid;
    protected List<Collectable> activeCollectables;
    protected List<Zombie> activeZombies;
    protected List<Plant> activePlants;
    protected List<Sun> activeSuns;
    protected List<Projectile> activeProjectiles;
    protected List<Obstacle> activeObstacles;
    protected LawnMowerManager lawnMowerManager;
    private GameState state;
    private int currentTick = 0;
    private Chapter currentChapter;
    private boolean willUnlockLevel = false;
    private int currentSun;
    private LevelSetup levelSetup;
    private ArrayList<LoseCondition> loseConditions;
    private WinCondition winCondition;
    private ArrayList<Mechanic> mechanics;
    private List<PlantCard> conveyorBelt;
    private List<PlantCard> plantLists;
    private boolean isConveyorMode;
    private GenericObjectPool<Sun> sunsPool = new GenericObjectPool<>(Sun::new);
    private GenericObjectPool<Projectile> projectilesPool = new GenericObjectPool<>(Projectile::new);
    private boolean sandstormActive = false;
    private MupointManager mupointManager;
    private boolean isPlantSelected = false;
    private PlantType selectedPlant = null;
    private boolean plantingPhase = false;
    private int currentBatchKills = 1;

    public GameWorld(LevelSetup levelSetup, ArrayList<LoseCondition> loseConditions,
                     WinCondition winCondition, ArrayList<Mechanic> mechanics) {
        App.getCurrentUser().setGamesPlayed(App.getCurrentUser().getGamesPlayed() + 1);
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

    public Plant getPlantAtPosition(float x, float y) {
        int col = (int) (x / App.getCellWidth());
        int row = (int) (y / App.getCellHeight());
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            if (Math.abs(x - grid[row][col].getX()) < App.getCellWidth() / 4) {
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
        if (this.mupointManager != null) {
            this.mupointManager.checkSunMilestones();
        }
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
        user.getQuestManager().checkAllQuests(user, false);
        processZombieDeathMu(zombie);
    }

    public void processZombieDeathMu(Zombie zombie) {
        if (this.mupointManager != null) {
            int simultaneousKills = this.currentBatchKills;
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
        int totalDead = (int) activeZombies.stream().filter(Zombie::isDead).count();
        boolean isFirst = true;
        Iterator<Zombie> zombieIterator = activeZombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            if (zombie.isDead()) {
                this.currentBatchKills = isFirst ? totalDead : 1;
                processZombieDeath(zombie);
                isFirst = false;
                zombieIterator.remove();
            }
        }
    }

    private void updateAll() {
        activePlants.forEach(Plant::update);
        activeCollectables.forEach(Collectable::update);
        List<Projectile> projectileSnapshot = new ArrayList<>(activeProjectiles);
        projectileSnapshot.forEach(Projectile::update);
        List<Zombie> zombieSnapshot = new ArrayList<>(activeZombies);
        zombieSnapshot.forEach(Zombie::update);
        if (!isConveyorMode) for (PlantCard card : plantLists) card.update();
        for (Sun sun : activeSuns) {
            if (sun.getProducer() == null && sun.isExpired()) {
                sun.collect();
            }
        }
        lawnMowerManager.updateMowers(activeZombies);
        for (Cell[] cells : grid){
            for (Cell cell : cells){
                cell.update();
            }
        }
    }

    private void removeIfDead() {
        activeSuns.removeIf(sun -> {
            if (sun.isCollected()) {
                sunsPool.release(sun);
                return true;
            }
            return false;
        });

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
            if (projectile.isDead()) {
                projectilesPool.release(projectile);
                return true;
            }
            return false;
        });
    }

    public void initialize() {
        applyChapterRules();
    }

    protected abstract void applyChapterRules();

    public void tick() {
        if (state != GameState.PLAYING) return;
        currentTick++;
        updateAll();
        cleanupDeadZombies();
        removeIfDead();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.hasObstacle()) {
                    Obstacle obs = cell.getObstacle();
                    if (obs instanceof Grave grave) {
                        if (!grave.blocksProjectiles()) {
                            grave.releaseContent();
                            cell.setPlantable(true);
                            cell.removeObstacle();
                        }
                    } else if (obs instanceof OctopusObstacle) {
                        if (cell.isEmpty() || !obs.blocksProjectiles()) {
                            cell.removeObstacle();
                        }
                    } else if (!obs.blocksProjectiles()) {
                        cell.removeObstacle();
                    }
                }
            }
        }
        for (Mechanic mechanic : mechanics) {
            mechanic.applyMechanic(this);
        }
        handleWinCondition();
        for (LoseCondition lose : loseConditions) {
            if (lose.checkLose(this)) {
                state = GameState.LOST;
                GameMenuController.handleLosing(this, mupointManager);
            }
        }
    }

    private void handleWinCondition() {
        if (!winCondition.checkWin(this)) return;
        state = GameState.WON;
        User user = App.getCurrentUser();
        if (user == null) return;
        QuestStats stats = user.getQuestStats();
        stats.setLevelWon(true);
        stats.setFinalSunCount(this.currentSun);
        boolean symmetric = isGardenSymmetricExceptMiddleRow();
        stats.setSymmetryAchieved(symmetric);
        int difficulty = user.getGameDifficulty();
        if (difficulty == 5) {
            stats.incrementConsecutiveWinsMaxDifficulty();
        } else {
            stats.resetConsecutiveWinsMaxDifficulty();
        }
        for (int c = 0; c < cols; c++) {
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
        for (int r = 0; r < rows; r++) {
            boolean hasPlant = false;
            for (int c = 0; c < cols; c++) {
                if (!grid[r][c].isEmpty()) {
                    hasPlant = true;
                    break;
                }
            }
            if (!hasPlant) {
                stats.addEmptyRowInLevel(r);
            }
        }
        int minDim = Math.min(rows, cols);
        for (int n = 0; n < minDim; n++) {
            if (stats.getEmptyColumnsInLevel().contains(n) && stats.getEmptyRowsInLevel().contains(n)) {
                stats.setEmptyColumnForCross(n);
                stats.setEmptyRowForCross(n);
                break;
            }
        }
        user.getQuestManager().checkAllQuests(user, true);
        user.getQuestStats().setLevelWon(true);
        GameMenuController.handleWinning(this);
    }

    public boolean isGardenSymmetricExceptMiddleRow() {
        if (grid == null || rows == 0 || cols == 0) return false;
        int middleRow = rows / 2;
        for (int r = 0; r < rows; r++) {
            if (r == middleRow) continue;
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

    public int getSun() {
        return currentSun;
    }

    public void setSun(int sun) {
        currentSun = sun;
        if (this.mupointManager != null) {
            this.mupointManager.checkSunMilestones();
        }
    }

    public List<Plant> getActivePlants() {
        return activePlants;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
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

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public List<Zombie> getActiveZombies() {
        return activeZombies;
    }

    public List<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    public LawnMowerManager getLawnMowerManager() {
        return lawnMowerManager;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void addZombie(Zombie zombie) {
        activeZombies.add(zombie);
    }

    public void addGrave(Grave grave) {
        activeObstacles.add(grave);
    }

    public GenericObjectPool<Projectile> getProjectilesPool() {
        return projectilesPool;
    }

    public void addMechanic(Mechanic mechanic) {
        mechanics.add(mechanic);
    }

    public List<PlantCard> getConveyorBelt() {
        return conveyorBelt;
    }

    public List<PlantCard> getPlantLists() {
        return plantLists;
    }

    public <T extends Mechanic> T getMechanic(Class<T> type) {
        return mechanics.stream()
                .filter(m -> type.isInstance(m))
                .map(m -> type.cast(m))
                .findFirst()
                .orElse(null);
    }

    public int getPlantFoods() {
        return plantFoods;
    }

    public void setPlantFoods(int plantFoods) {
        this.plantFoods = plantFoods;
    }

    public ArrayList<Mechanic> getMechanics() {
        return mechanics;
    }

    public boolean isConveyorMode() {
        return isConveyorMode;
    }

    public void setConveyorMode(boolean conveyorMode) {
        isConveyorMode = conveyorMode;
    }

    public Cell getCellAt(float x, float y) {
        return Cell.findCell(x, y, grid);
    }

    public List<Cell> findTwoEmptyCell(boolean water) {
        List<Cell> emptyCells = new ArrayList<>();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                if (cell.isEmpty() && cell.isPlantable() && !cell.hasObstacle()) {
                    if (water == cell.getTerrain().isWater()) emptyCells.add(cell);
                }
            }
        }
        if (emptyCells.size() <= 2) {
            return emptyCells;
        }
        Collections.shuffle(emptyCells);
        return new ArrayList<>(emptyCells.subList(0, 2));
    }

    public boolean isSandstormActive() {
        return sandstormActive;
    }

    public void setSandstormActive(boolean sandstormActive) {
        this.sandstormActive = sandstormActive;
    }

    public List<Collectable> getActiveCollectables() {
        return activeCollectables;
    }

    public LevelSetup getLevelSetup() {
        return levelSetup;
    }

    public void setPlantingPhase(boolean plantingPhase) {
        this.plantingPhase = plantingPhase;
    }

    public void setCurrentChapter(Chapter chapter) {
        this.currentChapter = chapter;
    }

    public boolean isWillUnlockLevel() {
        return willUnlockLevel;
    }

    public void setWillUnlockLevel(boolean willUnlockLevel) {
        this.willUnlockLevel = willUnlockLevel;
    }

    public boolean isPlantSelected() {
        return isPlantSelected;
    }

    public void setPlantSelected(boolean plantSelected) {
        isPlantSelected = plantSelected;
    }

    public PlantType getSelectedPlant() {
        return selectedPlant;
    }

    public void setSelectedPlant(PlantType selectedPlant) {
        this.selectedPlant = selectedPlant;
    }

    public void addProjectile(Projectile projectile) {
        if (projectile != null) {
            this.activeProjectiles.add(projectile);
        }
    }

    public List<Obstacle> getActiveObstacles() {
        return activeObstacles;
    }
    public WaveManager getWaveManager() {
        NormalMechanic normal = getMechanic(NormalMechanic.class);
        if (normal != null) {
            return normal.getWaveManager();
        }
        return null;
    }
    public <T extends LoseCondition> T getLoseCondition(Class<T> type) {
        if (loseConditions == null) return null;
        return loseConditions.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElse(null);
    }
}