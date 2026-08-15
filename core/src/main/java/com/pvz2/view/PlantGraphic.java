package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.Plant;
import pvz.libpvz.pam.PamPlayer;

public class PlantGraphic {

    private final Plant plant;
    private final float worldX;
    private final float worldY;

    private final String pamPath;
    private String currentClip;
    private float animTime = 0f;
    private boolean isLoop = true;

    public PlantGraphic(Plant plant, PamPlayer pamPlayer) {
        this.plant = plant;

        int col = plant.getCell().getCol();
        int row = plant.getCell().getRow();

        this.worldX = LawnGrid.getCellX(col);
        this.worldY = LawnGrid.getCellY(row);

        this.pamPath = PlantsCollectionMenuScreen.getPlantAnimAddress(plant.getType());
        this.currentClip = PlantsCollectionMenuScreen.getPlantInitialClip(plant.getType());

        if (pamPath != null && pamPlayer != null) {
            pamPlayer.loadAsync(pamPath, null);
        }
    }

    public void update(float delta) {
        animTime += delta;
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer) {
        if (plant == null || plant.isDead() || pamPath == null || pamPlayer == null) return;

        pamPlayer.draw(batch, pamPath, currentClip, animTime, worldX, worldY, isLoop);
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
}
