package models.zombie;

import models.enums.Zombies;
import models.zombie.state.WalkingState;
import models.zombie.state.ZombieState;

public abstract class Zombie {
    protected Zombies name;
    protected int health;
    protected int maxHealth;
    protected int speed;
    protected int originalSpeed; // سرعت اصلی برای بازگردانی پس از کندی
    protected int damage;
    protected boolean isDead = false;
    protected float x, y;
    protected ZombieState currentState;
    private boolean isDisabled = false;
    private int slowTicksRemaining = 0;          // تعداد تیک‌های باقی‌مانده از کندی
    private double slowFactor = 0.5;             // ضریب کندی

    public Zombie(Zombies name, int health, int speed, int damage) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.damage = damage;
        this.currentState = new WalkingState();
    }

    public void update() {
        if (isDead) return;
        if (slowTicksRemaining > 0) {
            slowTicksRemaining--;
            if (slowTicksRemaining == 0) {
                this.speed = originalSpeed;
            }
        }

        if (isDisabled) {
            return;
        }
        if (currentState != null) {
            currentState.handleAction(this);
        } else {
            move();
        }
    }

    public void move() {
        this.x -= this.speed; //حرکت به چپ
    }

    public void takeDamage(int amount, String damageType) {
        if (isDead) return;
        this.health -= amount;
        if (this.health <= 0) {
            die();
        }
    }

    public void die() {
        if (isDead) return;
        this.isDead = true;
        // چاپ پیام مرگ
        System.out.println("Zombie of type " + name.name() + " is isDead at (" + (int)x + ", " + (int)y + ")");
    }

    public void applySlow(int ticks, double factor) {
        if (ticks <= 0) return;
        if (slowTicksRemaining == 0) {
            this.originalSpeed = this.speed;
        }
        if (factor < slowFactor) {
            slowFactor = factor;
            this.speed = (int) (originalSpeed * slowFactor);
        }
        if (ticks > slowTicksRemaining) {
            slowTicksRemaining = ticks;
        }
    }

    public void disableFor(int ticks) {
        if (ticks <= 0) {
            isDisabled = false;
            return;
        }
        isDisabled = true;
    }

    public void enable() {
        this.isDisabled = false;
    }
    public boolean isDisabled() {
        return isDisabled;
    }
    public boolean isSlowed() {
        return slowTicksRemaining > 0;
    }
    public void resetSpeed() {
        this.speed = originalSpeed;
        this.slowTicksRemaining = 0;
        this.slowFactor = 1.0;
    }

    public boolean isDead() { return isDead; }
    public ZombieState getCurrentState() { return currentState; }
    public void setState(ZombieState state) { this.currentState = state; }
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed;
        if (slowTicksRemaining == 0) {
            this.originalSpeed = speed;
        }}
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public Zombies getName() { return name; }
    public int getDamage() {return damage;}
}