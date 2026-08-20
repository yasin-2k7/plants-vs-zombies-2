package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.ProjectileType;
import com.pvz2.models.projectile.Projectile;
import pvz.libpvz.pam.PamPlayer;

public class ProjectileGraphic {
    private final Projectile projectile;
    private ProjectileType type;
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
        if (projectile.getType() != type) type = projectile.getType();
        ProjectileAssets.VisualInfo info = ProjectileAssets.get(type);
        if (info == null) return;

        float drawX, drawY;

        if (type == ProjectileType.STAR || type == ProjectileType.ROTOBAGA_PROJECTILE){
            drawX = lastX;
            drawY = lastY;
        }
        else{
            drawY = type.movement.equals("STRAIGHT") ? lastY + 20 : lastY + 15;
            drawX = type.movement.equals("STRAIGHT") ? lastX : lastX - 30;
        }
        if (type == ProjectileType.CACTUS || type == ProjectileType.CACTUS_SPECIAL) drawY += 15;
        if (type == ProjectileType.SMALL_SHROOM) drawY -= 15;
        if (type == ProjectileType.FUME || type == ProjectileType.FUME_SPECIAL){
            drawX += 60;
            drawY -= 20;
        }
        if (info.kind == ProjectileAssets.Kind.PAM) {
            pamPlayer.draw(batch, info.flightPamPath, info.flightClip, animTime* App.getCurrentUser().getGameSpeed(), drawX, drawY, true);
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
