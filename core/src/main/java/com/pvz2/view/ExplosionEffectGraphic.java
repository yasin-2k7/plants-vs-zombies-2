package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.models.core.App;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
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
        GameWorld world = App.getCurrentGame();
        if (world != null && world.getState() != GameState.PLAYING) delta = 0;
        animTime += delta;
    }

    public boolean isFinished(PamPlayer pamPlayer) {
        return pamPlayer == null || animTime >= pamPlayer.clipDurationSeconds(pamPath, clip);
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer) {
        if (pamPlayer == null) return;
        pamPlayer.draw(batch, pamPath, clip, animTime* App.getCurrentUser().getGameSpeed(), x, y, scaleX, scaleY, false);
    }
}
