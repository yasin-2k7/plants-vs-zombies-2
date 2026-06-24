package models.zombie;

import models.enums.Zombies;
import models.zombie.state.WalkingState;
import models.zombie.state.ZombieState;

public abstract class Zombie {
    protected Zombies name;
    protected int health;
    protected int maxHealth;
    protected int speed;
    protected int damage;
    protected boolean isDead = false;
    protected float x, y;
    protected ZombieState currentState;

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
            System.out.println("Zombie of type " + name.name() + " is dead at (" + (int)x + ", " + (int)y + ")");
        }

        public boolean isDead() { return isDead; }
        public ZombieState getCurrentState() { return currentState; }
        public void setState(ZombieState state) { this.currentState = state; }
        public float getX() { return x; }
        public void setX(float x) { this.x = x; }
        public float getY() { return y; }
        public void setY(float y) { this.y = y; }
        public int getSpeed() { return speed; }
        public void setSpeed(int speed) { this.speed = speed; }
        public int getHealth() { return health; }
        public void setHealth(int health) { this.health = health; }
        public Zombies getName() { return name; }
        public int getDamage() {return damage;}
}