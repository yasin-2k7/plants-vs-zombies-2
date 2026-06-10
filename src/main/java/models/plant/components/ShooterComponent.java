package models.plant.components;

import models.enums.ProjectileType;
import models.plant.GameComponent;
import models.plant.Plant;

public class ShooterComponent implements GameComponent {
    private int damage;
    private float fireRate;
    private ProjectileType bulletType; // "Normal", "Fire", "Ice", "Poison"


    public ShooterComponent(int damage, ProjectileType bulletType) {
        this.damage = damage;
        this.bulletType = bulletType;
    }

    @Override
    public void update(Plant owner) {
        // منطق پیدا کردن زامبی و شلیک تیر
    }
}