package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class MintsComponent implements GameComponent {
    private String plantFamily; // خانواده گیاهانی که قرار است تقویت شوند
    private boolean isEffectApplied = false;

    public MintsComponent(String plantFamily) {
        this.plantFamily = plantFamily;
    }

    @Override
    public void update(Plant plant) {
        if (!isEffectApplied) {
            // TODO: پیدا کردن تمام گیاهان روی زمین که از خانواده plantFamily هستند
            // TODO: اعمال افکت Plant Food روی تمام آن‌ها
            isEffectApplied = true;

            // TODO: حذف کردن این نعنا از زمین بازی پس از چند ثانیه
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {

    }
}
