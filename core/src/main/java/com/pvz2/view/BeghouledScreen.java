package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pvz2.Main;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.enums.Chapter;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.miniGame.beghouled.BeghouledMechanics;
import com.pvz2.models.miniGame.beghouled.GridPosition;
import com.pvz2.models.miniGame.beghouled.PlantUpgrade;
import com.pvz2.models.miniGame.vaseBreaker.SeedPacket;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;

public class BeghouledScreen extends GameScreen {

    private final BeghouledMechanics mechanics;
    private final ShapeRenderer shapeRenderer;

    private GridPosition dragStartPos = null;
    private boolean isDragging = false;
    private Plant draggedPlant = null;

    private Table topUIBar;
    private Label progressLabel;

    private final Vector3 initialTouchPoint = new Vector3();

    public BeghouledScreen(Main game, GameWorld world, Chapter chapter) {
        super(game, world, chapter);
        this.mechanics = world.getMechanic(BeghouledMechanics.class);
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        super.show();
        if (topUIBar == null) {
            buildTopUIBar();
            initHUDUpgrades();
        }
        initIcyPlantGraphics();
    }

    private void buildTopUIBar() {
        if (mechanics == null) return;

        topUIBar = new Table();
        topUIBar.setFillParent(true);
        topUIBar.top().padTop(10);

        progressLabel = new Label("Matches: 0 / 500", skin, "big_outline");
        topUIBar.add(progressLabel).padBottom(8).row();

        mainStack.addActor(topUIBar);
    }
    private void initHUDUpgrades() {
        if (mechanics == null || getHud() == null) return;

        GameHUD.SelectedPlantsList selectedList = getHud().getSelectedPlantsList();

        PlantType[] slots = selectedList.getSlots();
        for (int i = 0; i < slots.length; i++) {
            slots[i] = null;
        }

        for (PlantUpgrade upgrade : mechanics.getUpgrades()) {
            selectedList.addPlant(upgrade.getTo());
        }

        selectedList.build();

        for (Actor child : selectedList.getChildren()) {
            if (child instanceof Table) {
                Table cardTable = (Table) child;
                for (Actor inner : cardTable.getChildren()) {
                    if (inner instanceof PlantCardView) {
                        PlantCardView cardView = (PlantCardView) inner;

                        for (PlantUpgrade upgrade : mechanics.getUpgrades()) {
                            if (upgrade.getTo() == cardView.getType()) {
                                cardView.setClickMethod(view -> {
                                    String error = mechanics.upgradePlant(world, upgrade.getFrom());
                                    if (error == null) {
                                        announce("Upgraded " + upgrade.getFrom().name() + "!");
                                        initIcyPlantGraphics();
                                    } else {
                                        announce(error);
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        if (mechanics != null && mechanics.consumeNeedsViewUpdate()) {
            initIcyPlantGraphics();
        }
        resumeCameraToMain();

        if (progressLabel != null && mechanics != null) {
            int currentMatches = mechanics.getMatchCount();
            int targetMatches = mechanics.getTargetMatches();
            progressLabel.setText("Matches: " + currentMatches + " / " + (targetMatches > 0 ? targetMatches : 500));
        }

        super.render(delta);
        drawSelectionHighlight();
    }

    @Override
    public void handleInput() {
        Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldViewport.unproject(touchPoint);

        int col = LawnGrid.getColFromX(touchPoint.x);
        int row = LawnGrid.getRowFromY(touchPoint.y);

        if (Gdx.input.justTouched()) {
            if (row != -1 && col != -1) {
                dragStartPos = new GridPosition(row, col);
                Cell cell = world.getGrid()[row][col];
                draggedPlant = cell.getPlant();
                initialTouchPoint.set(touchPoint);
                isDragging = true;
            } else {
                GameMenuController.collectSun(touchPoint.x, touchPoint.y);
                resetDragState();
            }
        }
        else if (Gdx.input.isTouched() && isDragging && draggedPlant != null && dragStartPos != null) {
            float dx = touchPoint.x - initialTouchPoint.x;
            float dy = touchPoint.y - initialTouchPoint.y;

            if (Math.abs(dx) < 12 && Math.abs(dy) < 12) {
                return;
            }

            if (Math.abs(dx) > Math.abs(dy)) {
                dy = 0;
                dx = Math.max(-LawnGrid.CELL_WIDTH, Math.min(LawnGrid.CELL_WIDTH, dx));
            } else {
                dx = 0;
                dy = Math.max(-LawnGrid.CELL_HEIGHT, Math.min(LawnGrid.CELL_HEIGHT, dy));
            }

            float startX = LawnGrid.getCellX(dragStartPos.col());
            float startY = LawnGrid.getCellY(dragStartPos.row());

            draggedPlant.setX((int) (startX + dx));
            draggedPlant.setY((int) (startY + dy));
            draggedPlant.setTargetPosition(startX + dx, startY + dy);
        }
        else if (!Gdx.input.isTouched() && isDragging) {
            if (dragStartPos != null && draggedPlant != null) {
                float startX = LawnGrid.getCellX(dragStartPos.col());
                float startY = LawnGrid.getCellY(dragStartPos.row());

                int targetCol = dragStartPos.col();
                int targetRow = dragStartPos.row();

                if (draggedPlant.getX() > startX + LawnGrid.CELL_WIDTH / 3f) targetCol++;
                else if (draggedPlant.getX() < startX - LawnGrid.CELL_WIDTH / 3f) targetCol--;
                else if (draggedPlant.getY() > startY + LawnGrid.CELL_HEIGHT / 3f) targetRow++;
                else if (draggedPlant.getY() < startY - LawnGrid.CELL_HEIGHT / 3f) targetRow--;

                GridPosition targetPos = new GridPosition(targetRow, targetCol);

                if ((targetRow != dragStartPos.row() || targetCol != dragStartPos.col())
                    && targetRow >= 0 && targetRow < world.getRows()
                    && targetCol >= 0 && targetCol < world.getCols()) {

                    String error = mechanics.trySwap(world, dragStartPos, targetPos);
                    if (error != null) {
                        announce(error);
                        resetDraggedPlantPos();
                    }
                } else {
                    resetDraggedPlantPos();
                }
            }
            resetDragState();
        }
    }

    private void resetDraggedPlantPos() {
        if (draggedPlant != null && dragStartPos != null) {
            float originalX = LawnGrid.getCellX(dragStartPos.col());
            float originalY = LawnGrid.getCellY(dragStartPos.row());
            draggedPlant.setTargetPosition(originalX, originalY);
        }
    }

    private void resetDragState() {
        isDragging = false;
        dragStartPos = null;
        draggedPlant = null;
    }

    @Override
    public void restartLevel() {
        super.restartLevel();
        initIcyPlantGraphics();
    }

    private void drawSelectionHighlight() {
        if (dragStartPos == null) return;

        float x = LawnGrid.getCellX(dragStartPos.col());
        float y = LawnGrid.getCellY(dragStartPos.row());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(worldCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(x, y, LawnGrid.CELL_WIDTH, LawnGrid.CELL_HEIGHT);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
