package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.AnimationDurations;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.PlantAnimationClips;
import pvz.libpvz.pam.PamPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlantGraphic {

    private final Plant plant;
    private final float worldX;
    private final float worldY;

    private String normalPamPath;
    private String imitatorPamPath;
    private String pamPath;
    private String initialClip;
    private String currentClip;
    private float animTime = 0f;
    private boolean isLoop = true;

    private final String plantFoodBgPamPath;
    private float plantFoodBgAnimTime = 0f;
    private boolean inPlantFoodBg = false;

    private static final Map<String, TextureRegion> FROST_REGION_CACHE = new HashMap<>();



    private static final String FROST_33_KEY = "IMAGE_EFFECTS_FROSTBITE_CHILL_PLANT_FROSTBITE_CHILL_PLANT_153X62";
    private static final String FROST_66_KEY = "IMAGE_EFFECTS_FROSTBITE_CHILL_PLANT_FROSTBITE_CHILL_PLANT_153X79";

    private static final String[] ICE_HEALTH_KEYS = {
        "IMAGE_EFFECTS_FROSTBITE_ICE_BLOCK_PLANT_FROSTBITE_ICE_BLOCK_PLANT_164X169",
        "IMAGE_EFFECTS_FROSTBITE_ICE_BLOCK_PLANT_FROSTBITE_ICE_BLOCK_PLANT_167X172_5",
        "IMAGE_EFFECTS_FROSTBITE_ICE_BLOCK_PLANT_FROSTBITE_ICE_BLOCK_PLANT_167X172_4",
        "IMAGE_EFFECTS_FROSTBITE_ICE_BLOCK_PLANT_FROSTBITE_ICE_BLOCK_PLANT_167X172_3",
        "IMAGE_EFFECTS_FROSTBITE_ICE_BLOCK_PLANT_FROSTBITE_ICE_BLOCK_PLANT_167X172_2",
        "IMAGE_EFFECTS_FROSTBITE_ICE_BLOCK_PLANT_FROSTBITE_ICE_BLOCK_PLANT_167X172_1"
    };

    private Plant.State lastState = Plant.State.IDLE;

    public PlantGraphic(Plant plant, PamPlayer pamPlayer) {
        this.plant = plant;

        int col = plant.getCell().getCol();
        int row = plant.getCell().getRow();

        this.worldX = LawnGrid.getCellX(col);
        this.worldY = LawnGrid.getCellY(row);

        this.normalPamPath = PlantsCollectionMenuScreen.getPlantAnimAddress(plant.getType());
        this.imitatorPamPath = PlantsCollectionMenuScreen.getPlantAnimAddress(PlantType.IMITATER);
        this.pamPath = plant.isImitate() ? imitatorPamPath : normalPamPath;

        this.currentClip = plant.isImitate() ? "idle" : PlantsCollectionMenuScreen.getPlantInitialClip(plant.getType());
        this.initialClip = currentClip;

        if (normalPamPath != null && pamPlayer != null) pamPlayer.loadAsync(normalPamPath, null);
        if (imitatorPamPath != null && pamPlayer != null) pamPlayer.loadAsync(imitatorPamPath, null);

        this.plantFoodBgPamPath = PlantAnimationClips.getPlantFoodBackgroundPamPath();
        if (plantFoodBgPamPath != null && pamPlayer != null) {
            pamPlayer.loadAsync(plantFoodBgPamPath, null);
        }
    }

    public void update(float delta) {
        if (!plant.isFreeze()){
            animTime += delta;
        }


        String activePamPath = plant.isImitate() ? imitatorPamPath : normalPamPath;
        if (!activePamPath.equals(pamPath)) {
            pamPath = activePamPath;
            initialClip = PlantsCollectionMenuScreen.getPlantInitialClip(plant.getType());
        }

        Plant.State state = plant.getState();
        boolean stateJustEntered = state != lastState;
        lastState = state;

        boolean isPlantFoodState = (state == Plant.State.PLANT_FOOD || state == Plant.State.PLANT_FOOD2);
        if (isPlantFoodState) {
            if (!inPlantFoodBg) {
                inPlantFoodBg = true;
                plantFoodBgAnimTime = 0f;
            } else {
                plantFoodBgAnimTime += delta;
            }
        } else {
            inPlantFoodBg = false;
        }

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

        float duration = AnimationDurations.getDuration(plant.getType(), currentClip, 0.5f);
        boolean readyToReturnToIdle = isLoop || animTime >= duration;
        if (readyToReturnToIdle) {
            playClip(initialClip, true);
        }
    }

    private static TextureRegion cachedRegion(String key) {
        if (key == null) return null;
        return FROST_REGION_CACHE.computeIfAbsent(key,
            k -> App.getGameApp().textureBank.region(k));
    }

    private TextureRegion resolveFrostOverlay() {
        if (plant.isFreeze()) {
            int stageCount = ICE_HEALTH_KEYS.length;
            float remainingFraction = plant.getIceHealth() / (float) Plant.MAX_ICE_HEALTH;
            int stageIndex = Math.min(stageCount - 1,
                (int) ((1f - remainingFraction) * stageCount));
            return cachedRegion(ICE_HEALTH_KEYS[stageIndex]);
        }
        int frozenAmount = plant.getFrozenAmount();
        if (frozenAmount >= 66) return cachedRegion(FROST_66_KEY);
        if (frozenAmount >= 33) return cachedRegion(FROST_33_KEY);
        return null;
    }

    private ClipInfo resolveClipFor(Plant.State state) {
        if (state == Plant.State.IMITATE_IDLE) {
            return new ClipInfo("idle", true);
        }
        if (state == Plant.State.IMITATE_ATTACK) {
            return new ClipInfo("attack", true);
        }
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
        if (state == Plant.State.INTRO){
            return new ClipInfo("intro", false);
        }
        if (state == Plant.State.DAMAGE || state == Plant.State.DAMAGE2 || state == Plant.State.DAMAGE3){
            return new ClipInfo(PlantAnimationClips.getDamagedClip(plant.getType(), state), true);
        }
        if (state == Plant.State.PLANT_FOOD) {
            return new ClipInfo(PlantAnimationClips.getPlantFoodClip(plant.getType()), true);
        }
        if (state == Plant.State.PLANT_FOOD2) {
            return new ClipInfo(PlantAnimationClips.getPlantFood2Clip(plant.getType()), true);
        }
        if (state == Plant.State.BUSY){
            return new ClipInfo("busy", true);
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

        if (inPlantFoodBg && plantFoodBgPamPath != null) {
            String bgClip = PlantAnimationClips.getPlantFoodBackgroundClip();
            pamPlayer.draw(batch, plantFoodBgPamPath, bgClip, plantFoodBgAnimTime, worldX+10, worldY+80, 0.8f, 0.8f,
                true);
        }

        pamPlayer.draw(batch, pamPath, currentClip, animTime, worldX, worldY, 0.8f, 0.8f,
            isLoop);

        TextureRegion overlay = resolveFrostOverlay();
        if (overlay != null) {
            float w = LawnGrid.CELL_WIDTH * 0.8f;
            float h = LawnGrid.CELL_HEIGHT * 0.8f;
            batch.draw(overlay, worldX - w / 2f, worldY - h / 2f, w, h);
        }
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
