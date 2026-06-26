package models.plant;

import models.core.App;
import models.enums.PlantType;
import models.plant.components.PlacementBehaviorComponent;
import models.plant.components.SunProducerComponent;
import models.world.Cell;

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
    private Cell cell = null;

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
            die();
            App.getCurrentGame().getActivePlants().remove(this);
        }
    }

    public Plant(PlantType type, int health, int damage) {
        this.type = type;
        this.health = health;
        this.damage = damage;
    }

    public void setY(int y) {
        this.y = y;
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

    public Cell getCell() {
        return cell;
    }

    public int getDamage() {
        return damage;
    }

    public <T extends GameComponent> T getComponent(Class<T> componentClass) {
        for (GameComponent component : components) {
            if (componentClass.isInstance(component)) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public void activatePlantFood(){

    }

    public void destroy(){

    }
}