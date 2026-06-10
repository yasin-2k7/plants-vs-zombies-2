package models.projectile;

import models.pool.Resettable;

public class DirectlyProjectile implements Resettable , Projectile{
    private ProjectileModel model;
    private double x, y;

    @Override
    public void makeDamage() {

    }
}