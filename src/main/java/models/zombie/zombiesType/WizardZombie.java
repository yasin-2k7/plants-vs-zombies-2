package models.zombie.zombiesType;

import models.zombie.Zombie;
import java.util.ArrayList;
import java.util.List;

public class WizardZombie extends Zombie {
    // لیستی برای نگه‌داری گیاهانی که طلسم شده‌اند
    private List<Object> transformedPlants = new ArrayList<>();

    public WizardZombie(int health, int speed, int damage) {
        super("Wizard Zombie", health, speed, damage);
    }
    // متد تبدیل گیاه به گربه
    public void transformPlant(Object plant) {
        //plant.setCatState(true);
        transformedPlants.add(plant);
    }

    @Override
    public void die() {
        // باطل شدن طلسم تمام گیاهان با مرگ جادوگر
        for (Object plant : transformedPlants) {
            //plant.setCatState(false);
        }
        transformedPlants.clear();
        super.die();
    }
}
