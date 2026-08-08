package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pvz2.Main;
import com.pvz2.controller.ChapterMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Chapter;
import pvz.libpvz.pam.PamPlayer;

public class ChapterMenuScreen extends MenuScreen {

    private TextureRegion textureRegion;
    private PamPlayer pamPlayer;

    private final ChapterMenuController controller = new ChapterMenuController();
    private static final String LOCK_PAM_PATH = "768/INITIAL/UI/UNIVERSE/WORLD_LOCK/WORLD_LOCK.PAM";

    private Group contentGroup;
    private float scrollX = 0f;
    private float minScrollX = 0f;
    private float velocityX = 0f;

    private boolean isDragging = false;
    private final Vector2 touchStartPos = new Vector2();
    private final Vector2 currTouch = new Vector2();
    private final Vector2 prevTouch = new Vector2();

    public ChapterMenuScreen(Main game) {
        super(game);

        FileHandle assetsFolder = Gdx.files.internal("");
        textureRegion = game.textureBank.region("IMAGE_MAINMENU_BACKGROUND");
        pamPlayer = new PamPlayer(game.textureBank, assetsFolder);
    }

    @Override
    protected void buildUI() {
        float stageWidth = stage.getWidth();
        float stageHeight = stage.getHeight();

        contentGroup = new Group() {
            @Override
            public void act(float delta) {
                super.act(delta);
                handleViewportIndependentInput();
            }
        };

        String[] regionNames = {
            "IMAGE_WORLDMAP_ZOMBOSS_NODE_EGYPT_ZOMBOSS_NODE_EGYPT_914X994",
            "IMAGE_WORLDMAP_BEACH_ANIM27_ANIM27_1362X953",
            "IMAGE_WORLDMAP_ZOMBOSS_NODE_DARK_ZOMBOSS_NODE_DARK_905X1096",
            "IMAGE_WORLDMAP_ZOMBOSS_NODE_ICEAGE_ZOMBOSS_NODE_ICEAGE_1055X1280"
        };

        final Chapter[] chapters = Chapter.values();
        int unlockedChapter = App.getCurrentUser().getUnlockedChapter();

        float nodeSize = 600f;
        float spacing = 200f;
        float startX = 200f;

        float startY = (stageHeight - nodeSize) / 2f;

        int count = Math.min(4, chapters.length);

        for (int i = 0; i < count; i++) {
            final TextureRegion nodeRegion = game.textureBank.region(regionNames[i]);
            final Chapter chapter = chapters[i];
            final boolean isLocked = chapter.ordinal() >= unlockedChapter;

            Actor nodeActor = new Actor() {
                private float stateTime = 0f;

                @Override
                public void act(float delta) {
                    super.act(delta);
                    stateTime += delta;
                }

                @Override
                public void draw(Batch batch, float parentAlpha) {
                    float centerX = getX() + getWidth() / 2f;
                    float centerY = getY() + getHeight() / 2f;

                    if (nodeRegion != null) {
                        batch.draw(nodeRegion, getX(), getY(), getWidth(), getHeight());
                    }

                    if (isLocked) {
                        try {
                            pamPlayer.draw(batch, LOCK_PAM_PATH, "idle", stateTime, centerX, centerY, true);
                        } catch (Exception ignored) {}
                    }
                }
            };

            nodeActor.setSize(nodeSize, nodeSize);
            nodeActor.setPosition(startX + i * (nodeSize + spacing), startY);

            nodeActor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!isDragging) {
                        String result = controller.chooseChapter(chapter);
                        game.setScreen(new LevelMenuScreen(game));
                        System.out.println(result);
                    }
                }
            });

            contentGroup.addActor(nodeActor);
        }

        float totalWidth = startX * 2 + count * nodeSize + (count - 1) * spacing;
        minScrollX = Math.min(0, stageWidth - totalWidth);

        mainStack.add(contentGroup);
    }

    private void handleViewportIndependentInput() {
        if (Gdx.input.isTouched()) {
            if (Gdx.input.justTouched()) {
                touchStartPos.set(Gdx.input.getX(), Gdx.input.getY());
            }

            currTouch.set(Gdx.input.getX(), Gdx.input.getY());
            prevTouch.set(Gdx.input.getX() - Gdx.input.getDeltaX(), Gdx.input.getY() - Gdx.input.getDeltaY());

            stage.screenToStageCoordinates(currTouch);
            stage.screenToStageCoordinates(prevTouch);

            float deltaStageX = currTouch.x - prevTouch.x;

            if (!isDragging && touchStartPos.dst(Gdx.input.getX(), Gdx.input.getY()) > 10f) {
                isDragging = true;
            }

            if (isDragging) {
                scrollX = MathUtils.clamp(scrollX + deltaStageX, minScrollX, 0);
                contentGroup.setX(scrollX);
                velocityX = deltaStageX;
            }
        } else {
            if (Math.abs(velocityX) > 0.5f) {
                scrollX = MathUtils.clamp(scrollX + velocityX, minScrollX, 0);
                contentGroup.setX(scrollX);
                velocityX *= 0.92f;
            }

            if (isDragging) {
                Gdx.app.postRunnable(() -> isDragging = false);
            }
        }
    }

    @Override
    protected void drawBackground(float delta) {
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.draw(textureRegion, 0, 0, stage.getWidth(), stage.getHeight());
        game.batch.end();
    }
}
