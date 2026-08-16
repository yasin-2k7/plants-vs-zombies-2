package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.AnimationDurations;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.PlantAnimationClips;
import pvz.libpvz.pam.PamPlayer;

public class PlantGraphic {

    private final Plant plant;
    private final float worldX;
    private final float worldY;

    private final String pamPath;
    private String initialClip;
    private String currentClip;
    private float animTime = 0f;
    private boolean isLoop = true;

    private Plant.State lastState = Plant.State.IDLE;

    public PlantGraphic(Plant plant, PamPlayer pamPlayer) {
        this.plant = plant;

        int col = plant.getCell().getCol();
        int row = plant.getCell().getRow();

        this.worldX = LawnGrid.getCellX(col);
        this.worldY = LawnGrid.getCellY(row);

        this.pamPath = PlantsCollectionMenuScreen.getPlantAnimAddress(plant.getType());
        this.currentClip = PlantsCollectionMenuScreen.getPlantInitialClip(plant.getType());
        this.initialClip = currentClip;

        if (pamPath != null && pamPlayer != null) {
            pamPlayer.loadAsync(pamPath, null);
        }
    }

    public void update(float delta) {
        animTime += delta;

        Plant.State state = plant.getState();
        boolean stateJustEntered = state != lastState;
        lastState = state;

        if (state != Plant.State.IDLE) {
            if (stateJustEntered) {
                ClipInfo info = resolveClipFor(state);
                if (info != null) {
                    playClip(info.clipName, info.loop);
                }
            }
            return; // keep playing whatever clip is active for this state
        }

        // state == IDLE
        if (currentClip.equals(initialClip)) {
            return; // already on the idle clip
        }

        // Looping clips (e.g. "unarmed") have no natural finish point, so cut
        // over immediately once the model returns to IDLE. One-shot clips
        // (e.g. "attack") still wait to visually finish first.
        float duration = AnimationDurations.getDuration(plant.getType(), currentClip, 0.5f);
        boolean readyToReturnToIdle = isLoop || animTime >= duration;
        if (readyToReturnToIdle) {
            playClip(initialClip, true);
        }
    }

    private ClipInfo resolveClipFor(Plant.State state) {
        if (state == Plant.State.SPECIAL || plant.getType() == PlantType.PUFF_SHROOM ||
            plant.getType() == PlantType.FUME_SHROOM) {
            return new ClipInfo(PlantsCollectionMenuScreen.getSpecialClip(plant.getType()), false);
        }
        if (state == Plant.State.ATTACK) {
            if (plant.getType() == PlantType.KIWIBEAST) return new ClipInfo("attack_stage3", false);
            return new ClipInfo("attack", false);
        }
        if (state == Plant.State.TRIGGERED) {
            return new ClipInfo(PlantAnimationClips.getTriggerClip(plant.getType()), false);
        }
        if (state == Plant.State.UNARMED) {
            return new ClipInfo(PlantAnimationClips.getUnarmedClip(plant.getType()), true);
        }
        if(state == Plant.State.HIT_LEFT || state == Plant.State.HIT_RIGHT || state == Plant.State.HIT_RIGHT_AND_LEFT){
            return new ClipInfo(PlantAnimationClips.getHitClip(state), false);
        }
        if (state == Plant.State.SPECIAL_IDLE){
            return new ClipInfo("special_idle", true);
        }
        return null;
    }

    private static class ClipInfo {
        final String clipName;
        final boolean loop;
        ClipInfo(String clipName, boolean loop) {
            this.clipName = clipName;
            this.loop = loop;
        }
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer) {
        if (plant == null || plant.isDead() || pamPath == null || pamPlayer == null) return;
        pamPlayer.draw(batch, pamPath, currentClip, animTime, worldX, worldY, 0.8f, 0.8f,
            isLoop);
    }

    public void playClip(String clipName, boolean loop) {
        this.currentClip = clipName;
        this.isLoop = loop;
        this.animTime = 0f;
    }

    public Plant getPlant() { return plant; }
    public int getRow() { return (int) plant.getY(); }
    public int getCol() { return (int) plant.getX(); }
    public PlantType getPlantType() { return plant.getType(); }
    public boolean isDead() { return plant.isDead(); }

    public float getWorldX() { return worldX; }
    public float getWorldY() { return worldY; }

    public String getCurrentClip() { return currentClip; }
    public String getInitialClip() { return initialClip; }
}
