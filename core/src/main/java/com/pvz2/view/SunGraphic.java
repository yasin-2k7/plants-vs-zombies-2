package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.Main;
import com.pvz2.models.core.App;
import com.pvz2.models.projectile.Projectile;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.Sun;
import com.pvz2.models.world.SunType;
import pvz.libpvz.pam.PamPlayer;

public class SunGraphic {
    private Sun sun;
    private float animTime = 0f;
    private boolean popping = false;
    private boolean popFinished = false;

    public SunGraphic(Sun sun) {
        this.sun = sun;
    }

    public void update(float delta) {
        GameWorld world = App.getCurrentGame();
        if (world != null && world.getState() != GameState.PLAYING) delta = 0;
        animTime += delta;

        if ((sun.isCollected() || sun.isExploded()) && !popping) {
            popping = true;
            animTime = 0f;
        }

        if (popping && animTime >= 0.4f) {
            popFinished = true;
            sun.setExpired(true);
        }
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer, Main game) {
        if (popFinished) return;

        String pamPath = getPamPath(sun.getType());
        String animState = popping ? "attack" : "animation";

        float scale = getScale(sun.getType());

        try {
            pamPlayer.draw(
                batch,
                pamPath,
                animState,
                animTime* App.getSpeed(),
                sun.getX(),
                sun.getY(),
                scale,
                scale,
                true
            );
        } catch (Exception e) {
        }
    }

    private String getPamPath(SunType type) {
        if (type == SunType.RADIOACTIVE) {
            return "768/FULL/EFFECTS/SUN_BOMB/SUN_BOMB.PAM";
        }
        return "768/INITIAL/EFFECTS/SUN/SUN.PAM";
    }

    public static float getScale(SunType type) {
        if (type == null) return 1f;
        switch (type) {
            case SPECIAL: return 1.5f;
            case RADIOACTIVE: return 1.1f;
            default: return 1f;
        }
    }

    public void updateModel(Sun newSun) {
        this.sun = newSun;
    }
    public Sun getSun() { return sun; }
    public boolean isPopFinished() { return popFinished; }
}
