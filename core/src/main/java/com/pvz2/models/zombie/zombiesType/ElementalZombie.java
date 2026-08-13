package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;

import java.util.Set;

public class ElementalZombie extends Zombie {
    private static final Set<String> FIRE_TYPES = Set.of(
            "FIRE_PEA",
            "PEPPER",
            "SPECIAL_PEPPER",
            "FIRE_PEASHOOTER",
            "FIRE"
    );
    private static final Set<String> ICE_TYPES = Set.of(
            "ICE_PEA",
            "SNOW_PEA",
            "ICE_MELON",
            "SPECIAL_ICE_MELON",
            "ICE_SHROOM",
            "ICE"
    );
    private boolean isExplorer;
    private boolean isIgnited;
    private int fuseTimer;
    private boolean hasExploded;

    public ElementalZombie(int health, double speed, int damage, boolean isExplorer) {
        super(Zombies.ELEMENTAL, health, speed, damage);
        this.isExplorer = isExplorer;
        this.isIgnited = true;
        this.fuseTimer = 150;
        this.hasExploded = false;
    }

    @Override
    public void update(float delta) {
        if (isDead) return;

        if (!isExplorer && isIgnited && !hasExploded) {
            fuseTimer--;
            if (fuseTimer <= 0) {
                explode();
            }
        }
        super.update(delta);

        if (isExplorer && isIgnited) {
            GameWorld game = App.getCurrentGame();
            if (game != null) {
                Cell currentCell = Cell.findZombieCell(game.getGrid(), this);
                if (currentCell != null) {
                    Cell frontCell = (this.speed > 0) ?
                            Cell.previousCell(currentCell, game.getGrid()) :
                            Cell.nextCell(currentCell, game.getGrid());
                    if (frontCell != null) {
                        Plant plant = frontCell.getPlant();
                        if (plant != null && !plant.isDead()) {
                            plant.die();
                            System.out.println("🔥Explorer burned plant at (" + plant.getX() +
                                    ", " + plant.getY() + ")");
                            GameMenuController.updateState("Explorer burned plant at (" + plant.getX() +
                                    ", " + plant.getY() + ")");
                        }
                    }
                }
            }
        }
    }

    private void explode() {
        this.hasExploded = true;
        this.isIgnited = false;
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        Cell currentCell = Cell.findZombieCell(game.getGrid(), this);
        if (currentCell != null) {
            this.y = currentCell.getY();
            Cell leftmostCell = game.getGrid()[currentCell.getRow()][0];
            this.x = leftmostCell.getX();
        } else {
            this.x = App.getCellWidth() / 2.0f;
        }

        this.speed = -Math.abs(this.speed);

        this.originalSpeed = -Math.abs(this.originalSpeed);

        System.out.println("🧨Prospector exploded and teleported to the left end of the row.");
//        int cols = game.getCols();
//        float newX = cols * App.getCellWidth() - App.getCellWidth() / 2;
//        this.x = newX;
//        this.speed = Math.abs(this.speed);
//        GameMenuController.updateState("Prospector exploded and teleported to the right end of the row.");
    }

    public void extinguish() {
        if (!isIgnited) return;
        this.isIgnited = false;
        System.out.println(isExplorer ? "Explorer's torch extinguished." : "Prospector's dynamite extinguished.");
        if (!isExplorer) {
            GameMenuController.updateState("Prospector's dynamite extinguished.");
        } else {
            GameMenuController.updateState("Explorer's torch extinguished.");
        }
    }

    public void ignite() {
        if (!isExplorer) return;
        if (isIgnited) return;
        this.isIgnited = true;
        System.out.println("🔥Explorer's torch ignited.");
        if (isExplorer) {
            this.isIgnited = true;
            GameMenuController.updateState("Explorer's torch ignited.");
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;

        if (damageType != null) {
            String upper = damageType.toUpperCase();
            if (ICE_TYPES.contains(upper) || upper.contains("ICE")) {
                extinguish();
            } else if (FIRE_TYPES.contains(upper) || upper.contains("FIRE")) {
                if (isExplorer) {
                    ignite();
                }
            }
        }

        super.takeDamage(amount, damageType);
    }

    @Override
    public void applySlow(float delta, double factor, boolean canWorkInFrostbite) {
        super.applySlow(delta, factor, canWorkInFrostbite);
        if (isExplorer && isIgnited) {
            extinguish();
        }
    }

    @Override
    public void move(float delta) {
        if (!isExplorer && hasExploded) {
            float newX = (float) (this.x - this.speed);

            GameWorld game = App.getCurrentGame();
            if (game != null) {
                float maxX = game.getCols() * App.getCellWidth();
                if (newX < 0) newX = 0;
                if (newX > maxX) newX = maxX;
            }
            this.x = newX;
        } else {
            super.move(delta);
        }
    }
}
