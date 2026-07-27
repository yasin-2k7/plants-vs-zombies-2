package models.zombie.zombiesType;

import controller.GameMenuController;
import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.world.obstacles.Grave;
import models.world.obstacles.OctopusObstacle;
import models.zombie.Zombie;

import java.util.List;

public class RangedZombie extends Zombie {
    private final int cooldownMax = 120;
    private String projectileType;
    private int cooldown;

    public RangedZombie(int health, double speed, int damage, String projectileType) {
        super(Zombies.RANGED, health, speed, damage);
        this.projectileType = projectileType;
        this.cooldown = 0;
    }

    public void throwProjectile() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;
        switch (projectileType) {
            case "SNOWBALL": {
                int row = (int) (this.y / App.getCellHeight());
                Plant target = game.getNearestPlantInRow(row, this.x - 10);
                if (target != null) {
                    target.increaseFrozenAmount();
                    System.out.println("❄️ Hunter Zombie threw a snowball at " + target.getType() +
                            " at (" + target.getX() + ", " + target.getY() + ")");
                } else {
                    System.out.println("❄️ Hunter Zombie threw a snowball but no target found in row " + row);
                }
                break;
            }
            case "OCTOPUS": {
                Plant target = game.getNearestPlantInRow((int) (this.y / App.getCellHeight()), this.x - 10);
                if (target != null && !target.isDead()) {
                    Cell cell = game.getCellAt(target.getX(), target.getY());
                    if (cell != null && !cell.hasObstacle()) {
                        OctopusObstacle octopus = new OctopusObstacle(target.getX(), target.getY(), target, cell);
                        cell.setObstacle(octopus);
                        game.getActiveObstacles().add(octopus);
                        GameMenuController.updateState(
                                "Octopus thrown at plant at (" + target.getX() + ", " + target.getY() + ")");
                    } else {
                        // System.out.println("Cell already has an obstacle or cannot place octopus.");
                    }
                }
                break;
            }

            case "BONE": {
                List<Cell> targetCells = game.findTwoEmptyCell(false);
                if (!targetCells.isEmpty()) {
                    for (Cell cell : targetCells) {
                        game.createGrave((int) cell.getX(), (int) cell.getY());
                        GameMenuController.updateState(
                                "Tomb Raiser threw a bone at (" +
                                        cell.getCol() + ", " + cell.getRow() + ")");
                    }
                } else {
                    GameMenuController.updateState("No empty cell on the board to place grave.");
                }
                break;
            }
        }
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update();
        if (cooldown <= 0) {
            throwProjectile();
            cooldown = cooldownMax;
        } else {
            cooldown--;
        }
    }
}