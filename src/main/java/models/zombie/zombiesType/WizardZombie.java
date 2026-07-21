package models.zombie.zombiesType;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;
import java.util.ArrayList;
import java.util.List;

public class WizardZombie extends Zombie {
    private List<Plant> transformedPlants;
    private int cooldown;
    private final int COOLDOWN_MAX = 20;

    public WizardZombie(int health, double speed, int damage) {
        super(Zombies.WIZARD, health, speed, damage);
        this.transformedPlants = new ArrayList<>();
        this.cooldown = 0;
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update(); // حرکت عادی
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
            System.out.println("Wizard turned a " + target.getType().name() + " into a sheep!");
        }
    }

    @Override
    public void die() {
        // رفع طلسم از تمام گیاهان تبدیل‌شده
        for (Plant p : transformedPlants) {
            if (p != null && !p.isDead()) {
                p.setCat(false);
            }
        }
        transformedPlants.clear();
        super.die();
    }
}