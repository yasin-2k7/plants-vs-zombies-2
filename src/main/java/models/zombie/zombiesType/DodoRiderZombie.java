package models.zombie.zombiesType;

import models.core.App;
import models.enums.PlantType;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;

import java.util.HashSet;
import java.util.Set;

public class DodoRiderZombie extends Zombie {
    private boolean isRiding;
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

        Cell currentCell = Cell.findZombieCell(game.getGrid(), this);

        if (isRiding && currentCell != null) {
            // گیاه در سلول جلویی (سمت چپ زامبی)
            Cell frontCell = Cell.nextCell(currentCell, game.getGrid());
            if (frontCell != null) {
                Plant obstacle = frontCell.getPlant();
                if (obstacle != null && !obstacle.isDead()) {
                    handleObstacle(obstacle, frontCell);
                }
            }

            // بررسی جابه‌جایی سطر (slipping)
            Cell prevCell = Cell.previousCell(currentCell, game.getGrid());
            if (prevCell != null && prevCell.getSlippingDir() != 0) {
                this.x -= App.getCellWidth(); // یک سلول کامل جلوتر
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
                Cell nextCell = Cell.nextCell(cell, game.getGrid());
                if (nextCell != null) {
                    this.x = nextCell.getX();
                    this.y = nextCell.getY();
                    System.out.println("Dodo Rider flew over a " + type.name() + " at (" + cell.getX() + ", " + cell.getY() + ")");
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

        // اگر جان به نصف رسید و هنوز سوار است، پرنده از بین می‌رود
        if (!isDead && isRiding && this.health <= this.maxHealth / 2) {
            isRiding = false;
            this.speed = this.originalSpeed * 0.6; // کندتر می‌شود
            System.out.println("Dodo Rider lost its mount!");
        }
    }

    public boolean isRiding() {
        return isRiding;
    }
}