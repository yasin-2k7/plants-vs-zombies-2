package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pvz2.Main;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Chapter;
import com.pvz2.models.lawnMower.LawnMower;
import com.pvz2.models.world.GameWorld;
import pvz.libpvz.pam.PamPlayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private PamPlayer pamPlayer;
    private final Main game;
    private final GameWorld world;
    private final Chapter chapter;

    private final OrthographicCamera worldCamera;
    private final Viewport worldViewport;

    private final Stage hudStage;
    private final GameHUD hud;

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

    private record PanStep(float targetCenterX, float duration, boolean isTravel) {
    }

    public GameScreen(Main game, GameWorld world, Chapter chapter) {
        this.game = game;
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

        hudStage = new Stage(new ScreenViewport(), game.batch);
        hud = new GameHUD(game, game.skin);
        hudStage.addActor(hud);

        FileHandle assetsFolder = Gdx.files.internal("");
        pamPlayer = new PamPlayer(game.textureBank, assetsFolder);

        pamPlayer.loadAsync(getMowerPamPath(chapter), null);

        computeSideWidths();
        buildIntroPanSequence();

        Gdx.input.setInputProcessor(hudStage);
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
        introSteps.add(new PanStep(houseCenterX, 0.9f, false));   // hold on house
        introSteps.add(new PanStep(streetCenterX, 2.2f, true));   // pan to street
        introSteps.add(new PanStep(streetCenterX, 0.9f, false));  // hold on street
        introSteps.add(new PanStep(mainCenterX, 2.2f, true));     // pan back to lawn
        introSteps.add(new PanStep(mainCenterX, 0f, false));      // fixed forever after

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

    public boolean isIntroFinished() {
        return introFinished;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!introFinished) updateIntroPan(delta);

        worldViewport.apply();
        game.batch.setProjectionMatrix(worldCamera.combined);
        game.batch.begin();
        drawBackground();
        renderWorldContent(delta);
        game.batch.end();

        hud.update(world, delta);
        hudStage.act(delta);
        hudStage.getViewport().apply();
        hudStage.draw();
    }

    private void drawBackground() {
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
            case EGYPT:
                return "768/INITIAL/MOWERS/MOWER_EGYPT/MOWER_EGYPT.PAM";
            case BIG_WAVE_BEACH:
                return "768/FULL/MOWERS/MOWER_BEACH/MOWER_BEACH.PAM";
            case DARK_AGES:
                return "768/FULL/MOWERS/MOWER_DARK/MOWER_DARK.PAM";
                case FROSTBITE_CAVES:
                    return "768/FULL/MOWERS/MOWER_ICEAGE/MOWER_ICEAGE.PAM";
            default:
                return "768/FULL/MOWERS/MOWER_MODERN/MOWER_MODERN.PAM";
        }
    }

    private String getMowerAnimStateName(LawnMower.MowerState state) {
        switch (state) {
            case IDLE:
                return "idle";
            case MOVING:
                return "transition";
            case ATTACKING:
                return "attack";
            default:
                return "idle";
        }
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, false);
        hudStage.getViewport().update(width, height, true);
        hud.resize(hudStage.getWidth(), hudStage.getHeight());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(hudStage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        hudStage.dispose();
    }
}
