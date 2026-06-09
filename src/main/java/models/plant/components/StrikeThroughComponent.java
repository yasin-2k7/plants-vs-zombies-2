package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class StrikeThroughComponent implements GameComponent {
    private int damage;

    public StrikeThroughComponent(int damage) {
        this.damage = damage;
    }

    @Override
    public void update(Plant plant) {
        strikeAttack();
    }

    private void strikeAttack() {
        // TODO: ایجاد پرتابه یا حمله‌ای که با برخورد به اولین زامبی متوقف نمی‌شود
        // TODO: اعمال دمیج به تمام زامبی‌هایی که در مسیر (یک لاین) قرار دارند
    }
}
