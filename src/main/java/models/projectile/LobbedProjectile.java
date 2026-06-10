package models.projectile;

import models.pool.Resettable;

import java.security.cert.PolicyNode;

public class LobbedProjectile implements Resettable, Projectile {
    private ProjectileModel model;
    private double x, y;

    @Override
    public void makeDamage() {

    }
}
