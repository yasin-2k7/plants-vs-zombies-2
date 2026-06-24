package models.plant;

import models.core.App;
import models.enums.PlantType;
import models.plant.components.SunProducerComponent;

import java.util.ArrayList;

public class Plant {
    private PlantType type;
    private int health;
    private int x, y;
    private int damage;
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
            if (type == PlantType.SUN_BEAN && comp instanceof SunProducerComponent){
                continue;
            }
            comp.update(this);
        }
    }

    public void takeDamage(int damage){
        health -= damage;
        if (type.equals(PlantType.GOLD_BLOOM)){
            components.getLast().update(this);
        }
        if (health <= 0){
            App.getCurrentGame().getActivePlants().remove(this);
        }
    }

    public Plant(PlantType type, int health, int x, int y, int damage) {
        this.type = type;
        this.health = health;
        this.x = x;
        this.y = y;
        this.damage = damage;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PlantType getType() {
        return type;
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