package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pvz2.Main;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.miniGame.vaseBreaker.Vase;
import com.pvz2.models.miniGame.vaseBreaker.VaseBreakerLevel;
import com.pvz2.models.miniGame.vaseBreaker.VaseType;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.pool.GenericObjectPool;
import com.pvz2.models.projectile.Projectile;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameState;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.view.audios.GameSFX;
import com.pvz2.view.audios.SFXManager;
import pvz.libpvz.pam.PamPlayer;

import java.util.*;

public class VaseBreakerScreen extends MenuScreen {

    private final VaseBreakerLevel world;

    private TextureRegion lawnBackground;
    private final List<VaseGraphic> vaseGraphics = new ArrayList<>();

    private final List<ZombieGraphic> zombieGraphics = new ArrayList<>();
    private final List<PlantGraphic> plantGraphics = new ArrayList<>();
    private final Map<Projectile, ProjectileGraphic> projectileGraphics = new HashMap<>();
    private final List<ProjectileImpactGraphic> projectileImpacts = new ArrayList<>();

    private final FileHandle assetsFolder;
    private PamPlayer pamPlayer;

    private final List<PlantCardView> seedPackets = new ArrayList<>();

    private final PlantPlacementManager plantPlacementManager = new PlantPlacementManager();

    private ImageButton shovelBtn;
    private ImageButton pauseBtn;
    private Table pauseOverlay;

    public enum VaseState {
        DROPPING,
        IDLE,
        BREAKING,
        BROKEN
    }

    public VaseBreakerScreen(Main game, VaseBreakerLevel world) {
        super(game);
        this.world = world;

        this.assetsFolder = Gdx.files.internal("");
        this.pamPlayer = new PamPlayer(game.textureBank, Gdx.files.internal(""));
    }

    @Override
    public void show() {
        super.show();

        initWorldCamera(1800, 1000);

        lawnBackground = game.textureBank.region("IMAGE_BACKGROUNDS_JOUST_TEXTURE");

        initVaseGraphics();
    }

    @Override
    protected void buildUI() {
        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top();

        shovelBtn = createShovelBtn();
        pauseBtn = new ImageButton(skin, "ingame_pause");
        pauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        topBar.add(shovelBtn).pad(20).left();
        topBar.add().expandX();
        topBar.add(pauseBtn).pad(20).right();

        mainStack.addActor(topBar);
    }

    private ImageButton createShovelBtn() {
        TextureRegion shovelIcon = game.textureBank.region("IMAGE_UI_HUD_INGAME_SHOVEL_BUTTON");
        TextureRegion shovelIconSelected = game.textureBank.region("IMAGE_UI_HUD_INGAME_SHOVEL_BUTTON_DOWN");

        Drawable shovel = shovelIcon != null ? new TextureRegionDrawable(shovelIcon) : null;
        Drawable shovelSelected = shovelIconSelected != null ? new TextureRegionDrawable(shovelIconSelected) : null;

        ImageButton button = new ImageButton(shovel, shovel, shovelSelected != null ? shovelSelected : shovel);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // GameMenuController.selectAndUnselectShovel() toggles
                // world.isSelectedShovel() and clears any conflicting
                // selection (plant / plant food), exactly like normal levels.
                GameMenuController.selectAndUnselectShovel();
            }
        });
        return button;
    }

    private void togglePause() {
        if (world.getState() == GameState.PLAYING) {
            world.setState(GameState.PAUSED);
            showPauseOverlay();
        } else if (world.getState() == GameState.PAUSED) {
            world.setState(GameState.PLAYING);
            hidePauseOverlay();
        }
    }

    private void showPauseOverlay() {
        if (pauseOverlay != null) {
            pauseOverlay.remove();
        }
        pauseOverlay = new Table();
        pauseOverlay.setFillParent(true);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.6f));
        pixmap.fill();
        Texture bgTexture = new Texture(pixmap);
        pixmap.dispose();
        pauseOverlay.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));

        TextButton resumeBtn = new TextButton("RESUME", skin, "purple");
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        TextButton restartBtn = new TextButton("RESTART", skin, "purple");
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                restartLevel();
            }
        });

        pauseOverlay.add(resumeBtn).pad(10).row();
        pauseOverlay.add(restartBtn).pad(10);

        modalStack.addActor(pauseOverlay);
    }

    private void hidePauseOverlay() {
        if (pauseOverlay != null) {
            pauseOverlay.remove();
            pauseOverlay = null;
        }
    }

    public void restartLevel() {
        world.reset();
        world.setState(GameState.PLAYING);
        hidePauseOverlay();

        plantGraphics.clear();
        zombieGraphics.clear();
        seedPackets.clear();
        plantPlacementManager.cancelSelection();

        initVaseGraphics();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (worldViewport != null) {
            worldViewport.update(width, height, true);
        }
    }

    @Override
    protected void drawBackground(float delta) {
        handleInput();

        world.tick(delta);

        plantGraphics.removeIf(PlantGraphic::isDead);

        applyWorldViewport();
        game.batch.begin();

        if (lawnBackground != null) {
            game.batch.draw(lawnBackground, 0, 0, 1800, 1000);
        }

        for (VaseGraphic vg : vaseGraphics) {
            vg.update(delta);
            vg.draw();
        }

        for (PlantGraphic pg : plantGraphics) {
            pg.update(delta);
            pg.draw(game.batch, pamPlayer);
        }

        for (ZombieGraphic zg : zombieGraphics) {
            zg.update(delta, pamPlayer);
            zg.draw(game.batch, pamPlayer);
        }

        renderProjectiles(delta, game.batch, pamPlayer);

        for (PlantCardView card : seedPackets) {
            card.draw(game.batch, 1f);
        }

        Vector3 cursorWorldPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldViewport.unproject(cursorWorldPos);

        plantPlacementManager.drawPreview(game.batch, cursorWorldPos);

        game.batch.end();
    }
    private void renderProjectiles(float delta, SpriteBatch batch, PamPlayer pamPlayer) {
        List<Projectile> active = world.getActiveProjectiles();
        GenericObjectPool<Projectile> pool = App.getCurrentGame().getProjectilesPool();

        Iterator<Map.Entry<Projectile, ProjectileGraphic>> it = projectileGraphics.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Projectile, ProjectileGraphic> entry = it.next();
            ProjectileGraphic pg = entry.getValue();
            boolean reused = pool.getGeneration(entry.getKey()) != pg.getGeneration();
            boolean noLongerActive = !active.contains(entry.getKey());
            if (reused || noLongerActive) {
                projectileImpacts.add(new ProjectileImpactGraphic(pg.getType(), pg.getLastX(), pg.getLastY()));
                it.remove();
            }
        }

        for (Projectile p : active) {
            int currentGen = pool.getGeneration(p);
            ProjectileGraphic existing = projectileGraphics.get(p);
            if (existing == null || existing.getGeneration() != currentGen) {
                projectileGraphics.put(p, new ProjectileGraphic(p, currentGen));
            }
        }

        for (ProjectileGraphic pg : projectileGraphics.values()) {
            pg.update(delta);
            pg.draw(batch, pamPlayer);
        }

        Iterator<ProjectileImpactGraphic> impactIt = projectileImpacts.iterator();
        while (impactIt.hasNext()) {
            ProjectileImpactGraphic ig = impactIt.next();
            ig.update(delta);
            ig.draw(batch, pamPlayer);
            if (ig.isFinished(pamPlayer)) impactIt.remove();
        }
    }


    private void initVaseGraphics() {
        vaseGraphics.clear();
        zombieGraphics.clear();
        for (Vase vase : world.getVases()) {
            float targetX = LawnGrid.getCellX(vase.getCol());
            float targetY = LawnGrid.getCellY(vase.getRow());

            vaseGraphics.add(new VaseGraphic(vase, targetX, targetY));
            if (vase.getHiddenZombie() != null) {
                String lookupName = vase.getHiddenZombie().getName().name();
                String pamPath = ZombiesTable.getZombiesAnimAddress().get(lookupName);
                if (pamPath != null) {
                    pamPlayer.loadAsync(pamPath, null);
                }
            }
        }
        vaseGraphics.sort((v1, v2) -> Float.compare(v2.targetY, v1.targetY));
    }

    private void handleInput() {
        // world.tick() already no-ops the simulation while paused, but we
        // also shouldn't accept any clicks (planting / shoveling / vases)
        // while the pause overlay is up.
        if (world.getState() == GameState.PAUSED) return;

        Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldViewport.unproject(touchPoint);

        if (Gdx.input.justTouched()) {

            if (App.getCurrentGame() != null && App.getCurrentGame().isSelectedShovel()) {
                GameMenuController.pluckPlant(touchPoint.x, touchPoint.y);
                plantGraphics.removeIf(pg -> pg.isDead() || pg.getPlant() == null || pg.getPlant().getCell() == null);
                return;
            }

            if (plantPlacementManager.isPlantSelected()) {
                int row = LawnGrid.getRowFromY(touchPoint.y);
                int col = LawnGrid.getColFromX(touchPoint.x);

                if (row >= 0 && col >= 0) {
                    PlantType selectedPlant = plantPlacementManager.getSelectedPlant();

                    boolean success = plantPlacementManager.tryPlace(row, col);

                    if (success) {
                        Cell cell = null;
                        Cell[][] grid = App.getCurrentGame().getGrid();
                        if (grid != null && row < grid.length && col < grid[0].length) {
                            cell = grid[row][col];
                        }

                        if (cell != null) {
                            // Route through Cell.handlePlanting(...), the same
                            // path GameMenuController.plantPlant(...) uses in
                            // normal levels, instead of instantiating the
                            // plant directly. This makes sure the plant is
                            // properly registered on the cell/world so it
                            // actually attacks, and that its model X/Y are
                            // stored as row/col (like every other level),
                            // which is what PlantGraphic expects.
                            boolean boosted = App.getCurrentUser() != null
                                && App.getCurrentUser().hasBoost(selectedPlant);

                            Plant plantModel = cell.handlePlanting(selectedPlant, boosted);

                            if (plantModel != null) {
                                plantModel.setX(col);
                                plantModel.setY(row);
                                plantModel.setCell(cell);

                                plantGraphics.add(new PlantGraphic(plantModel, pamPlayer));
                                SFXManager.getInstance().playSound(GameSFX.PLANT);
                            }
                        }
                    }
                } else {
                    plantPlacementManager.cancelSelection();
                }
                return;
            }

            for (int i = seedPackets.size() - 1; i >= 0; i--) {
                PlantCardView card = seedPackets.get(i);
                if (touchPoint.x >= card.getX() && touchPoint.x <= card.getX() + card.getWidth() &&
                    touchPoint.y >= card.getY() && touchPoint.y <= card.getY() + card.getHeight()) {

                    if (card.isActive()) {
                        plantPlacementManager.selectPlant(card.getType(), () -> seedPackets.remove(card));
                    }
                    return;
                }
            }

            for (int i = vaseGraphics.size() - 1; i >= 0; i--) {
                VaseGraphic vg = vaseGraphics.get(i);
                if (vg.contains(touchPoint.x, touchPoint.y)) {
                    vg.onClicked();
                    break;
                }
            }
        }
    }

    private class VaseGraphic {
        private final Vase vase;
        private final float targetX;
        private final float targetY;
        private float currentX;
        private float currentY;

        private VaseState state = VaseState.DROPPING;
        private float dropTimer = 0f;
        private final float dropDuration = 0.77f;

        private float animTime = 0f;
        private String currentClip;
        private boolean isLoop = true;
        private final String pamPath;
        private boolean contentSpawned = false;

        private static final Random random = new Random();

        public VaseGraphic(Vase vase, float targetX, float targetY) {
            this.vase = vase;
            this.targetX = targetX;
            this.targetY = targetY;

            this.currentX = targetX;
            this.currentY = targetY + 600f;

            this.pamPath = getPamPathForType(vase.getType());
            this.currentClip = "idle";

            pamPlayer.loadAsync(pamPath, null);
        }

        private String getPamPathForType(VaseType type) {
            if (type == VaseType.PLANT) return "768/FULL/VASEBREAKER/VASE_GREEN/VASE_GREEN.PAM";
            if (type == VaseType.GIANT) return "768/FULL/VASEBREAKER/VASE_GARGANTUAR/VASE_GARGANTUAR.PAM";
            return "768/FULL/VASEBREAKER/VASE_BROWN/VASE_BROWN.PAM";
        }

        public boolean contains(float x, float y) {
            float width = 110f;
            float height = 110f;

            float minX = currentX - (width / 2f);
            float maxX = currentX + (width / 2f);

            float minY = currentY - 30f;
            float maxY = currentY + height - 30f;

            return state == VaseState.IDLE &&
                x >= minX && x <= maxX &&
                y >= minY && y <= maxY;
        }

        public void onClicked() {
            if (state == VaseState.IDLE) {
                startBreak();
            }
        }

        public void startBreak() {
            if (state == VaseState.BREAKING || state == VaseState.BROKEN) return;

            state = VaseState.BREAKING;
            animTime = 0f;
            currentClip = random.nextBoolean() ? "break" : "break2";
            isLoop = false;
        }

        public void update(float delta) {
            animTime += delta;

            if (state == VaseState.DROPPING) {
                dropTimer += delta;
                float progress = Math.min(1f, dropTimer / dropDuration);
                currentY = Interpolation.bounceOut.apply(targetY + 600f, targetY, progress);

                if (progress >= 1f) {
                    currentY = targetY;
                    state = VaseState.IDLE;
                    currentClip = "idle";
                    animTime = 0f;
                }
            } else if (state == VaseState.BREAKING) {
                float clipDuration = pamPlayer.clipDurationSeconds(pamPath, currentClip);
                if (!contentSpawned && animTime >= clipDuration * 0.1f) {
                    contentSpawned = true;
                    spawnContent();
                }
                if (animTime >= clipDuration) {
                    state = VaseState.BROKEN;
                }
            }
        }

        private void spawnContent() {
            world.breakVaseAt(vase.getRow(), vase.getCol());

            if (vase.getHiddenZombie() != null) {
                Zombie zombie = vase.getHiddenZombie();

                float startX = LawnGrid.getCellX(vase.getCol());
                float startY = LawnGrid.getCellY(vase.getRow());

                zombie.setX(startX);
                zombie.setY(startY);

                // Register the zombie in the actual game world (same list
                // GameMenuController.cheatSpawnZombie uses) so plants can
                // detect and attack it and world.tick() actually moves it.
                // Previously the zombie only existed in this screen's local
                // list, so it was rendered but never "seen" by the game.
                world.getActiveZombies().add(zombie);

                zombieGraphics.add(new ZombieGraphic(zombie));
            }
            else if (vase.getHiddenSeed() != null) {
                PlantType plantType = vase.getHiddenSeed().getPlantType();
                float x = LawnGrid.getCellX(vase.getCol());
                float y = LawnGrid.getCellY(vase.getRow());

                int level = App.getCurrentUser() != null ?
                    App.getCurrentUser().getUnlockedPlantsLevels().getOrDefault(plantType, 1) : 1;
                int cost = 0;

                PlantCardView card = new PlantCardView(true, false, false, level, cost, plantType);

                float width = 100f;
                float height = 130f;
                card.setSize(width, height);

                card.clearChildren();
                card.build();

                card.setPosition(x - width / 2f, y - height / 2f);

                card.setClickMethod(clickedCard -> {
                    plantPlacementManager.selectPlant(clickedCard.getType(), () -> {
                        seedPackets.remove(clickedCard);
                    });
                });

                seedPackets.add(card);
            }
        }

        public void draw() {
            if (state == VaseState.BROKEN) return;

            pamPlayer.draw(game.batch, pamPath, currentClip, animTime, currentX, currentY, isLoop);
        }
    }
}
