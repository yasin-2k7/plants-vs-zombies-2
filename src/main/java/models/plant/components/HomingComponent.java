package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class HomingComponent implements GameComponent {
    private int damage;

    public HomingComponent(int damage) {
        this.damage = damage;
    }

    @Override
    public void update(Plant plant) {
        // TODO: جستجو در تمام زمین بازی (نه فقط لاین خود گیاه) برای پیدا کردن هدف
        // TODO: ایجاد پرتابه‌ای که مسیرش را به سمت زامبی هدف کج می‌کند (قفل کردن روی هدف)
    }
}
