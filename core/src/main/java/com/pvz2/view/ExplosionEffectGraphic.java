package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pvz.libpvz.pam.PamPlayer;

public class ExplosionEffectGraphic {
    private final String pamPath;
    private final String clip;
    private final float x, y;
    private final float scaleX, scaleY;
    private float animTime = 0f;

    public ExplosionEffectGraphic(String pamPath, String clip, float x, float y, PamPlayer pamPlayer, float scaleX, float scaleY) {
        this.pamPath = pamPath;
        this.clip = clip;
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        if (pamPlayer != null) pamPlayer.loadAsync(pamPath, null);
    }

    public void update(float delta) {
        animTime += delta;
    }

    public boolean isFinished(PamPlayer pamPlayer) {
        return pamPlayer == null || animTime >= pamPlayer.clipDurationSeconds(pamPath, clip);
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer) {
        if (pamPlayer == null) return;
        pamPlayer.draw(batch, pamPath, clip, animTime, x, y, scaleX, scaleY, false);
    }
}
