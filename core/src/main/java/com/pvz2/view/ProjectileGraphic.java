package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvz2.models.enums.ProjectileType;
import com.pvz2.models.projectile.Projectile;
import pvz.libpvz.pam.PamPlayer;

public class ProjectileGraphic {
    private final Projectile projectile;
    private final ProjectileType type;
    private float animTime = 0f;
    private final int generation;

    private float lastX;
    private float lastY;

    public ProjectileGraphic(Projectile projectile, int generation) {
        this.projectile = projectile;
        this.type = projectile.getType();
        this.lastX = projectile.getX();
        this.lastY = projectile.getY();
        this.generation = generation;
    }

    public void update(float delta) {
        animTime += delta;
        lastX = projectile.getX();
        lastY = projectile.getY();
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer) {
        ProjectileAssets.VisualInfo info = ProjectileAssets.get(type);
        if (info == null) return;

        float drawY = type.movement.equals("STRAIGHT") ? lastY - 35 : lastY + 10;
        float drawX = type.movement.equals("STRAIGHT") ? lastX - 20 : lastX - 25;

        if (info.kind == ProjectileAssets.Kind.PAM) {
            pamPlayer.draw(batch, info.flightPamPath, info.flightClip, animTime, drawX, drawY, true);
        } else {
            TextureRegion region = ProjectileAssets.region(info.textureRegionKey);
            if (region == null) return;
            float w = region.getRegionWidth();
            float h = region.getRegionHeight();
            batch.draw(region, drawX - w / 2f, drawY - h / 2f, w, h);
        }
    }

    public float getLastX() { return lastX; }
    public float getLastY() { return lastY; }
    public ProjectileType getType() { return type; }

    public int getGeneration() {
        return generation;
    }
}
