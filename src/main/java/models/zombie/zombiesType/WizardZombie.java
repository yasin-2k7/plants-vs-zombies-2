package models.zombie.zombiesType;

import controller.GameMenuController;
import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

public class WizardZombie extends Zombie {
    private static final int COOLDOWN_MAX = 20;
    private List<Plant> transformedPlants;
    private int cooldown;

    public WizardZombie(int health, double speed, int damage) {
        super(Zombies.WIZARD, health, speed, damage);
        this.transformedPlants = new ArrayList<>();
        this.cooldown = 0;
        this.currentState = null;
    }

    @Override
    public void update() {
        if (isDead) return;
        Cell currentCell = Cell.findZombieCell(App.getCurrentGame().getGrid(), this);
        if (currentCell != null && currentCell.getSlippingDir() != 0) {
            y += App.getCellHeight() * currentCell.getSlippingDir();
        }

        move();

        if (cooldown <= 0) {
            castSpell();
            cooldown = COOLDOWN_MAX;
        } else {
            cooldown--;
        }
    }

    private void castSpell() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        Plant target = game.getNearestPlantInRow((int) (this.y / App.getCellHeight()), this.x + 10);
        if (target != null && !target.isDead() && !target.isCat()) {
            target.setCat(true);
            transformedPlants.add(target);
            GameMenuController.updateState("Wizard turned a " + target.getType().name() + " into a cat!");
        }
    }

    @Override
    public void die() {
        for (Plant p : transformedPlants) {
            if (p != null && !p.isDead()) {
                p.setCat(false);
            }
        }
        transformedPlants.clear();
        super.die();
    }
}