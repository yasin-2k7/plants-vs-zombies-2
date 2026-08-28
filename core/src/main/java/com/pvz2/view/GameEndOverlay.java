package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pvz2.Main;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserDataManager;
import com.pvz2.models.core.UserManager;
import com.pvz2.models.miniGame.MiniGameWorld;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.levelSetup.DeadLineLevelSetup;
import com.pvz2.models.miniGame.IZombie.IZombieLevel;
import pvz.skin.BorderedTable;

public class GameEndOverlay extends Table {

    private final Texture backgroundTexture;

    public GameEndOverlay(Main game, Skin skin, GameWorld world, Runnable onRestart) {
        setFillParent(true);

        this.backgroundTexture = createBackgroundTexture();
        setBackground(new TextureRegionDrawable(new TextureRegion(this.backgroundTexture)));

        boolean won = world.getState() == GameState.WON;

        BorderedTable frame = new BorderedTable();
        frame.pad(40, 30, 30, 30);

        frame.add(createTitleLabel(skin, won)).padBottom(20).row();
        frame.add(createStatusLabel(skin, world, won)).width(480f).padBottom(20).row();
        frame.add(createButtonsTable(game, skin, world, onRestart));

        add(frame);
    }

    private Texture createBackgroundTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.6f));
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Label createTitleLabel(Skin skin, boolean won) {
        Label title = new Label(won ? "LEVEL COMPLETE!" : "GAME OVER", skin, "big");
        title.setColor(won ? Color.BLACK : new Color(0.55f, 0.05f, 0.05f, 1f));
        title.setAlignment(Align.center);
        return title;
    }

    private Label createStatusLabel(Skin skin, GameWorld world, boolean won) {
        Label status = new Label(buildStatusMessage(world, won), skin, "medium");
        status.setColor(Color.BLACK);
        status.setWrap(true);
        status.setAlignment(Align.center);
        return status;
    }

    private Table createButtonsTable(Main game, Skin skin, GameWorld world, Runnable onRestart) {
        TextButton restartBtn = new TextButton("RESTART", skin, "brown");
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
                if (onRestart != null) {
                    onRestart.run();
                }
            }
        });

        TextButton exitBtn = new TextButton("EXIT", skin, "green");
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                User user = App.getCurrentUser();
                if (user != null) {
                    user.save();
                    UserManager.syncCurrentUser();
                }
                remove();
                App.setCurrentGame(null);
                if (world instanceof MiniGameWorld) {
                    game.setScreen(new MainMenuScreen(game));
                } else {
                    game.setScreen(new LevelMenuScreen(game));
                }
            }
        });

        Table buttonsTable = new Table();
        buttonsTable.add(exitBtn).pad(10).width(180);
        buttonsTable.add(restartBtn).pad(10).width(180);

        return buttonsTable;
    }

    private String buildStatusMessage(GameWorld world, boolean won) {
        if (won) {
            if (world instanceof IZombieLevel) {
                return "Delicious! You ate all the brains and won the level!";
            }
            return "You defended your lawn and cleared every wave. Nice work, neighbor!";
        }

        if (world instanceof IZombieLevel) {
            return "You ran out of zombies and failed to eat all the brains!";
        }
        if (world.getLevelSetup() instanceof DeadLineLevelSetup) {
            return "A zombie crossed the marked line. Better luck next time!";
        }
        return "The zombies got through and ate your brains. Try again!";
    }

    @Override
    public boolean remove() {
        boolean removed = super.remove();
        backgroundTexture.dispose();
        return removed;
    }
}
