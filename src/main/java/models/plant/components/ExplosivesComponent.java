package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class ExplosivesComponent implements GameComponent {
    private boolean isMine; // آیا مین است (صبر کند تا زامبی بیاید) یا فوری منفجر شود؟

    public ExplosivesComponent(boolean isMine) {
        this.isMine = isMine;
    }

    @Override
    public void update(Plant plant) {
        if (!isMine) {
            // TODO: انفجار فوری پس از کاشته شدن و آسیب به محوطه اطراف
            explode(plant);
        } else {
            // TODO: بررسی اینکه آیا زامبی در نزدیکی (فاصله مشخص) قرار دارد یا خیر. اگر بود -> explode()
        }
    }

    private void explode(Plant plant) {
        // TODO: اعمال دمیج Area-of-Effect به زامبی‌های اطراف
        // TODO: حذف گیاه از زمین بازی پس از انفجار
    }
}
