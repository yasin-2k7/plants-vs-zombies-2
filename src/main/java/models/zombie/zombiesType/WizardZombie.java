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
    private final int COOLDOWN_MAX = 20; // 1.3 ثانیه

    public WizardZombie(int health, double speed, int damage) {
        super(Zombies.WIZARD, health, speed, damage);
        this.transformedPlants = new ArrayList<>();
        this.cooldown = 0;
    }

    @Override
    public void update() {
        if (isDead) return;
        super.update();
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
        Plant target = game.getNearestPlantInRow((int)this.y, this.x + 10);
        if (target != null && !target.isDead()) {
            target.setSheep(true);
            transformedPlants.add(target);
        }
    }

    @Override
    public void die() {
        // رفع طلسم از تمام گیاهان تبدیل‌شده
        for (Plant p : transformedPlants) {
            if (p != null && !p.isDead()) {
                p.setSheep(false);
            }
        }
        transformedPlants.clear();
        super.die();
    }
}
