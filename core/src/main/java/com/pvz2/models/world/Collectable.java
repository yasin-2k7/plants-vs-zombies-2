package com.pvz2.models.world;

import com.pvz2.models.enums.CollectableType;

public class Collectable {
    private float x, y;
    private CollectableType type;
    private int lifeTime = 70;
    private int currentTime = 0;
    private boolean dead = false;

    public Collectable(float x, float y, CollectableType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        currentTime++;
        if (currentTime >= lifeTime) {
            dead = true;
        }
    }

    public void collect() {
        dead = true;
    }


    public boolean isDead() {
        return dead;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public CollectableType getType() {
        return type;
    }
}
