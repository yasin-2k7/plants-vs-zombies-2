package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pvz2.Main;
import com.pvz2.controller.ChapterMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Chapter;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;

public class ChapterMenuScreen extends MenuScreen {

    TextureRegion textureRegion;
    private TextureBank textureBank;
    private PamPlayer pamPlayer;

    private final ChapterMenuController controller = new ChapterMenuController();
    private static final String LOCK_PAM_PATH = "768/INITIAL/UI/UNIVERSE/WORLD_LOCK/WORLD_LOCK.PAM";

    public ChapterMenuScreen(Main game) {
        super(game);

        FileHandle assetsFolder = Gdx.files.internal("");
        textureBank = new TextureBank("786", assetsFolder);
        textureRegion = textureBank.region("IMAGE_MAINMENU_BACKGROUND");

        pamPlayer = new PamPlayer(game.textureBank, assetsFolder);
    }

    @Override
    protected void buildUI() {
        Table contentTable = new Table();
        contentTable.defaults().padLeft(200).padRight(200);

        String[] pamPaths = {
            "768/INITIAL/WORLDMAP/ZOMBOSS_NODE_EGYPT/ZOMBOSS_NODE_EGYPT.PAM",
            "768/FULL/WORLDMAP/ZOMBOSS_NODE_ICEAGE/ZOMBOSS_NODE_ICEAGE.PAM",
            "768/FULL/WORLDMAP/ZOMBOSS_NODE_DARK/ZOMBOSS_NODE_DARK.PAM",
            "768/FULL/WORLDMAP/ZOMBOSS_NODE_BEACH/ZOMBOSS_NODE_BEACH.PAM"
        };

        Chapter[] chapters = Chapter.values();
        int unlockedChapter = App.getCurrentUser().getUnlockedChapter();

        for (int i = 0; i < Math.min(4, chapters.length); i++) {
            final String pamPath = pamPaths[i];
            final Chapter chapter = chapters[i];
            final boolean isLocked = chapter.ordinal() > unlockedChapter;

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


                    try {
                        pamPlayer.draw(batch, pamPath, "active", stateTime, centerX, centerY, true);
                    } catch (IllegalArgumentException e) {
                        pamPlayer.draw(batch, pamPath, "Active", stateTime, centerX, centerY, true);
                    }
                    if (isLocked) {
                        pamPlayer.draw(batch, LOCK_PAM_PATH, "idle", stateTime, centerX, centerY, true);
                    }
                }
            };

            nodeActor.setSize(600, 600);
            nodeActor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String result = controller.chooseChapter(chapter);
                    System.out.println(result);
                }
            });

            contentTable.add(nodeActor).size(600, 600);
        }

        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlingTime(0.4f);

        mainStack.add(scrollPane);
    }

    @Override
    protected void drawBackground(float delta) {
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.draw(textureRegion, 0, 0, stage.getWidth(), stage.getHeight());
        game.batch.end();
    }
}
