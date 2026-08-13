package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pvz2.Main;
import com.pvz2.models.miniGame.vaseBreaker.Vase;
import com.pvz2.models.miniGame.vaseBreaker.VaseBreakerLevel;
import com.pvz2.models.miniGame.vaseBreaker.VaseType;
import com.pvz2.models.zombie.Zombie;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VaseBreakerScreen extends MenuScreen {

    private final VaseBreakerLevel world;
    private OrthographicCamera worldCamera;
    private Viewport worldViewport;

    private TextureRegion lawnBackground;
    private final List<VaseGraphic> vaseGraphics = new ArrayList<>();

    private final List<ZombieGraphic> zombieGraphics = new ArrayList<>();

    private final FileHandle assetsFolder;
    private PamPlayer pamPlayer;

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

        worldCamera = new OrthographicCamera();
        worldViewport = new FillViewport(1800, 1000, worldCamera);
        worldCamera.position.set(1800 / 2f, 1000 / 2f, 0);
        worldCamera.update();

        lawnBackground = game.textureBank.region("IMAGE_BACKGROUNDS_JOUST_TEXTURE");

        initVaseGraphics();
    }

    @Override
    protected void buildUI() {
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

        worldViewport.apply();
        game.batch.setProjectionMatrix(worldCamera.combined);
        game.batch.begin();

        if (lawnBackground != null) {
            game.batch.draw(lawnBackground, 0, 0, 1800, 1000);
        }

        for (VaseGraphic vg : vaseGraphics) {
            vg.update(delta);
            vg.draw();
        }

        for (ZombieGraphic zg : zombieGraphics) {
            zg.update(delta, pamPlayer);
            zg.draw(game.batch, pamPlayer);
        }

        game.batch.end();
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
        if (Gdx.input.justTouched()) {
            Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            worldViewport.unproject(touchPoint);

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

                zombieGraphics.add(new ZombieGraphic(zombie));
            }
        }

        public void draw() {
            if (state == VaseState.BROKEN) return;

            pamPlayer.draw(game.batch, pamPath, currentClip, animTime, currentX, currentY, isLoop);
        }
    }
}
