package models.plant;

import models.enums.PlantType;

import java.util.ArrayList;

public class Plant {
    private PlantType type;
    private int health;
    private int x, y;
    private int damage;
    private int sunCost;
    private ArrayList<GameComponent> components = new ArrayList<>();
    private boolean dead = false;
    private boolean sheep = false;
    private boolean disabled = false;
    private int slowTicks = 0;

    public void addComponent(GameComponent comp) {
        components.add(comp);
    }

    public void update() {
        for (GameComponent comp : components) {
            comp.update(this);
        }
    }

    public Plant(PlantType type) {
        this.type = type;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) die();
    }

    public void die() { this.dead = true; }
    public boolean isDead() { return dead; }
    public void setSheep(boolean sheep) { this.sheep = sheep; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }
    public void applySlow(int ticks) { this.slowTicks = ticks; }
    public int getX() { return x; }
    public int getY() { return y; }
    public PlantType getType() { return type; }
    public void setX(int x) { this.x = x; }
}