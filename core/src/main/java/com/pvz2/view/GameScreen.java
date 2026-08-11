package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pvz2.Main;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Chapter;
import com.pvz2.models.lawnMower.LawnMower;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends MenuScreen {

    private PamPlayer pamPlayer;
    private final GameWorld world;
    private final Chapter chapter;

    private final OrthographicCamera worldCamera;
    private final Viewport worldViewport;

    private GameHUD hud;

    private final TextureRegion bgLeft;
    private final TextureRegion bgMain;
    private final TextureRegion bgRight;

    private final float mainLawnWidth;
    private final float mainLawnHeight;
    private float leftWidthScaled;
    private float rightWidthScaled;

    private final List<PanStep> introSteps = new ArrayList<>();
    private int currentStepIndex = 0;
    private float stepElapsed = 0f;
    private float panStartX;
    private boolean introFinished = false;

    private CrazyDaveOverlay daveOverlay;

    private record PanStep(float targetCenterX, float duration, boolean isTravel) {
    }

    public GameScreen(Main game, GameWorld world, Chapter chapter) {
        super(game);
        this.world = world;
        this.chapter = chapter;

        String[] keys = getBackgroundKeys(chapter);
        bgLeft = game.textureBank.region(keys[0]);
        bgMain = game.textureBank.region(keys[1]);
        bgRight = game.textureBank.region(keys[2]);

        mainLawnWidth = world.getCols() * App.getCellWidth();
        mainLawnHeight = world.getRows() * App.getCellHeight();

        worldCamera = new OrthographicCamera();
        worldViewport = new FillViewport(mainLawnWidth, mainLawnHeight, worldCamera);

        FileHandle assetsFolder = Gdx.files.internal("");
        pamPlayer = new PamPlayer(game.textureBank, assetsFolder);
        pamPlayer.loadAsync(getMowerPamPath(chapter), null);
        pamPlayer.loadAsync("768/INITIAL/EFFECTS/MOWER_SPAWN/MOWER_SPAWN.PAM", null);

        computeSideWidths();
        buildIntroPanSequence();

    }

    @Override
    public void show() {
        super.show();

        stage.setViewport(new ScreenViewport());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    protected void buildUI() {
        hud = new GameHUD(game, skin, this::restartLevel);
        mainStack.addActor(hud);

        List<String> starting = world.getStartingDialogs();
        if (starting == null) {
            starting = new ArrayList<>();
        }
        daveOverlay = new CrazyDaveOverlay(game, starting, world);
        modalStack.addActor(daveOverlay);
    }

    public void restartLevel() {
        world.reset();
        world.setEndGameHandled(false);

        buildIntroPanSequence();

        if (daveOverlay != null) {
            daveOverlay.remove();
        }
        List<String> starting = world.getStartingDialogs();
        if (starting == null) {
            starting = new ArrayList<>();
        }
        daveOverlay = new CrazyDaveOverlay(game, starting, world);
        modalStack.addActor(daveOverlay);
    }

    private String[] getBackgroundKeys(Chapter chapter) {
        if (chapter == null) chapter = Chapter.EGYPT;
        switch (chapter) {
            case EGYPT:
                return new String[]{
                    "IMAGE_BACKGROUNDS_EGYPT_TEXTURE_LEFT",
                    "IMAGE_BACKGROUNDS_EGYPT_TEXTURE",
                    "IMAGE_BACKGROUNDS_EGYPT_TEXTURE_RIGHT"
                };
            case BIG_WAVE_BEACH:
                return new String[]{
                    "IMAGE_BACKGROUNDS_BEACH_TEXTURE_LEFT",
                    "IMAGE_BACKGROUNDS_BEACH_TEXTURE",
                    "IMAGE_BACKGROUNDS_BEACH_TEXTURE_RIGHT"
                };
            case DARK_AGES:
                return new String[]{
                    "IMAGE_BACKGROUNDS_DARK_TEXTURE_LEFT",
                    "IMAGE_BACKGROUNDS_DARK_TEXTURE",
                    "IMAGE_BACKGROUNDS_DARK_TEXTURE_RIGHT"
                };
            case FROSTBITE_CAVES:
                return new String[]{
                    "IMAGE_BACKGROUNDS_ICEAGE_TEXTURE_LEFT",
                    "IMAGE_BACKGROUNDS_ICEAGE_TEXTURE",
                    "IMAGE_BACKGROUNDS_ICEAGE_TEXTURE_RIGHT"
                };
            default:
                return new String[]{
                    "IMAGE_BACKGROUNDS_EGYPT_TEXTURE_LEFT",
                    "IMAGE_BACKGROUNDS_EGYPT_TEXTURE",
                    "IMAGE_BACKGROUNDS_EGYPT_TEXTURE_RIGHT"
                };
        }
    }

    private void computeSideWidths() {
        float scale = (bgMain != null && bgMain.getRegionHeight() > 0)
            ? mainLawnHeight / bgMain.getRegionHeight()
            : 1f;
        leftWidthScaled = bgLeft != null ? bgLeft.getRegionWidth() * scale : mainLawnWidth * 0.4f;
        rightWidthScaled = bgRight != null ? bgRight.getRegionWidth() * scale : mainLawnWidth * 0.4f;
    }

    private void buildIntroPanSequence() {
        float mainCenterX = mainLawnWidth / 2f;
        float minCameraX = -leftWidthScaled + mainLawnWidth / 2f;
        float maxCameraX = mainLawnWidth + rightWidthScaled - mainLawnWidth / 2f;

        float houseCenterX = MathUtils.clamp(-leftWidthScaled / 2f, minCameraX, maxCameraX);
        float streetCenterX = MathUtils.clamp(mainLawnWidth + rightWidthScaled / 2f, minCameraX, maxCameraX);

        introSteps.clear();
        introSteps.add(new PanStep(houseCenterX, 0.9f, false));
        introSteps.add(new PanStep(streetCenterX, 2.2f, true));
        introSteps.add(new PanStep(streetCenterX, 0.9f, false));
        introSteps.add(new PanStep(mainCenterX, 2.2f, true));
        introSteps.add(new PanStep(mainCenterX, 0f, false));

        currentStepIndex = 0;
        stepElapsed = 0f;
        panStartX = houseCenterX;
        introFinished = false;

        worldCamera.position.set(houseCenterX, mainLawnHeight / 2f, 0);
        worldCamera.update();
    }

    private void updateIntroPan(float delta) {
        if (introFinished || currentStepIndex >= introSteps.size()) {
            introFinished = true;
            return;
        }

        PanStep step = introSteps.get(currentStepIndex);
        stepElapsed += delta;

        if (step.isTravel() && step.duration() > 0f) {
            float t = Math.min(1f, stepElapsed / step.duration());
            worldCamera.position.x = Interpolation.smooth.apply(panStartX, step.targetCenterX(), t);
            worldCamera.update();
        } else {
            worldCamera.position.x = step.targetCenterX();
            worldCamera.update();
        }

        if (stepElapsed >= step.duration()) {
            panStartX = step.targetCenterX();
            currentStepIndex++;
            stepElapsed = 0f;
            if (currentStepIndex >= introSteps.size()) {
                introFinished = true;
            }
        }
    }

    @Override
    protected void drawBackground(float delta) {
        if (world.getState() != GameState.PAUSED) {
            if (!introFinished) {
                updateIntroPan(delta);
            } else {
                if (daveOverlay != null && !daveOverlay.isStarted() && world.getState() == GameState.PLAYING) {
                    daveOverlay.startPresentation();
                }

                if (!world.isEndGameHandled()) {
                    if (world.getState() == GameState.WON) {
                        world.setEndGameHandled(true);
                        if (daveOverlay != null) {
                            daveOverlay.startPresentation(world.getWinningDialogs(), () -> {
                                GameMenuController.handleWinning(world);
                            });
                        } else {
                            GameMenuController.handleWinning(world);
                        }
                    } else if (world.getState() == GameState.LOST) {
                        world.setEndGameHandled(true);
                        if (daveOverlay != null) {
                            daveOverlay.startPresentation(world.getLosingDialogs(), () -> {
                                GameMenuController.handleLosing(world);
                            });
                        } else {
                            GameMenuController.handleLosing(world);
                        }
                    }
                }
            }
        }

        worldViewport.apply();
        game.batch.setProjectionMatrix(worldCamera.combined);
        game.batch.begin();
        drawLawnBackground();
        renderWorldContent(delta);
        game.batch.end();

        if (hud != null) {
            hud.update(world, delta);
        }
    }

    private void drawLawnBackground() {
        float y = 0f;
        if (bgLeft != null) game.batch.draw(bgLeft, -leftWidthScaled, y, leftWidthScaled, mainLawnHeight);
        if (bgMain != null) {
            game.batch.draw(bgMain, 0, y, mainLawnWidth, mainLawnHeight);
        } else {
            game.batch.setColor(Color.DARK_GRAY);
            game.batch.draw(UiUtils.getSolidColorRegion(Color.DARK_GRAY), 0, y, mainLawnWidth, mainLawnHeight);
            game.batch.setColor(Color.WHITE);
        }
        if (bgRight != null) game.batch.draw(bgRight, mainLawnWidth, y, rightWidthScaled, mainLawnHeight);
    }

    private void renderWorldContent(float delta) {
        if (world.getLawnMowerManager() != null) {
            float gridOffsetY = App.getCellHeight() * 0.8f;
            float visualRowHeight = App.getCellHeight() * 0.68f;

            for (LawnMower mower : world.getLawnMowerManager().getMowers()) {
                if (!mower.isSpent()) {
                    float x = (float) mower.getPositionX();
                    float y = gridOffsetY + (mower.getRow() * visualRowHeight);
                    String pamPath = getMowerPamPath(chapter);
                    String animStateName = getMowerAnimStateName(mower.getState());

                    try {
                        com.badlogic.gdx.math.Matrix4 oldMatrix = game.batch.getTransformMatrix().cpy();
                        com.badlogic.gdx.math.Matrix4 newMatrix = new com.badlogic.gdx.math.Matrix4(oldMatrix);
                        float scale = Math.min(0.45f, App.getCellHeight() / 200f);

                        newMatrix.translate(x, y, 0);
                        newMatrix.scale(scale, scale, 1f);
                        newMatrix.translate(-x, -y, 0);

                        game.batch.setTransformMatrix(newMatrix);
                        pamPlayer.draw(game.batch, pamPath, animStateName, mower.getStateTime(), x, y, true);
                        game.batch.setTransformMatrix(oldMatrix);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getMowerPamPath(Chapter chapter) {
        switch (chapter) {
            case EGYPT: return "768/INITIAL/MOWERS/MOWER_EGYPT/MOWER_EGYPT.PAM";
            case BIG_WAVE_BEACH: return "768/FULL/MOWERS/MOWER_BEACH/MOWER_BEACH.PAM";
            case DARK_AGES: return "768/FULL/MOWERS/MOWER_DARK/MOWER_DARK.PAM";
            case FROSTBITE_CAVES: return "768/FULL/MOWERS/MOWER_ICEAGE/MOWER_ICEAGE.PAM";
            default: return "768/FULL/MOWERS/MOWER_MODERN/MOWER_MODERN.PAM";
        }
    }

    private String getMowerAnimStateName(LawnMower.MowerState state) {
        switch (state) {
            case IDLE: return "idle";
            case MOVING: return "transition";
            case ATTACKING: return "attack";
            default: return "idle";
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        worldViewport.update(width, height, false);
        if (hud != null) {
            hud.resize(stage.getWidth(), stage.getHeight());
        }
    }
}
