package models.plant;

import controller.GameMenuController;
import models.Damageable;
import models.core.App;
import models.core.User;
import models.enums.PlantType;
import models.plant.components.SunProducerComponent;
import models.world.Cell;
import models.world.obstacles.IceBlock;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

public class Plant implements Damageable {
    private PlantType type;
    private int health;
    private int initHealth;
    private int x, y;
    private int damage;
    private transient ArrayList<GameComponent> components = new ArrayList<>();
    private boolean dead = false;
    private boolean cat = false;
    private boolean disabled = false;
    private transient Cell cell = null;
    private int frozenAmount = 0;
    private boolean freeze = false;
    private int iceHealth = 0;
    private boolean isFire = false;
    private boolean plantFoodInStart = false;

    public Plant(PlantType type, int health, int damage) {
        this.type = type;
        this.health = health;
        this.damage = damage;
        initHealth = health;
    }

    public static boolean isMushroom(PlantType type) {
        return switch (type) {
            case SUN_SHROOM, PUFF_SHROOM, FUME_SHROOM, SEA_SHROOM,
                 ICE_SHROOM, DOOM_SHROOM, MAGNET_SHROOM, HYPNO_SHROOM -> true;
            default -> false;
        };
    }

    public void addComponent(GameComponent comp) {
        components.add(comp);
    }

    public void update() {
        if (disabled || freeze || cat) return;
        if (plantFoodInStart) {
            activatePlantFood();
            plantFoodInStart = false;
        }
        for (GameComponent comp : components) {
            if (type == PlantType.SUN_BEAN && comp instanceof SunProducerComponent) {
                continue;
            }
            comp.update(this);
        }
        if (isFire) {
            checkFire();
        }
    }

    private void checkFire() {
        List<Cell> neighborCells = Cell.getNeighborCells(cell, App.getCurrentGame().getGrid(), 1);
        for (Cell cell1 : neighborCells) {
            if (cell1.getPlant().freeze) {
                cell1.getPlant().iceHealth -= 6;
                if (cell1.getPlant().iceHealth <= 0) {
                    cell1.getPlant().unfreeze();
                }
            }
            if (cell1.hasObstacle() && cell1.getObstacle() instanceof IceBlock iceBlock) {
                iceBlock.takeDamage(6, "NORMAL");
            }
            for (Zombie zombie : Cell.getZombiesInCell(cell)) {
                if (zombie.getIceHealth() > 0) {
                    zombie.setIceHealth(zombie.getIceHealth() - 6);
                }
            }
        }
    }

    public void takeDamage(int damage) {
        takeDamage(damage, (Zombie) null);
    }

    @Override
    public void takeDamage(int damage, String damageType) {
    }

    @Override
    public void takeDamage(int damage, Zombie zombie) {
        if (iceHealth > 0) {
            iceHealth -= damage;
            if (iceHealth <= 0) {
                unfreeze();
            }
            return;
        }

        int remainingDamage = damage;
        for (GameComponent comp : components) {
            remainingDamage = comp.onTakeDamage(this, remainingDamage, zombie);
            if (remainingDamage <= 0) {
                break;
            }
        }

        this.health -= remainingDamage;

        if (health <= 0) {
            User user = App.getCurrentUser();
            if (user != null) {
                user.getQuestStats().incrementPlantsLost();
            }
            App.getCurrentGame().notifyPlantEaten();
            die();
        }

    }

    public void die() {
        if (this.dead) return;

        this.dead = true;
        for (GameComponent component : components) {
            component.onDeath(this);
        }

        if (this.cell != null) {
            this.cell.findAndRemovePlant();
            this.cell = null;
        }
        GameMenuController.updateState("Plant " + this.getType().name() + " at (" + this.x + ", " + this.y + ") is destroyed.");
    }

    public boolean isDead() {
        return dead;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PlantType getType() {
        return type;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public <T extends GameComponent> T getComponent(Class<T> componentClass) {
        for (GameComponent component : components) {
            if (componentClass.isInstance(component)) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public void increaseFrozenAmount() {
        if (frozenAmount == 99 || isFire) return;
        frozenAmount += 33;
        if (frozenAmount >= 99) {
            frozenAmount = 0;
            freeze = true;
            iceHealth = 600;
        }
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void unfreeze() {
        freeze = false;
        iceHealth = 0;
    }

    public void activatePlantFood() {
        for (GameComponent component : components) {
            component.activatePlantFood(this);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void destroy() {

    }

    public void initAfterLoad() {
        if (this.components == null) {
            this.components = new ArrayList<>();
        }
    }

    public void setFire(boolean fire) {
        isFire = fire;
    }

    public int getInitHealth() {
        return initHealth;
    }

    public void setPlantFoodInStart(boolean plantFoodInStart) {
        this.plantFoodInStart = plantFoodInStart;
    }

    public boolean isCat() {
        return cat;
    }

    public void setCat(boolean cat) {
        this.cat = cat;
    }
}