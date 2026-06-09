package models.plant.state;

import models.plant.Plant;

public interface PlantState {
    // مدیریت رفتار گیاه بر اساس وضعیت فعلی‌اش
    void action(Plant plant);
}