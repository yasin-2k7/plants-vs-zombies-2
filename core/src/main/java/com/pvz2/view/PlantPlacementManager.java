package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantType;

public class PlantPlacementManager {

    private PlantType selectedPlant;
    private Runnable onPlacedCallback;

    private static final float PREVIEW_WIDTH = 90f;
    private static final float PREVIEW_HEIGHT = 90f;

    public void selectPlant(PlantType plantType, Runnable onPlacedCallback) {
        this.selectedPlant = plantType;
        this.onPlacedCallback = onPlacedCallback;
    }

    public boolean isPlantSelected() {
        return selectedPlant != null;
    }

    public PlantType getSelectedPlant() {
        return selectedPlant;
    }

    public void cancelSelection() {
        this.selectedPlant = null;
        this.onPlacedCallback = null;
    }


    public boolean tryPlace(int row, int col) {
        if (!isPlantSelected()) return false;

        boolean success = true;

        if (success) {
            if (onPlacedCallback != null) {
                onPlacedCallback.run();
            }
            cancelSelection();
            return true;
        }
        return false;
    }


    public void drawPreview(SpriteBatch batch, Vector3 cursorWorldPos) {
        if (!isPlantSelected()) return;

        String textureKey = PlantsTable.getPlantsMap().get(selectedPlant);
        if (textureKey == null) return;

        TextureRegion plantRegion = App.getGameApp().textureBank.region(textureKey);
        if (plantRegion == null) return;

        int col = LawnGrid.getColFromX(cursorWorldPos.x);
        int row = LawnGrid.getRowFromY(cursorWorldPos.y);

        if (col >= 0 && row >= 0) {
            float cellX = LawnGrid.getCellX(col) - (PREVIEW_WIDTH / 2f);
            float cellY = LawnGrid.getCellY(row) - (PREVIEW_HEIGHT / 2f);

            batch.setColor(1f, 1f, 1f, 0.35f);
            batch.draw(plantRegion, cellX, cellY, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        }

        float drawX = cursorWorldPos.x - (PREVIEW_WIDTH / 2f);
        float drawY = cursorWorldPos.y - (PREVIEW_HEIGHT / 2f);

        batch.setColor(1f, 1f, 1f, 0.85f);
        batch.draw(plantRegion, drawX, drawY, PREVIEW_WIDTH, PREVIEW_HEIGHT);

        batch.setColor(Color.WHITE);
    }
}
