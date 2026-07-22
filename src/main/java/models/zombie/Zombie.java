package models.zombie;

import controller.GameMenuController;
import models.Damageable;
import models.core.User;
import models.enums.CollectableType;
import models.enums.PlantType;
import models.core.App;
import models.enums.Zombies;
import models.world.Cell;
import models.world.ChapterWorld.FrostbiteCavesWorld;
import models.world.Collectable;
import models.world.GameWorld;
import models.zombie.state.WalkingState;
import models.zombie.state.ZombieState;
import view.terminalView.GameMenuView;

import java.util.ArrayList;
import java.util.List;

public abstract class Zombie implements Damageable {
    protected Zombies name;
    protected String specificName;
    protected int health;
    protected int maxHealth;
    protected double speed;
    protected double originalSpeed = speed; // سرعت اصلی برای بازگردانی پس از کندی
    protected int damage;
    protected boolean isDead = false;
    protected float x, y;
    protected ZombieState currentState;
    private int slowTicksRemaining = 0;          // تعداد تیک‌های باقی‌مانده از کندی
    private double slowFactor = 0.5;            // ضریب کندی
    private int disabledTicksRemaining;
    private int freezedTicksRemaining;
    protected List<String> armorTypes = new ArrayList<>();
    private int iceHealth = 0;
    private boolean dropsReward;      // آیا این زامبی جایزه دارد؟
    private boolean glowing = false;

    private PlantType killerPlantType;

    private long spawnTick;
    private boolean hasEatenPlant = false;
    private GameWorld world;


    public Zombie(Zombies name, int health, double speed, int damage) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed*15;
        this.damage = damage/10;
        this.currentState = new WalkingState();
    }

    public Zombie(Zombies name, int health, double speed, int damage, GameWorld world) {
        this.world = world;
        this.spawnTick = world.getCurrentTick();

        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed*10;
        this.damage = damage/10;
        this.currentState = new WalkingState();
    }

    public void update() {
        if (isDead || health <= 0) return;
        if (slowTicksRemaining > 0) {
            slowTicksRemaining--;
            if (slowTicksRemaining == 0) {
                this.speed = originalSpeed;
            }
        }

        if (disabledTicksRemaining > 0) {
            disabledTicksRemaining--;
            return;
        }
        if (freezedTicksRemaining > 0) {
            freezedTicksRemaining--;
            if (freezedTicksRemaining == 0) applySlow(20, 0.5, true);
            return;
        }
        Cell currentCell = Cell.findZombieCell(App.getCurrentGame().getGrid(), this);
        if (currentCell != null && currentCell.getSlippingDir() != 0){
            y += App.getCellHeight() * currentCell.getSlippingDir();
        }
        if (currentState != null) {
            currentState.handleAction(this);
        } else {
            currentState = new WalkingState();
        }
    }

    public void move() {
        this.x -= this.speed; // حرکت به چپ
    }

        @Override
        public void takeDamage(int amount, String damageType) {
            if (isDead) return;
            if (iceHealth > 0){
                iceHealth -= damage;
                if (iceHealth <= 0){
                    unfreeze();
                }
                return;
            }
            this.health -= amount;
            if (this.health <= 0) {
                die();
            }
        }

    public void unfreeze() {
        iceHealth = 0;
        freezedTicksRemaining = 0;
        slowTicksRemaining = 0;
        this.speed = originalSpeed;
    }

    public void die() {
        if (isDead) return;
        this.isDead = true;

        GameWorld world = App.getCurrentGame();
        if (world == null) return;

        if (glowing) {
            Collectable plantFood = new Collectable(this.x, this.y, CollectableType.PLANT_FOOD);
            world.getActiveCollectables().add(plantFood);
            GameMenuController.updateState("\uD83C\uDFC6The glowing zombie dropped a plant food at (" + (int)x + ", " + (int)y + ")");
        }

        if (Math.random() < 0.10) {
            CollectableType type;
            if (Math.random() < 0.33) {
                type = CollectableType.COIN;
            } else if (Math.random() < 0.5){
                type = CollectableType.POT;
            } else {
                type = CollectableType.DIAMOND;
            }
            Collectable drop = new Collectable(this.x, this.y, type);
            world.getActiveCollectables().add(drop);
            GameMenuController.updateState("\uD83C\uDFC6A zombie dropped a " + type.name().toLowerCase() + " at (" + (int)x + ", " + (int)y + ")");
        }

        GameMenuController.updateState("\uD83D\uDC80Zombie of type " + name.name() + " is dead at (" + (int)x + ", " + (int)y + ")");
    }

    public void applySlow(int ticks, double factor, boolean canWorkInFrostbite) {
        if (!canWorkInFrostbite && App.getCurrentGame() instanceof FrostbiteCavesWorld){
            return;
        }
        if (ticks <= 0) return;
        if (slowTicksRemaining == 0) {
            this.originalSpeed = this.speed;
        }
        if (factor < slowFactor) {
            slowFactor = factor;
            this.speed = (originalSpeed * slowFactor);
        }
        if (ticks > slowTicksRemaining) {
            slowTicksRemaining = ticks;
        }
    }

    public void disableFor(int ticks) {
        disabledTicksRemaining = ticks;
    }

    public void freeze(int ticks){
        freezedTicksRemaining = ticks;
    }

    public boolean isSlowed() {
        return slowTicksRemaining > 0;
    }
    public void resetSpeed() {
        this.speed = originalSpeed;
        this.slowTicksRemaining = 0;
        this.slowFactor = 1.0;
    }

    public boolean isBoss() {return false;}
    public boolean isDead() { return isDead; }
    public ZombieState getCurrentState() { return currentState; }
    public void setState(ZombieState state) { this.currentState = state; }
    @Override
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    @Override
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed;
        if (slowTicksRemaining == 0) {
            this.originalSpeed = speed;
        }}

    public int getSlowTicksRemaining() {
        return slowTicksRemaining;
    }

    public int getDisabledTicksRemaining() {
        return disabledTicksRemaining;
    }

    public int getFreezedTicksRemaining() {
        return freezedTicksRemaining;
    }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public Zombies getName() { return name; }

    public String getSpecificName() { return specificName; }
    public void setSpecificName(String specificName) { this.specificName = specificName; }

    public int getDamage() {return damage;}

    public void setKiller(PlantType killer) {
        this.killerPlantType = killer;
    }

    public PlantType getKillerPlantType() {
        return killerPlantType;
    }

    public int getIceHealth() {
        return iceHealth;
    }

    public void setIceHealth(int iceHealth) {
        this.iceHealth = iceHealth;
    }

    public void addArmorType(String type) {
        this.armorTypes.add(type);
    }

    public List<String> getArmorTypes() {
        return armorTypes;
    }

    public boolean isPusher() {return false;}

    public void setDropsReward(boolean dropsReward) {this.dropsReward = dropsReward;}

    public boolean isDropsReward() {return dropsReward;}


    public long getSpawnTick() {
        return spawnTick;
    }

    public void setSpawnTick(long spawnTick) {
        this.spawnTick = spawnTick;
    }

    public boolean hasEatenPlant() {
        return hasEatenPlant;
    }

    public void setHasEatenPlant(boolean hasEatenPlant) {
        this.hasEatenPlant = hasEatenPlant;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public GameWorld getWorld() {
        return world;
    }

    public void setGlowing(boolean glowing) {this.glowing = glowing;}

    public boolean isGlowing() {return glowing;}
}