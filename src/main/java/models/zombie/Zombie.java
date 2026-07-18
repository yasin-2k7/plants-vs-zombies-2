package models.zombie;

import controller.GameMenuController;
import models.Damageable;
import models.enums.PlantType;
import models.core.App;
import models.enums.Zombies;
import models.world.Cell;
import models.world.ChapterWorld.FrostbiteCavesWorld;
import models.zombie.state.WalkingState;
import models.zombie.state.ZombieState;

import java.util.ArrayList;
import java.util.List;

public abstract class Zombie implements Damageable {
    protected Zombies name;
    protected String specificName; // متغیر جدید برای نگهداری نام دقیق زامبی
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

    private PlantType killerPlantType;


    public Zombie(Zombies name, int health, double speed, int damage) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed*15;
        this.damage = damage/10;
        this.currentState = new WalkingState();
    }

    public void update() {
        if (isDead || health <= 0 || iceHealth > 0) return;
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
            GameMenuController.updateState("Zombie of type " + name.name() + " is dead at (" + (int)x + ", " + (int)y + ")");
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
}