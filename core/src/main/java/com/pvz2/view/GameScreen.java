package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pvz2.Main;
import com.pvz2.controller.GameMenuController;
import com.pvz2.controller.PlantMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.Chapter;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.lawnMower.LawnMower;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.PlantAnimationClips;
import com.pvz2.models.plant.components.ExplosivesComponent;
import com.pvz2.models.pool.GenericObjectPool;
import com.pvz2.models.projectile.Projectile;
import com.pvz2.models.world.ChapterWorld.AncientEgyptWorld;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.Sandstorm;
import com.pvz2.models.world.Sun;
import com.pvz2.models.world.obstacles.Grave;
import com.pvz2.models.zombie.Zombie;
import pvz.libpvz.pam.PamPlayer;
import com.badlogic.gdx.utils.Align;
import pvz.skin.BorderedTable;

import java.util.*;
import java.util.function.Consumer;

public class GameScreen extends MenuScreen {

    private PamPlayer pamPlayer;
    private final GameWorld world;
    private final Chapter chapter;

    private GameHUD hud;

    private Texture imitaterOverlayBg;
    private Table imitaterOverlay;

    private final TextureRegion bgLeft;
    private final TextureRegion bgMain;
    private final TextureRegion bgRight;

    private final float mainLawnWidth;
    private final float mainLawnHeight;
    private float leftWidthScaled;
    private float rightWidthScaled;

    private LawnGridDebugRenderer lawnGridDebugRenderer;

    private Vector3 cursorWorldPos = new Vector3(0, 0, 0);
    private final List<PlantGraphic> plantGraphics = new ArrayList<>();
    private final List<ExplosionEffectGraphic> explosionGraphics = new ArrayList<>();
    private final Map<Projectile, ProjectileGraphic> projectileGraphics = new HashMap<>();
    private final List<ProjectileImpactGraphic> projectileImpacts = new ArrayList<>();

    private final List<PanStep> introSteps = new ArrayList<>();
    private int currentStepIndex = 0;
    private float stepElapsed = 0f;
    private float panStartX;
    private boolean introFinished = false;

    private static final int STREET_ARRIVAL_STEP_INDEX = 1;
    private boolean introPausedAtStreet = false;
    private Table streetTable;
    private PlantDetailsTable plantDetailsTable;

    private CrazyDaveOverlay daveOverlay;
    private static GameScreen activeInstance;
    private static final List<String> pendingAnnouncements = new ArrayList<>();

    private LevelObjectivesOverlay objectivesOverlay;
    private boolean objectivesDismissed = false;

    private GameEndOverlay endGameOverlay;

    private PlantMenuController plantMenuController = new PlantMenuController();

    private final PlantPlacementManager plantPlacementManager = new PlantPlacementManager();
    private final PlantfoodPlacementManager plantfoodPlacementManager = new PlantfoodPlacementManager();
    private final ShovelPlacementManager shovelPlacementManager = new ShovelPlacementManager();

    private ZombiePreviewManager zombiePreviewManager;
    private boolean zombiePreviewVisible = true;

    private final Map<Sun, SunGraphic> sunGraphics = new HashMap<>();
    private final List<GraveGraphic> graveGraphics = new ArrayList<>();

    private final Map<Sandstorm, SandstormGraphic> sandstormGraphics = new HashMap<>();
    private record PanStep(float targetCenterX, float duration, boolean isTravel) {
    }

    public GameScreen(Main game, GameWorld world, Chapter chapter) {
        super(game);
        this.world = world;
        this.chapter = chapter;

        zombiePreviewManager = new ZombiePreviewManager(world.getWaveManager(), world.getRows(), world.getCols());  // <<< اضافه شد

        String[] keys = getBackgroundKeys(chapter);
        bgLeft = game.textureBank.region(keys[0]);
        bgMain = game.textureBank.region(keys[1]);
        bgRight = game.textureBank.region(keys[2]);

        mainLawnWidth = 1800;
        mainLawnHeight = 1000;

        initWorldCamera(mainLawnWidth, mainLawnHeight);
        lawnGridDebugRenderer = new LawnGridDebugRenderer();

        FileHandle assetsFolder = Gdx.files.internal("");
        pamPlayer = new PamPlayer(game.textureBank, assetsFolder);
        pamPlayer.loadAsync(getMowerPamPath(chapter), null);
        pamPlayer.loadAsync("768/INITIAL/EFFECTS/MOWER_SPAWN/MOWER_SPAWN.PAM", null);

        pamPlayer.loadAsync("768/INITIAL/EFFECTS/SUN/SUN.PAM", null);
        pamPlayer.loadAsync("768/FULL/EFFECTS/SUN_BOMB/SUN_BOMB.PAM", null);

        pamPlayer.loadAsync("768/INITIAL/EFFECTS/SANDSTORM_TOP/SANDSTORM_TOP.PAM", null);

        computeSideWidths();
        buildIntroPanSequence();

    }

    @Override
    public void show() {
        super.show();

        stage.setViewport(new ScreenViewport());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        activeInstance = this;
        initGraveGraphics();
        initIcyPlantGraphics();
    }

    @Override
    public void hide() {
        super.hide();
        if (activeInstance == this) {
            activeInstance = null;
        }
    }

    public static void announce(String message) {
        if (activeInstance != null) {
            activeInstance.addToast("", message, true);
        } else {
            pendingAnnouncements.add(message);
        }
    }

    private void flushPendingAnnouncements() {
        if (pendingAnnouncements.isEmpty()) return;
        for (String msg : pendingAnnouncements) {
            addToast("", msg, true);
        }
        pendingAnnouncements.clear();
    }

    @Override
    protected void buildUI() {
        hud = new GameHUD(game, skin, this, this::restartLevel);
        mainStack.addActor(hud);
        hud.setInGameDetailsVisibility(false);


        buildStreetTable();
        streetTable.setVisible(false);

        List<String> starting = world.getStartingDialogs();
        if (starting == null) {
            starting = new ArrayList<>();
        }
        daveOverlay = new CrazyDaveOverlay(game, starting, world);
        modalStack.addActor(daveOverlay);

        objectivesOverlay = new LevelObjectivesOverlay(game, skin, world.getLevelSetup(), () -> {
            objectivesDismissed = true;
        });
        modalStack.addActor(objectivesOverlay);
    }

    public void restartLevel() {
        world.reset();
        world.setEndGameHandled(false);
        for (int i = 0; i < 8; i++){
            hud.getSelectedPlantsList().getSlots()[i] = null;
        }
        hud.getSelectedPlantsList().build();

        plantGraphics.clear();
        zombieGraphics.clear();
        zombiePreviewManager = new ZombiePreviewManager(world.getWaveManager(), world.getRows(), world.getCols());  // <<< اضافه شد

        if (endGameOverlay != null) {
            endGameOverlay.remove();
            endGameOverlay = null;
        }

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

    private void showEndGameOverlay() {
        if (endGameOverlay != null) {
            endGameOverlay.remove();
        }
        endGameOverlay = new GameEndOverlay(game, skin, world, this::restartLevel);
        modalStack.addActor(endGameOverlay);
    }

    public void buildStreetTable() {
        if (streetTable == null){
            streetTable = new Table();
        }
        streetTable.clear();
        streetTable.setFillParent(true);
        streetTable.bottom().left().defaults().pad(10);

        Table panel = new BorderedTable();
        panel.left();

        plantDetailsTable = new PlantDetailsTable(PlantType.SUNFLOWER, App.getCurrentUser(), game
            , this);
        panel.add(plantDetailsTable).left().pad(5).row();
        PlantsTable plantsTable = new PlantsTable(4, 1, true, 150, 100, createSelectCardMethod());
        plantsTable.build();
        Table wrapper = new Table();
        wrapper.add(plantsTable).pad(15);
        ScrollPane scrollPane = new ScrollPane(wrapper, game.skin);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setScrollingDisabled(true, false);
        panel.add(scrollPane).padTop(10).expandX().fillX();

        TextButton continueButton = new TextButton("LET'S ROCK!", skin, "purple");
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (plantMenuController.startGame(GameScreen.this)){
                    resumeCameraToMain();
                    pendingAnnouncements.add(0, "Prepare your petals!\nIt's time to garden or die");
                    if (!world.isConveyorMode()){
                        hud.getSelectedPlantsList().activate(world.getPlantLists());
                    }
                }
            }
        });
        streetTable.add(panel).padLeft(170).padBottom(20).maxHeight(1000).width(800);
        streetTable.add().expandX();
        streetTable.add(continueButton).right().bottom().pad(20);


        mainStack.addActor(streetTable);
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
        introSteps.add(new PanStep(mainCenterX, 2.2f, true));
        introSteps.add(new PanStep(mainCenterX, 0f, false));

        currentStepIndex = 0;
        stepElapsed = 0f;
        panStartX = houseCenterX;
        introFinished = false;
        introPausedAtStreet = false;
        zombiePreviewVisible  = true;
        hideStreetTable();

        worldCamera.position.set(houseCenterX, mainLawnHeight / 2f, 0);
        worldCamera.update();
    }

    private void updateIntroPan(float delta) {
        if (introFinished || introPausedAtStreet || currentStepIndex >= introSteps.size()) {
            if (currentStepIndex >= introSteps.size()) {
                introFinished = true;
            }
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

            if (currentStepIndex == STREET_ARRIVAL_STEP_INDEX) {
                if (world.isConveyorMode()) {
                    currentStepIndex++;
                    stepElapsed = 0f;
                    return;
                } else {
                    pauseCameraAtStreet();
                    return;
                }
            }

            currentStepIndex++;
            stepElapsed = 0f;
            if (currentStepIndex >= introSteps.size()) {
                introFinished = true;
            }
        }
    }

    private void pauseCameraAtStreet() {
        introPausedAtStreet = true;
        worldCamera.position.x = introSteps.get(STREET_ARRIVAL_STEP_INDEX).targetCenterX();
        worldCamera.update();
        showStreetTable();
        hud.setSelectedPlantsVisibility();
    }

    public void resumeCameraToMain() {
        if (!introPausedAtStreet) {
            return;
        }
        introPausedAtStreet = false;
        zombiePreviewVisible = false;
        // moving effect
        hideStreetTable();
        hud.setInGameDetailsVisibility(true);

        panStartX = worldCamera.position.x;
        currentStepIndex = STREET_ARRIVAL_STEP_INDEX + 1;
        stepElapsed = 0f;
    }

    private void showStreetTable() {
        if (streetTable != null) {
            streetTable.addAction(Actions.sequence(
                Actions.moveBy(0, -1500),
                Actions.visible(true),
                Actions.moveBy(0, 1500, 1, Interpolation.bounceIn)
            ));
        }
    }

    private void hideStreetTable() {
        if (streetTable != null) {
            streetTable.addAction(Actions.sequence(
                Actions.moveBy(0, -1500, 1, Interpolation.bounceOut),
                Actions.visible(false),
                Actions.moveBy(0, 1500)
            ));
        }
    }

    @Override
    protected void drawBackground(float delta) {
        world.tick(delta);
        if (world.getState() != GameState.PAUSED) {
            if (objectivesDismissed) {
                if (!introFinished) {
                    updateIntroPan(delta);
                } else {
                    if (daveOverlay != null && !daveOverlay.isStarted() && world.getState() == GameState.PLAYING) {
                        daveOverlay.startPresentation(this::flushPendingAnnouncements);
                    }

                    if (!world.isEndGameHandled()) {
                        if (world.getState() == GameState.WON) {
                            world.setEndGameHandled(true);
                            if (daveOverlay != null) {
                                daveOverlay.startPresentation(world.getWinningDialogs(), () -> {
                                    GameMenuController.handleWinning(world);
                                    showEndGameOverlay();
                                });
                            } else {
                                GameMenuController.handleWinning(world);
                                showEndGameOverlay();
                            }
                        } else if (world.getState() == GameState.LOST) {
                            world.setEndGameHandled(true);
                            if (daveOverlay != null) {
                                daveOverlay.startPresentation(world.getLosingDialogs(), () -> {
                                    GameMenuController.handleLosing(world);
                                    showEndGameOverlay();
                                });
                            } else {
                                GameMenuController.handleLosing(world);
                                showEndGameOverlay();
                            }
                        }
                    }
                }
            }
        }



        applyWorldViewport();
        game.batch.begin();
        drawLawnBackground();
        renderWorldContent(delta);
        worldViewport.unproject(cursorWorldPos);
        plantPlacementManager.drawPreview(game.batch, cursorWorldPos);
        plantfoodPlacementManager.drawPreview(game.batch, cursorWorldPos);
        shovelPlacementManager.drawPreview(game.batch, cursorWorldPos);

        if (zombiePreviewManager != null && zombiePreviewVisible) {
            zombiePreviewManager.update(delta, pamPlayer);
            zombiePreviewManager.draw(game.batch, pamPlayer);
        }

        syncZombieGraphics();
        List<ZombieGraphic> sortedZombies = new ArrayList<>(zombieGraphics.values());
        sortedZombies.sort((z1, z2) -> Float.compare(z2.getZombie().getY(), z1.getZombie().getY()));
        for (PlantGraphic pg : plantGraphics){
            pg.update(delta);
            pg.draw(game.batch, pamPlayer);
        }
        for (ZombieGraphic zg : sortedZombies) {
            zg.update(delta, pamPlayer);
            zg.draw(game.batch, pamPlayer);
        }

        for (ExplosionEffectGraphic eg : explosionGraphics) {
            eg.update(delta);
            eg.draw(game.batch, pamPlayer);
        }
        if (world instanceof AncientEgyptWorld) {
            AncientEgyptWorld egyptWorld = (AncientEgyptWorld) world;

            for (Sandstorm sandstorm : egyptWorld.getActiveSandstorms()) {
                sandstormGraphics.computeIfAbsent(sandstorm, SandstormGraphic::new);
            }
            sandstormGraphics.keySet().removeIf(s ->
                !egyptWorld.getActiveSandstorms().contains(s) || s.isFinished()
            );

            for (SandstormGraphic sg : sandstormGraphics.values()) {
                sg.draw(game.batch, pamPlayer);
            }
        }
        explosionGraphics.removeIf(eg -> eg.isFinished(pamPlayer));
        syncSunGraphics();

        for (SunGraphic sg : new ArrayList<>(sunGraphics.values())) {
            sg.update(delta);
            sg.draw(game.batch, pamPlayer, game);
        }
        renderProjectiles(delta, game.batch, pamPlayer);
        game.batch.end();


        if (App.getCurrentUser().isShowGrid()) {
            lawnGridDebugRenderer.draw(worldCamera);
        }

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
            for (LawnMower mower : world.getLawnMowerManager().getMowers()) {
                if (!mower.isSpent()) {
                    float x = (float) mower.getPositionX();
                    float y = LawnGrid.getCellY(mower.getRow());
                    String pamPath = getMowerPamPath(chapter);
                    String animStateName = getMowerAnimStateName(mower.getState());

                    try {
                        pamPlayer.draw(game.batch, pamPath, animStateName, mower.getStateTime(),
                            x, y, 0.8f, 0.8f, true);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        syncGraveGraphics();
        for (int i = graveGraphics.size() - 1; i >= 0; i--) {
            GraveGraphic graphic = graveGraphics.get(i);
            Grave grave = graphic.getGrave(); // گرفتن مدل از گرافیک

            // آپدیت و رسم انیمیشن
            graphic.update(delta);
            graphic.draw(game.batch, pamPlayer, game);

            // اگر انیمیشن تمام شده و قبر نابود شده است
            if (grave.isDestroyed() && graphic.isBreakFinished()) {
                graveGraphics.remove(i);       // ۱. حذف از لیست گرافیکی
                world.removeObstacle(grave);   // ۲. حذف واقعی و نهایی از دنیای بازی (activeObstacles)
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
        if (hud != null && stage != null) {
            hud.resize(stage.getWidth(), stage.getHeight());
        }
    }

    @Override
    public Table createToastNotification(String title, String message, boolean urgent) {
        if (!urgent) {
            return super.createToastNotification(title, message, urgent);
        }
        Table toast = new Table();
        toast.pad(10);

        Label messageLabel = new Label(message, skin, "big_outline");
        messageLabel.setColor(new Color(0.95f, 0.16f, 0.14f, 1f));
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);
        messageLabel.setFontScale(1.3f);

        toast.add(messageLabel).width(800).center();
        return toast;
    }

    @Override
    protected void presentToast(Notif notif) {
        if (!notif.urgent) {
            super.presentToast(notif);
            return;
        }

        final Table toastTable = createToastNotification(notif.title, notif.message, true);
        final Table wrapper = new Table();
        wrapper.setFillParent(true);
        wrapper.center();
        wrapper.add(toastTable);

        toastTable.setTransform(true);
        toastTable.setOrigin(Align.center);
        toastTable.setScale(0.7f);
        toastTable.getColor().a = 0f;

        toastStack.addActor(wrapper);

        toastTable.addAction(Actions.sequence(
            Actions.parallel(
                Actions.fadeIn(0.3f),
                Actions.scaleTo(1f, 1f, 0.35f, Interpolation.swingOut)
            ),
            Actions.delay(2.4f),
            Actions.fadeOut(0.4f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    wrapper.remove();
                    showNextToast();
                }
            })
        ));
    }


    public GameHUD getHud() {
        return hud;
    }

    public PlantMenuController getPlantMenuController() {
        return plantMenuController;
    }

    private Consumer<PlantCardView> createSelectCardMethod(){
        return new Consumer<PlantCardView>() {
            @Override
            public void accept(PlantCardView plantCardView) {
                PlantType plantType = plantCardView.getType();
                if (hud.getSelectedPlantsList().hasPlant(plantType)){
                    plantMenuController.removePlant(plantType.name(),
                        GameScreen.this);
                    hud.getSelectedPlantsList().removePlant(plantType);
                    hud.getSelectedPlantsList().build();
                }
                else{
                    plantDetailsTable.reset(plantType);
                    if (plantType == PlantType.IMITATER){
                        if (App.getCurrentUser().getUnlockedPlantsLevels().getOrDefault(PlantType.IMITATER, 0) > 0){
                            showImitatorTable();
                        }
                        // create overlay and table

                        return;
                    }
                    if (plantMenuController.addPlant(plantType.name(), GameScreen.this)){
                        hud.getSelectedPlantsList().addPlant(plantType);
                        hud.getSelectedPlantsList().build();
                    }
                }
            }
        };
    }

    private Consumer<PlantCardView> createImitaterSelectMethod(){
        return new Consumer<PlantCardView>() {
            @Override
            public void accept(PlantCardView plantCardView) {
                if (plantMenuController.addPlant(PlantType.IMITATER.name(),
                    plantCardView.getType().name(), GameScreen.this)){
                    hud.getSelectedPlantsList().addPlant(PlantType.IMITATER);
                    hud.getSelectedPlantsList().setImitatorCardType(plantCardView.getType());
                    hud.getSelectedPlantsList().build();
                    hideImitatorTable();
                    //remove overlay

                }
            }
        };
    }

    private void showImitatorTable(){
        if (imitaterOverlay != null) {
            return;
        }

        imitaterOverlay = new Table();
        imitaterOverlay.setFillParent(true);
        imitaterOverlay.setTouchable(Touchable.enabled);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.6f));
        pixmap.fill();
        imitaterOverlayBg = new Texture(pixmap);
        imitaterOverlay.setBackground(new TextureRegionDrawable(new TextureRegion(imitaterOverlayBg)));
        pixmap.dispose();

        BorderedTable panel = new BorderedTable();
        panel.pad(20);

        Label title = new Label("CHOOSE A PLANT TO IMITATE", skin, "big");
        panel.add(title).padBottom(15).row();

        PlantsTable imitaterChoices = new PlantsTable(4, 10, false, 135, 90,
            createImitaterSelectMethod());
        imitaterChoices.build();

        ScrollPane scrollPane = new ScrollPane(imitaterChoices, skin);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setScrollingDisabled(true, false);
        panel.add(scrollPane).width(600).height(400).row();

        TextButton cancelBtn = new TextButton("CANCEL", skin, "brown");
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideImitatorTable();
            }
        });
        panel.add(cancelBtn).padTop(10);

        imitaterOverlay.add(panel);
        modalStack.addActor(imitaterOverlay);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        cursorWorldPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
            GameMenuController.cheatSpawnZombie("ZombieDefault", 6, 1);
        }
        Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldViewport.unproject(touchPoint);

        if (Gdx.input.justTouched()) {
            if (plantPlacementManager.isPlantSelected()) {
                int row = LawnGrid.getRowFromY(touchPoint.y);
                int col = LawnGrid.getColFromX(touchPoint.x);

                if (row >= 0 && col >= 0) {
                    Plant newPlant = GameMenuController.plantPlant(
                        plantPlacementManager.getSelectedPlant(),
                        App.getCellCenterX(col),
                        App.getCellCenterY(row)
                    );
                    if (newPlant != null) {
                        PlantGraphic pg = new PlantGraphic(newPlant, pamPlayer);
                        plantGraphics.add(pg);
                        checkExplosion(newPlant, pg);

                        plantPlacementManager.tryPlace(row, col);
                    }
                }
            } else if (plantfoodPlacementManager.isSelected()){
                int row = LawnGrid.getRowFromY(touchPoint.y);
                int col = LawnGrid.getColFromX(touchPoint.x);

                if (row >= 0 && col >= 0) {
                    GameMenuController.feedPlant(App.getCellCenterX(col), App.getCellCenterY(row));
                }
            } else if (shovelPlacementManager.isSelected()){
                int row = LawnGrid.getRowFromY(touchPoint.y);
                int col = LawnGrid.getColFromX(touchPoint.x);

                if (row >= 0 && col >= 0) {
                    GameMenuController.pluckPlant(App.getCellCenterX(col), App.getCellCenterY(row));
                }
            }
            else {
                GameMenuController.collectSun(touchPoint.x, touchPoint.y);
            }
            hud.getSelectedPlantsList().unselectPlants();
            hud.getPlantFoodBank().setSelected(false);
            hud.getShovelBtn().setChecked(false);
            plantPlacementManager.cancelSelection();
            plantfoodPlacementManager.setSelected(false);
            shovelPlacementManager.setSelected(false);
        }

    }
    private void checkExplosion(Plant newPlant, PlantGraphic pg){
        ExplosivesComponent explosives =
            newPlant.getComponent(ExplosivesComponent.class);
        if (explosives != null) {
            explosives.setExplodeCallback(owner -> {
                String fxPath = PlantAnimationClips.getExplosionPamPath(owner.getType());
                String fxClip = PlantAnimationClips.getExplosionClip(owner.getType());
                if (fxPath != null) {
                    float y=pg.getWorldY(),scaleX = 1,scaleY = 1;
                    if (newPlant.getType() == PlantType.CHERRY_BOMB){
                        y = pg.getWorldY()+100;
                    }
                    else if (newPlant.getType() == PlantType.JALAPENO){
                        y = pg.getWorldY()+20;
                        scaleX = 20;
                        scaleY = 1.5f;
                    }
                    explosionGraphics.add(new ExplosionEffectGraphic(
                        fxPath, fxClip, pg.getWorldX(), y, pamPlayer, scaleX,
                        scaleY
                    ));
                }
            });
        }
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

    private void hideImitatorTable(){
        if (imitaterOverlay != null) {
            imitaterOverlay.remove();
            imitaterOverlay = null;
        }
        if (imitaterOverlayBg != null) {
            imitaterOverlayBg.dispose();
            imitaterOverlayBg = null;
        }
    }

    private final Map<Zombie, ZombieGraphic> zombieGraphics = new HashMap<>();

    private void syncZombieGraphics() {
        for (Zombie z : world.getActiveZombies()) {
            zombieGraphics.computeIfAbsent(z, ZombieGraphic::new);
        }
        zombieGraphics.keySet().removeIf(z -> !world.getActiveZombies().contains(z));
    }
    private void syncSunGraphics() {
        for (Sun sun : world.getActiveSuns()) {
            sunGraphics.computeIfAbsent(sun, SunGraphic::new);
        }
        sunGraphics.entrySet().removeIf(entry ->
            !world.getActiveSuns().contains(entry.getKey()) && entry.getValue().isPopFinished()
        );
    }

    public List<PlantGraphic> getPlantGraphics() {
        return plantGraphics;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (lawnGridDebugRenderer != null) {
            lawnGridDebugRenderer.dispose();
        }
    }

    public PlantPlacementManager getPlantPlacementManager() {
        return plantPlacementManager;
    }

    public void initGraveGraphics() {
        graveGraphics.clear();
        for (Grave grave : world.getGraves()) {
            graveGraphics.add(new GraveGraphic(grave));
        }
    }

    public void initIcyPlantGraphics() {
        plantGraphics.clear();
        for (Plant plant : world.getActivePlants()) {
            plantGraphics.add(new PlantGraphic(plant, pamPlayer));
        }
    }

    public PlantfoodPlacementManager getPlantfoodPlacementManager() {
        return plantfoodPlacementManager;
    }

    public ShovelPlacementManager getShovelPlacementManager() {
        return shovelPlacementManager;
    }

    private void syncGraveGraphics() {
        if (world == null) return;
        List<Grave> activeGraves = world.getGraves();

        for (Grave grave : activeGraves) {
            boolean exists = false;
            for (GraveGraphic gg : graveGraphics) {
                if (gg.getGrave() == grave) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                graveGraphics.add(new GraveGraphic(grave, world));
            }
        }
    }
}
