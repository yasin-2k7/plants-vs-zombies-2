package models.plant;

import controller.GameMenuController;
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
    private transient ArrayList<GameComponent> components = new ArrayList<>();
    private boolean dead = false;
    private boolean sheep = false;
    private boolean disabled = false;
    private int slowTicks = 0;
    private transient Cell cell = null;
    private int frozenAmount = 0;
    private boolean freeze = false;

    public void addComponent(GameComponent comp) {
        components.add(comp);
    }

    private void ensureComponentsInit() {
        if (components == null) {
            components = new ArrayList<>();
        }
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
            cell.findAndRemovePlant();
            App.getCurrentGame().notifyPlantEaten();
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

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void die() {
        this.dead = true;
        GameMenuController.updateState("Plant " + this.getType().name() + " at (" + this.x + ", " + this.y + ") is destroyed.");
    }
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

    public void increaseFrozenAmount(int amount){
        frozenAmount += amount;
        if (frozenAmount >= 100){
            frozenAmount = 100;
            freeze = true;
        }
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void unfreeze(){
        freeze = false;
    }

    public void activatePlantFood(){
        for (GameComponent component : components){
            component.activatePlantFood(this);
        }
    }

    public int getHealth() {
        return health;
    }

    public void destroy(){

    }

    public void initAfterLoad() {
        if (this.components == null) {
            this.components = new ArrayList<>();
        }
    }
}