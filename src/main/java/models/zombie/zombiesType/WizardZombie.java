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
    private List<Plant> transformedPlants;
    private int cooldown;
    private static final int COOLDOWN_MAX = 20;

    public WizardZombie(int health, double speed, int damage) {
        super(Zombies.WIZARD, health, speed, damage);
        this.transformedPlants = new ArrayList<>();
        this.cooldown = 0;
        // وضعیت را نادیده می‌گیریم تا هیچ‌گاه وارد EatingState نشود
        this.currentState = null;
    }

    @Override
    public void update() {
        if (isDead) return;

//        // ---- مدیریت کندی ----
//        if (slowTicksRemaining > 0) {
//            slowTicksRemaining--;
//            if (slowTicksRemaining == 0) {
//                this.speed = originalSpeed;
//            }
//        }
//
//        // ---- مدیریت غیرفعال‌سازی ----
//        if (disabledTicksRemaining > 0) {
//            disabledTicksRemaining--;
//            return;
//        }
//
//        // ---- مدیریت یخ‌زدگی ----
//        if (freezedTicksRemaining > 0) {
//            freezedTicksRemaining--;
//            if (freezedTicksRemaining == 0) {
//                applySlow(20, 0.5, true);
//            }
//            return;
//        }

        // ---- لغزندگی (از سلول) ----
        Cell currentCell = Cell.findZombieCell(App.getCurrentGame().getGrid(), this);
        if (currentCell != null && currentCell.getSlippingDir() != 0) {
            y += App.getCellHeight() * currentCell.getSlippingDir();
        }

        // ---- حرکت (بدون خوردن) ----
        move();

        // ---- پرتاب طلسم ----
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

        // پیدا کردن نزدیک‌ترین گیاه در همان سطر، سمت راست جادوگر
        Plant target = game.getNearestPlantInRow((int) (this.y / App.getCellHeight()), this.x + 10);
        if (target != null && !target.isDead() && !target.isCat()) {
            target.setCat(true);
            transformedPlants.add(target);
            GameMenuController.updateState("Wizard turned a " + target.getType().name() + " into a cat!");
        }
    }

    @Override
    public void die() {
        // رفع طلسم از تمام گیاهان تبدیل‌شده (اگر زنده باشند)
        for (Plant p : transformedPlants) {
            if (p != null && !p.isDead()) {
                p.setCat(false);
            }
        }
        transformedPlants.clear();
        super.die();
    }
}