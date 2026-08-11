package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.pvz2.Main;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserDataManager;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.wave.WaveManager;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import pvz.skin.BorderedTable;

public class GameHUD extends Group {

    private static final float MARGIN = 20f;

    private final Table topBar;
    private final SunCounter sunCounter;
    private final WaveProgressBar waveProgressBar;
    private final ResourcesTable resourcesTable;
    private final PlantFoodBank plantFoodBank;

    private PauseMenuOverlay activeOverlay;

    public GameHUD(Main game, Skin skin, Runnable onRestart) {
        User user = App.getCurrentUser();

        sunCounter = new SunCounter(game, skin);
        waveProgressBar = new WaveProgressBar(game);
        resourcesTable = new ResourcesTable(user, game);
        plantFoodBank = new PlantFoodBank(game);

        ImageButton pauseBtn = new ImageButton(skin, "ingame_pause");

        pauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameWorld world = App.getCurrentGame();
                if (world == null) return;

                if (activeOverlay != null) {
                    activeOverlay.remove();
                    activeOverlay = null;
                }

                if (world.getState() == GameState.PLAYING) {
                    world.setState(GameState.PAUSED);
                    try {
                        activeOverlay = new PauseMenuOverlay(game, skin, world, onRestart, () -> activeOverlay = null);
                        getStage().addActor(activeOverlay);
                    } catch (Exception e) {
                        e.printStackTrace();
                        world.setState(GameState.PLAYING);
                        activeOverlay = null;
                    }
                }
            }
        });
        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelMenuScreen(game));
            }
        });

        topBar = new Table();
        topBar.add(backBtn).left().pad(MARGIN);
        topBar.add(sunCounter).left().pad(MARGIN);

        topBar.add(waveProgressBar).expandX().center().padTop(MARGIN);
        topBar.add(resourcesTable).right().pad(MARGIN);
        topBar.add(pauseBtn).right().pad(MARGIN);

        addActor(topBar);
        addActor(plantFoodBank);
    }

    public void resize(float stageWidth, float stageHeight) {
        topBar.pack();
        topBar.setSize(stageWidth, topBar.getHeight());
        topBar.invalidate();
        topBar.validate();
        topBar.setPosition(0, stageHeight - topBar.getHeight());

        plantFoodBank.setPosition(MARGIN, MARGIN);
    }

    public void update(GameWorld world, float delta) {
        sunCounter.update(world);
        plantFoodBank.update(world);
        resourcesTable.update();
        waveProgressBar.update(world != null ? world.getWaveManager() : null, delta);
    }

    private static class PauseMenuOverlay extends Table {
        private final Texture backgroundTexture;
        private final Runnable onClosed;

        public PauseMenuOverlay(Main game, Skin skin, GameWorld world, Runnable onRestart, Runnable onClosed) {
            this.onClosed = onClosed;
            setFillParent(true);

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(new Color(0, 0, 0, 0.6f));
            pixmap.fill();
            backgroundTexture = new Texture(pixmap);
            setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
            pixmap.dispose();

            BorderedTable frame = new BorderedTable();
            frame.pad(40, 30, 30, 30);

            Group toppersGroup = new Group();
            TextureRegion grassReg = game.textureBank.region("IMAGE_UI_PAUSEMENU_WINDOWTOPPER");
            TextureRegion flowerReg = game.textureBank.region("IMAGE_UI_PAUSEMENU_SUNFLOWER_TOPPER");

            if (grassReg != null && flowerReg != null) {
                Image grass = new Image(grassReg);
                Image flower = new Image(flowerReg);

                grass.setPosition(-grass.getWidth() / 2f, 0);
                flower.setPosition(-flower.getWidth() / 2f, grass.getHeight() * 0.4f);

                toppersGroup.addActor(grass);
                toppersGroup.addActor(flower);
            }

            Label title = new Label("GAME PAUSED", skin, "big");
            title.setAlignment(Align.center);

            TextButton resumeBtn = new TextButton("RESUME", skin, "purple");
            TextButton restartBtn = new TextButton("RESTART", skin, "brown");
            TextButton exitBtn = new TextButton("SAVE AND EXIT", skin, "green");

            resumeBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    world.setState(GameState.PLAYING);
                    remove();
                }
            });

            exitBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    User user = App.getCurrentUser();
                    if (user != null) {
                        UserDataManager.saveUser(user);
                    }
                    remove();
                    game.setScreen(new LevelMenuScreen(game));
                }
            });

            restartBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    remove();
                    if (onRestart != null) {
                        onRestart.run();
                    }
                }
            });

            Table buttonsTable = new Table();
            buttonsTable.add(exitBtn).pad(10).width(160);
            buttonsTable.add(restartBtn).pad(10).width(160);
            buttonsTable.add(resumeBtn).pad(10).width(160);

            frame.add(title).padBottom(20).row();
            frame.add(buttonsTable).padTop(10);

            add(toppersGroup).padBottom(-20).row();
            add(frame);
        }

        @Override
        public boolean remove() {
            boolean removed = super.remove();
            backgroundTexture.dispose();
            if (onClosed != null) {
                onClosed.run();
            }
            return removed;
        }
    }


    private static class SunCounter extends Table {
        private final Label sunLabel;

        SunCounter(Main game, Skin skin) {
            TextureRegion bgRegion = game.textureBank.region("IMAGE_UI_HUD_INGAME_BACKGROUND_3SLICE");
            if (bgRegion != null) {
                setBackground(new TextureRegionDrawable(bgRegion));
            } else {
                setBackground(UiUtils.darkChipBackground());
            }

            pad(5f, 10f, 5f, 25f);

            Image sunIcon = new Image(game.textureBank.region("IMAGE_UI_HUD_INGAME_SUN_DOWN"));
            sunIcon.setScaling(Scaling.fit);

            sunLabel = new Label("0", skin, "big_outline");

            add(sunIcon).size(60f).padRight(8f);
            add(sunLabel).left();
        }

        void update(GameWorld world) {
            if (world == null) return;
            sunLabel.setText(String.valueOf(world.getSun()));
        }
    }

    private static class WaveProgressBar extends Actor {
        private final TextureRegion meterBackground;
        private final TextureRegion zombieHead;
        private final TextureRegion solidGreen;

        private float fillInsetLeftPct = 0.05f;
        private float fillInsetRightPct = 0.08f;
        private float fillInsetTopPct = 0.30f;
        private float fillInsetBottomPct = 0.30f;

        private float displayedProgress = 0f;

        WaveProgressBar(Main game) {
            meterBackground = game.textureBank.region("IMAGE_UI_HUD_INGAME_ZOMBOSS_PROGRESS_METER");
            zombieHead = game.textureBank.region("IMAGE_UI_HUD_INGAME_PROGRESS_METER_ZOMBIEHEAD");
            solidGreen = UiUtils.getSolidColorRegion(new Color(0.35f, 0.85f, 0.25f, 1f));

            if (meterBackground != null) {
                setSize(meterBackground.getRegionWidth(), meterBackground.getRegionHeight());
            } else {
                setSize(500f, 60f);
            }
        }

        void update(WaveManager waveManager, float delta) {
            float target = (waveManager != null) ? MathUtils.clamp(waveManager.getOverallProgress(), 0f, 1f) : 0f;
            displayedProgress = MathUtils.lerp(displayedProgress, target, Math.min(1f, delta * 4f));
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            float x = getX(), y = getY(), w = getWidth(), h = getHeight();

            if (meterBackground != null) batch.draw(meterBackground, x, y, w, h);

            float trackX = x + w * fillInsetLeftPct;
            float trackY = y + h * fillInsetBottomPct;
            float trackW = w * (1f - fillInsetLeftPct - fillInsetRightPct);
            float trackH = h * (1f - fillInsetTopPct - fillInsetBottomPct);

            float fillW = trackW * displayedProgress;
            if (fillW > 0f) batch.draw(solidGreen, trackX, trackY, fillW, trackH);

            if (zombieHead != null) {
                float headSize = h * 1.3f;
                float headX = trackX + fillW - headSize / 2f;
                float headY = y + h / 2f - headSize / 2f;
                batch.draw(zombieHead, headX, headY, headSize, headSize);
            }
        }
    }


    private static class PlantFoodBank extends Actor {
        private static final int MAX_PLANT_FOOD = 3;

        private final TextureRegion bankIcon;
        private final TextureRegion solidGreen;

        private final float[][] pipOffsetsPct = {
            {0.42f, 0.5f},
            {0.58f, 0.5f},
            {0.74f, 0.5f}
        };
        private final float pipSizePct = 0.14f;

        private int currentPlantFoods = 0;

        PlantFoodBank(Main game) {
            bankIcon = game.textureBank.region("IMAGE_UI_HUD_INGAME_PLANTFOOD_BANK");
            solidGreen = UiUtils.getSolidColorRegion(new Color(0.35f, 0.85f, 0.25f, 1f));

            if (bankIcon != null) {
                setSize(bankIcon.getRegionWidth(), bankIcon.getRegionHeight());
            } else {
                setSize(220f, 90f);
            }
        }

        void update(GameWorld world) {
            if (world == null) return;
            currentPlantFoods = Math.min(MAX_PLANT_FOOD, world.getPlantFoods());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            float x = getX(), y = getY(), w = getWidth(), h = getHeight();

            if (bankIcon != null) batch.draw(bankIcon, x, y, w, h);

            float pipSize = Math.min(w, h) * pipSizePct;
            for (int i = 0; i < MAX_PLANT_FOOD; i++) {
                if (i >= currentPlantFoods) continue;
                float cx = x + w * pipOffsetsPct[i][0];
                float cy = y + h * pipOffsetsPct[i][1];
                batch.draw(solidGreen, cx - pipSize / 2f, cy - pipSize / 2f, pipSize, pipSize);
            }
        }
    }
}
