package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class WallNutsComponent implements GameComponent {
    private String nutType;
    private int damage;

    public WallNutsComponent(String nutType, int damage) {
        this.nutType = nutType;
        this.damage = damage;
    }

    @Override
    public void update(Plant owner) {
        //بررسی انواع دانه ها و مدت زمان مقاومت انها
    }

    @Override
    public void activatePlantFood(Plant owner) {

    }
}
