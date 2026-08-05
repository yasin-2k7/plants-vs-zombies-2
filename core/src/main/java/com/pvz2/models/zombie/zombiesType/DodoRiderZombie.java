package com.pvz2.models.zombie.zombiesType;

import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.enums.Zombies;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.Zombie;

import java.util.HashSet;
import java.util.Set;

public class DodoRiderZombie extends Zombie {
    private static final Set<PlantType> FLY_OVER_PLANTS = new HashSet<>();

    static {
        FLY_OVER_PLANTS.add(PlantType.WALL_NUT);
        FLY_OVER_PLANTS.add(PlantType.POTATO_MINE);
        FLY_OVER_PLANTS.add(PlantType.GARLIC);
        FLY_OVER_PLANTS.add(PlantType.SQUASH);
        FLY_OVER_PLANTS.add(PlantType.CHERRY_BOMB);
        FLY_OVER_PLANTS.add(PlantType.JALAPENO);
        FLY_OVER_PLANTS.add(PlantType.PRIMAL_POTATO_MINE);
        FLY_OVER_PLANTS.add(PlantType.EXPLODE_O_NUT);
        FLY_OVER_PLANTS.add(PlantType.GRAPESHOT);
        FLY_OVER_PLANTS.add(PlantType.DOOM_SHROOM);
        FLY_OVER_PLANTS.add(PlantType.TANGLE_KELP);
        FLY_OVER_PLANTS.add(PlantType.ICE_SHROOM);
    }

    private boolean isRiding;

    public DodoRiderZombie(int health, double speed, int damage) {
        super(Zombies.DODO_RIDER, health, speed, damage);
        this.isRiding = true;
    }

    @Override
    public void update() {
        if (isDead) return;

        GameWorld game = App.getCurrentGame();
        if (game == null) {
            super.update();
            return;
        }

        int col = (int) (this.x / App.getCellWidth());
        int row = (int) (this.y / App.getCellHeight());
        if (row < 0 || row >= game.getRows() || col < 0 || col >= game.getCols()) {
            super.update();
            return;
        }

        if (isRiding) {
            if (col > 0) {
                Cell frontCell = game.getGrid()[row][col - 1];
                if (frontCell != null) {
                    Plant obstacle = frontCell.getPlant();
                    if (obstacle != null && !obstacle.isDead()) {
                        handleObstacle(obstacle, frontCell);
                    }
                }
            }

            if (col > 0 && game.getGrid()[row][col - 1].getSlippingDir() != 0) {
                this.x -= App.getCellWidth();
            }
        }

        super.update();
    }

    private void handleObstacle(Plant plant, Cell cell) {
        PlantType type = plant.getType();

        if (type == PlantType.TALL_NUT) {
            return;
        }

        if (FLY_OVER_PLANTS.contains(type)) {
            GameWorld game = App.getCurrentGame();
            if (game != null) {
                int col = cell.getCol() - 1;
                int row = cell.getRow();
                if (col >= 0) {
                    Cell nextCell = game.getGrid()[row][col];
                    this.x = nextCell.getX();
                    this.y = nextCell.getY();
                    GameMenuController.updateState(
                            "Dodo Rider flew over a " + type.name() + " at (" + cell.getX() + "," + cell.getY() + ")");
                } else {
                    this.x -= App.getCellWidth();
                }
            }
        }
    }

    @Override
    public void takeDamage(int amount, String damageType) {
        if (isDead) return;

        super.takeDamage(amount, damageType);

        if (!isDead && isRiding && this.health <= this.maxHealth / 2) {
            isRiding = false;
            this.speed = this.originalSpeed * 0.6;
            GameMenuController.updateState("Dodo Rider lost its mount!");
        }
    }
}
