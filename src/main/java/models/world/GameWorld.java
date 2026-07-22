package models.world;

import controller.GameMenuController;
import models.Damageable;
import models.core.App;
import models.core.User;
import models.core.UserDataManager;
import models.enums.Chapter;
import models.enums.PlantFamily;
import models.enums.PlantType;
import models.lawnMower.LawnMower;
import models.lawnMower.LawnMowerManager;
//import models.miniGame.MechanicsStrategy;
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
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;
import models.zombie.wave.WaveManager;

import java.util.*;

public abstract class GameWorld {
    private long startTime;
    private long currentTime;

    Random random = new Random();

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

    protected List<Zombie> activeZombies;
    protected List<Plant> activePlants;
    protected List<Sun> activeSuns;
    protected List<Projectile> activeProjectiles;
    protected List<Grave> activeGrave;
    protected List<Damageable> activeTargets;
    protected LawnMowerManager lawnMowerManager;

    private GenericObjectPool<Sun> sunsPool = new GenericObjectPool<>(Sun::new);
    private GenericObjectPool<Projectile> projectilesPool = new GenericObjectPool<>(Projectile::new);

    private final List<LifespanComponent> smallShrooms = new ArrayList<>();

    private boolean sandstormActive = false;

    private final List<Runnable> zombieKillListeners = new ArrayList<>();
    private final List<Runnable> plantEatenListeners = new ArrayList<>();

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
        this.activeGrave = new ArrayList<>();
        this.activeTargets = new ArrayList<>();
        this.lawnMowerManager = new LawnMowerManager();
        this.sunsPool = new GenericObjectPool<>(Sun::new);
        currentSun = 50;

        this.startTime = System.currentTimeMillis();
        this.state = GameState.PLAYING;
        this.plantLists = new ArrayList<>();

        this.levelSetup.groundSetup(this);
        this.plantFoods = App.getCurrentUser().getPlantFoods();
        applyChapterRules();
        App.getCurrentUser().setPlantFoods(0);
    }

    public GameWorld() {

    }

    // متد پیدا کردن گیاه بر اساس مختصات حرکتی زامبی
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

    private void processZombieDeath(Zombie zombie) {
        this.notifyZombieKilled();

        User user = App.getCurrentUser();
        if (user == null) return;

        QuestStats stats = user.getQuestStats();
        stats.addZombiesKilledToday(1);
        stats.addTotalZombiesKilled(1);

        String chapter = currentChapter.name();
        stats.addZombiesKilledByChapter(chapter, 1);

        // ثبت آمار بر اساس نوع گیاه کشنده
        if (zombie.getKillerPlantType() != null) {
            stats.addZombiesKilledByPlant(zombie.getKillerPlantType(), 1);
            PlantFamily family = zombie.getKillerPlantType().family;
            stats.addZombieKilledByFamily(family);
        }

        // ثبت آمار برای کوئست ۷ (سرعت عمل در موج اول)
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

        // بررسی نهایی تمام کوئست‌ها
        user.getQuestManager().checkAllQuests(user);
    }

    private void cleanupDeadZombies() {
        Iterator<Zombie> zombieIterator = activeZombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            if (zombie.isDead()) {
                processZombieDeath(zombie); // ۱. ثبت کامل آمار و کوئست‌ها
                zombieIterator.remove();    // ۲. حذف ایمن از لیست زامبی‌های فعال
            }
        }
    }



    protected abstract void applyChapterRules();

    public void tick(){

        if(state != GameState.PLAYING) return;

        currentTick++;


        activePlants.forEach(Plant::update);
        activeZombies.forEach(Zombie::update);
        activeProjectiles.forEach(Projectile::update);
        if (!isConveyorMode) {
            for (PlantCard card : plantLists){
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
        activeProjectiles.removeIf(Projectile::isDead);

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
                // بررسی تقارن (کوئست ۹)
                boolean symmetric = isGardenSymmetricExceptMiddleRow();
                stats.setSymmetryAchieved(symmetric);

                // کوئست ۱۳: برد با بیشترین سختی
                int difficulty = user.getGameDifficulty();
                if (difficulty == 5) {
                    stats.incrementConsecutiveWinsMaxDifficulty();
                } else {
                    stats.resetConsecutiveWinsMaxDifficulty();
                }

                // ---- کوئست ۱۷: ستون‌های خالی ----
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

                // ---- کوئست ۱۸: سطرهای خالی ----
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

                // کوئست ۱۹: صلیب بی دفاع
                // بررسی می‌کنیم که برای هر n (0 تا min(rows, cols)-1) ستون و ردیف n خالی است
                int minDim = Math.min(rows, cols);
                for (int n = 0; n < minDim; n++) {
                    if (stats.getEmptyColumnsInLevel().contains(n) && stats.getEmptyRowsInLevel().contains(n)) {
                        stats.setEmptyColumnForCross(n);
                        stats.setEmptyRowForCross(n);
                        break; // فقط کوچکترین n را ثبت می‌کنیم
                    }
                }

                user.getQuestManager().checkAllQuests(user);
                user.getQuestStats().setLevelWon(true);
                GameMenuController.handleWinning(this);
            }
        }

        for(LoseCondition lose : loseConditions){
            if(lose.checkLose(this)){
                state = GameState.LOST;
                GameMenuController.handleLosing(this);
            }
        }

    }

    public boolean isGardenSymmetric() {
        if (grid == null || rows == 0 || cols == 0) return false;
        int middleRow = rows / 2;
        for (int r = 0; r < rows; r++) {
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
        int col = (int) (x / 100);
        int row = (int) (y / 100);
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grid[row][col];
        }
        return null;
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

    public boolean isWillUnlockLevel() {
        return willUnlockLevel;
    }
}