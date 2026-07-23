package models.world.obstacles;

import models.Damageable;

public abstract class Obstacle implements Damageable {
    protected float x, y;
    protected int health;
    protected boolean isDestroyed = false;

    public Obstacle(float x, float y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }


    @Override
    public void takeDamage(int amount, String type) {
        if (isDestroyed) return;

        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isDestroyed = true;
        }
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public boolean blocksProjectiles() {
        return !isDestroyed;
    }

    public void die() {
        this.isDestroyed = true;
    }
    public int getHealth() {return health;}
}
