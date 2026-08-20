package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.zombiesType.BarrelRollerZombie;
import pvz.libpvz.pam.PamPlayer;

import java.util.HashMap;
import java.util.Set;

public class ZombieGraphic {
    private static final Set<String> NON_LOOPING_CLIPS = Set.of(
        "spinup", "spindown", "fly_start", "fly_end"
    );

    private final Zombie zombie;
    private final String pamPath;
    private final HashMap<String, Boolean> visibilities;

    private float animTime = 0f;
    private String currentClip = "walk";
    private boolean isLoop = true;

    public ZombieGraphic(Zombie zombie) {
        this.zombie = zombie;

        String lookupName = (zombie.getSpecificName() != null)
            ? zombie.getSpecificName()
            : zombie.getName().name();

        this.pamPath = ZombiesTable.getZombiesAnimAddress().get(lookupName);
        this.visibilities = ZombiesTable.getZombiesVisibilities().get(lookupName);
    }

    public void update(float delta, PamPlayer pamPlayer) {
        String dieClip = resolveClip("die");

        if (zombie.isDead() && !currentClip.equals(dieClip)) {
            playClip(dieClip, false);
        }

        animTime += delta;

        if (!zombie.isDead()) {
            String targetClip = resolveClip(zombie.getAnimationClip());
            if (!targetClip.equals(currentClip)) {
                boolean loop = !NON_LOOPING_CLIPS.contains(targetClip);
                playClip(targetClip, loop);
            }
        }

        if (pamPath != null) {
            pamPlayer.loadAsync(pamPath, null);
        }
    }

    private String resolveClip(String baseClip) {
        String specificName = zombie.getSpecificName();
        if ("ZombieNewspaper".equals(specificName) && baseClip.equals("idle")) {
            return "idle_newspaper";
        }
        if (zombie instanceof BarrelRollerZombie barrelZombie && !barrelZombie.isBarrelIntact()) {
            return baseClip + "2";
        }
        return baseClip;
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer) {
        if (pamPath == null) return;

        float renderX = zombie.getX();
        float renderY = zombie.getY();

        float flashAmount = zombie.getDamageFlashProgress();
        if (flashAmount > 0f) {
            ShaderProgram shader = DamageFlashShader.get();
            batch.setShader(shader);
            shader.setUniformf("u_flashColor", 1f, 1f, 1f);
            shader.setUniformf("u_flashAmount", flashAmount);
        }

        // اگر سرعت کوچکتر از ۰ باشد یعنی در حال حرکت به راست است؛ پس Scale را برای محور X منفی می‌کنیم
        float scaleX = zombie.getSpeed() < 0 ? -0.8f : 0.8f;

        if (visibilities != null) {
            pamPlayer.draw(batch, pamPath, currentClip, animTime, renderX,
                renderY, scaleX, 0.8f, isLoop, visibilities);
        } else {
            pamPlayer.draw(batch, pamPath, currentClip, animTime, renderX,
                renderY, scaleX, 0.8f, isLoop);
        }

        if (flashAmount > 0f) {
            batch.setShader(null);
        }
    }

    public void playClip(String clipName, boolean loop) {
        if (!this.currentClip.equals(clipName)) {
            this.currentClip = clipName;
            this.isLoop = loop;
            this.animTime = 0f;
        }
    }

    public boolean isDeathAnimationFinished() {
        return zombie.isDead() && currentClip.equals(resolveClip("die")) && animTime >= 1.5f;
    }

    public Zombie getZombie() {
        return zombie;
    }
}
