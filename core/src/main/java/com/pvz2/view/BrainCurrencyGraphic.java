package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.Main;
import com.pvz2.models.core.App;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.network.onlineIZombie.BrainCurrency;
import pvz.libpvz.pam.PamPlayer;

public class BrainCurrencyGraphic {
    private BrainCurrency brainCurrency;
    private float animTime = 0f;

    public BrainCurrencyGraphic(BrainCurrency brainCurrency) {
        this.brainCurrency = brainCurrency;
    }

    public void update(float delta) {
        GameWorld world = App.getCurrentGame();
        if (world != null && world.getState() != GameState.PLAYING) delta = 0;
        animTime += delta;

        if (brainCurrency.isCollected()) {
            animTime = 0f;
        }
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer, Main game) {
        String pamPath = "768/FULL/ZOMBIE/POWER_BRAIN_PROJECTILE/POWER_BRAIN_PROJECTILE.PAM";
        String animState = "animation";

        float scale = 0.8f;

        try {
            pamPlayer.draw(
                batch,
                pamPath,
                animState,
                animTime,
                brainCurrency.getX(),
                brainCurrency.getY(),
                scale,
                scale,
                true
            );
        } catch (Exception e) {
        }
    }

    public BrainCurrency getBrainCurrency() { return brainCurrency; }
    public void updateModel(BrainCurrency newBrainCurrency) {
        this.brainCurrency = newBrainCurrency;
    }
}
