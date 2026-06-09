package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class ShooterComponent implements GameComponent {
    private int damage;
    private float fireRate;
    private String bulletType; // "Normal", "Fire", "Ice", "Poison"


    public ShooterComponent(int damage, String bulletType) {
        this.damage = damage;
        this.bulletType = bulletType;
    }

    @Override
    public void update(Plant owner) {
        // منطق پیدا کردن زامبی و شلیک تیر
    }
}