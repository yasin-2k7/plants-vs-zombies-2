package models.world;

import models.enums.PlantType;
import models.miniGame.MechanicsStrategy;
import models.plant.Plant;
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
       // if (activeZombies == null) return;
        //for (int i = activeZombies.size() - 1; i >= 0; i--) {
            //models.zombie.Zombie zombie = activeZombies.get(i);
           // zombie.update();
           // if (zombie.isDead()) {
             //   activeZombies.remove(i);
           // }
        //}
    }

    public List<Zombie> getActiveZombies() {
        return activeZombies;
    }

    public int getCols() { return cols; }
    public void addZombie(Zombie zombie) { activeZombies.add(zombie); }
}