package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pvz2.Main;
import com.pvz2.controller.LevelMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.enums.Chapter;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;

public class LevelMenuScreen extends MenuScreen {

    private TextureRegion backgroundRegion;
    private PamPlayer pamPlayer;

    private final LevelMenuController controller = new LevelMenuController(this);

    private Group contentGroup;
    private float scrollX = 0f;
    private float minScrollX = 0f;
    private float velocityX = 0f;

    private boolean isDragging = false;
    private final Vector2 touchStartPos = new Vector2();
    private final Vector2 currTouch = new Vector2();
    private final Vector2 prevTouch = new Vector2();

    public LevelMenuScreen(Main game) {
        super(game);

        FileHandle assetsFolder = Gdx.files.internal("");
        backgroundRegion = game.textureBank.region("IMAGE_MAINMENU_BACKGROUND");
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

        int totalLevels = 4;

        User currentUser = App.getCurrentUser();
        Chapter currentChapter = currentUser.getCurrentChapter();
        int unlockedLevel = currentUser.getUnlockedLevel();
        int unlockedChapterOrdinal = currentUser.getUnlockedChapter();

        boolean isChapterLocked = (currentChapter != null) && (currentChapter.ordinal() > unlockedChapterOrdinal);

        float nodeSize = 350f;
        float spacingX = 300f;
        float startX = 250f;
        float baseY = (stageHeight - nodeSize) / 2f;

        float[] yOffsets = { -120f, 150f, -100f, 130f, -80f, 100f };

        Vector2[] nodeCenters = new Vector2[totalLevels];
        Vector2[] nodePositions = new Vector2[totalLevels];

        for (int i = 0; i < totalLevels; i++) {
            float offsetY = yOffsets[i % yOffsets.length];
            float posX = startX + i * (nodeSize + spacingX);
            float posY = baseY + offsetY;

            nodePositions[i] = new Vector2(posX, posY);
            nodeCenters[i] = new Vector2(posX + nodeSize / 2f, posY + nodeSize / 2f);
        }

        final List<String> chapterPathPams = getPathPamsForChapter(currentChapter);
        final float tileSpacing = getTileSpacingForChapter(currentChapter);

        for (int i = 0; i < totalLevels - 1; i++) {
            final Vector2 c1 = nodeCenters[i];
            final Vector2 c2 = nodeCenters[i + 1];

            final float distance = c1.dst(c2);
            final float angleDeg = MathUtils.atan2(c2.y - c1.y, c2.x - c1.x) * MathUtils.radiansToDegrees;

            final int tileCount = Math.max(1, (int) (distance / tileSpacing));

            final boolean isPathUnlocked = !isChapterLocked && ((i + 1) < unlockedLevel);
            final int levelIndex = i;

            Actor pathActor = new Actor() {
                private float stateTime = 0f;
                private final Matrix4 originalMatrix = new Matrix4();
                private final Matrix4 transformMatrix = new Matrix4();

                @Override
                public void act(float delta) {
                    super.act(delta);
                    stateTime += delta;
                }

                @Override
                public void draw(Batch batch, float parentAlpha) {
                    try {
                        originalMatrix.set(batch.getTransformMatrix());

                        for (int k = 1; k < tileCount; k++) {
                            float t = (float) k / tileCount;
                            float px = MathUtils.lerp(c1.x, c2.x, t);
                            float py = MathUtils.lerp(c1.y, c2.y, t);

                            int pamIndex = (levelIndex + k) % chapterPathPams.size();
                            String selectedPam = chapterPathPams.get(pamIndex);

                            transformMatrix.set(originalMatrix);
                            transformMatrix.translate(px, py, 0);
                            transformMatrix.rotate(0, 0, 1, angleDeg);

                            batch.setTransformMatrix(transformMatrix);

                            pamPlayer.draw(batch, selectedPam, "idle", stateTime, 0, 0, true);
                        }

                        batch.setTransformMatrix(originalMatrix);
                    } catch (Exception ignored) {}
                }
            };

            contentGroup.addActor(pathActor);
        }

        for (int i = 0; i < totalLevels; i++) {
            final int levelIndex = i + 1;
            final boolean isBoss = (i == totalLevels - 1);
            final String nodePam = getNodePamForLevel(currentChapter, isBoss);

            Vector2 pos = nodePositions[i];

            final int levelStatus;
            if (isChapterLocked) {
                levelStatus = 0;
            } else if (levelIndex < unlockedLevel || currentChapter.ordinal()+1 < unlockedChapterOrdinal) {
                levelStatus = 2;
            } else if (levelIndex == unlockedLevel) {
                levelStatus = 1;
            } else {
                levelStatus = 0;
            }

            Actor levelNodeActor = new Actor() {
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

                    String animName = "locked_idle";
                    if (levelStatus == 2) animName = "finished";
                    else if (levelStatus == 1) animName = "unlocked";

                    try {
                        pamPlayer.draw(batch, nodePam, animName, stateTime, centerX, centerY, true);
                    } catch (Exception ignored) {}
                }
            };

            float currentSize = isBoss ? nodeSize * 1.25f : nodeSize;
            levelNodeActor.setSize(currentSize, currentSize);
            levelNodeActor.setPosition(pos.x, pos.y);

            levelNodeActor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isDragging) return;
                    if (levelStatus == 0) {
                        System.out.println("this level is locked!");
                        return;
                    }
                    String result = controller.chooseLevel(levelIndex);
                    System.out.println(result);
                }
            });

            contentGroup.addActor(levelNodeActor);
        }

        float maxX = nodePositions[totalLevels - 1].x;
        float totalWidth = maxX + nodeSize + startX;
        minScrollX = Math.min(0, stageWidth - totalWidth);

        mainStack.add(contentGroup);
        addBackButton();
    }

    private void addBackButton() {
        TextButton backButton = new TextButton("Back", skin);
        backButton.setSize(200f, 80f);
        backButton.setPosition(50f, stage.getHeight() - 130f);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ChapterMenuScreen(game));
            }
        });

        stage.addActor(backButton);
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
        if (backgroundRegion != null) {
            game.batch.draw(backgroundRegion, 0, 0, stage.getWidth(), stage.getHeight());
        }
        game.batch.end();
    }

    private List<String> getPathPamsForChapter(Chapter chapter) {
        if (chapter == null) {
            return List.of("768/INITIAL/WORLDMAP/PATHS/ANIM6.PAM");
        }

        switch (chapter) {
            case EGYPT:
                return List.of(
                    "768/INITIAL/WORLDMAP/EGYPT/ANIM5/ANIM5.PAM",
                    "768/INITIAL/WORLDMAP/EGYPT/ANIM6/ANIM6.PAM",
                    "768/INITIAL/WORLDMAP/EGYPT/ANIM7/ANIM7.PAM",
                    "768/INITIAL/WORLDMAP/EGYPT/ANIM9/ANIM9.PAM"
                );
            case BIG_WAVE_BEACH:
                return List.of(
                    "768/FULL/WORLDMAP/BEACH/ANIM10/ANIM10.PAM",
                    "768/FULL/WORLDMAP/BEACH/ANIM11/ANIM11.PAM",
                    "768/FULL/WORLDMAP/BEACH/ANIM12/ANIM12.PAM",
                    "768/FULL/WORLDMAP/BEACH/ANIM13/ANIM13.PAM",
                    "768/FULL/WORLDMAP/BEACH/ANIM14/ANIM14.PAM"
                );
            case DARK_AGES:
                return List.of(
                    "768/FULL/WORLDMAP/DARK/ANIM15/ANIM15.PAM",
                    "768/FULL/WORLDMAP/DARK/ANIM16/ANIM16.PAM",
                    "768/FULL/WORLDMAP/DARK/ANIM13/ANIM13.PAM",
                    "768/FULL/WORLDMAP/DARK/ANIM12/ANIM12.PAM"
                );
            case FROSTBITE_CAVES:
                return List.of(
                    "768/FULL/WORLDMAP/ICEAGE/ANIM10/ANIM10.PAM",
                    "768/FULL/WORLDMAP/ICEAGE/ANIM11/ANIM11.PAM",
                    "768/FULL/WORLDMAP/ICEAGE/ANIM12/ANIM12.PAM",
                    "768/FULL/WORLDMAP/ICEAGE/ANIM26/ANIM26.PAM",
                    "768/FULL/WORLDMAP/ICEAGE/ANIM28/ANIM28.PAM"
                );
            default:
                return List.of("768/INITIAL/WORLDMAP/PATHS/ANIM6.PAM");
        }
    }
    private float getTileSpacingForChapter(Chapter chapter) {
        if (chapter == null) return 90f;

        switch (chapter) {
            case BIG_WAVE_BEACH:
                return 180f;
            case FROSTBITE_CAVES:
                return 210f;
            case DARK_AGES:
                return 70f;
            case EGYPT:
                return 120f;
            default:
                return 90f;
        }
    }
    private String getNodePamForLevel(Chapter chapter, boolean isBoss) {
        if (chapter == null) return "768/INITIAL/WORLDMAP/LEVEL_NODE/LEVEL_NODE.PAM";

        if (isBoss) {
            return "768/INITIAL/WORLDMAP/LEVEL_NODE_GARGANTUAR/LEVEL_NODE_GARGANTUAR.PAM";
        } else {
            return "768/INITIAL/WORLDMAP/LEVEL_NODE/LEVEL_NODE.PAM";
        }
    }
}
