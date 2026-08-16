package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.models.zombie.Zombie;
import pvz.libpvz.pam.PamPlayer;

import java.util.HashMap;

public class ZombieGraphic {
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
        if (zombie.isDead() && !currentClip.equals("die")) {
            playClip(resolveClip("die"), false);
        }
        if (zombie.isDead()) return;

        animTime += delta;

        String targetClip = resolveClip(zombie.getAnimationClip());
        if (!targetClip.equals(currentClip)) {
            playClip(targetClip, true);
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
        return baseClip;
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer) {
        if (zombie.isDead() || pamPath == null) return;

        float renderX = zombie.getX();
        float renderY = zombie.getY();

        if (visibilities != null) {
            pamPlayer.draw(batch, pamPath, currentClip, animTime, renderX,
                renderY - LawnGrid.CELL_HEIGHT / 2, 0.8f, 0.8f, isLoop, visibilities);
        } else {
            pamPlayer.draw(batch, pamPath, currentClip, animTime, renderX,
                renderY - LawnGrid.CELL_HEIGHT / 2, 0.8f, 0.8f, isLoop);
        }
    }

    public void playClip(String clipName, boolean loop) {
        if (!this.currentClip.equals(clipName)) {
            this.currentClip = clipName;
            this.isLoop = loop;
            this.animTime = 0f;
        }
    }

    public Zombie getZombie() {
        return zombie;
    }
}
