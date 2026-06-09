package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class MeleeAttackersComponent implements GameComponent {
    private int meleeDamage;

    public MeleeAttackersComponent(int damage) {
        this.meleeDamage = damage;
    }

    @Override
    public void update(Plant plant) {
        // TODO: بررسی اینکه آیا زامبی در خانه مجاور (فاصله نزدیک) قرار دارد
        // اگر زامبی نزدیک بود:
        meleeAttack();
    }

    private void meleeAttack() {
        // TODO: اعمال دمیج مستقیم (بدون تولید پرتابه) به زامبیِ نزدیک
    }


}
