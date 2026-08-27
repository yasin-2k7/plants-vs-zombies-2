package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.pvz2.Main;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserDataManager;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.miniGame.beghouled.BeghouledMechanics;
import com.pvz2.models.miniGame.beghouled.BeghouledSetup;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.plant.card.PlantCardFactory;
import com.pvz2.models.world.GameState;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.zombie.wave.WaveManager;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pvz2.view.audios.GameSFX;
import com.pvz2.view.audios.SFXManager;
import pvz.skin.BorderedTable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class GameHUD extends Group {

    private static final float MARGIN = 20f;

    private final Table topBar;
    private final SunCounter sunCounter;
    private final ImageButton shovelBtn;
    private final WaveProgressBar waveProgressBar;
    private final ResourcesTable resourcesTable;
    private final PlantFoodBank plantFoodBank;
    private final SelectedPlantsList selectedPlantsList;
    private final GameScreen screen;
    private final ConveyorBeltView conveyorBeltView;

    private PauseMenuOverlay activeOverlay;

    public GameHUD(Main game, Skin skin, GameScreen screen, Runnable onRestart) {
        this.screen = screen;
        User user = App.getCurrentUser();

        sunCounter = new SunCounter(game, skin);
        waveProgressBar = new WaveProgressBar(game);
        resourcesTable = new ResourcesTable(user, game);
        plantFoodBank = new PlantFoodBank(game, skin, screen);
        selectedPlantsList = new SelectedPlantsList(1, 1, false,
            150, 100, createSelectingMethod(), game);

        conveyorBeltView = new ConveyorBeltView(screen);

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

        shovelBtn = createShovelBtn(game);

        topBar = new Table();
        topBar.add(sunCounter).left().pad(MARGIN);
        topBar.add(shovelBtn).pad(5);

        topBar.add(waveProgressBar).expandX().center().padTop(MARGIN);
        topBar.add(resourcesTable).right().pad(MARGIN);
        topBar.add(pauseBtn).right().pad(MARGIN).row();
        topBar.add(selectedPlantsList).left().pad(MARGIN);

        addActor(topBar);
        addActor(plantFoodBank);
        addActor(conveyorBeltView);
    }

    private ImageButton createShovelBtn(Main game) {
        TextureRegion shovelIcon;
        TextureRegion shovelIconSelected;
        shovelIcon = game.textureBank.region("IMAGE_UI_HUD_INGAME_SHOVEL_BUTTON");
        shovelIconSelected = game.textureBank.region("IMAGE_UI_HUD_INGAME_SHOVEL_BUTTON_DOWN");
        Drawable shovel = new TextureRegionDrawable(shovelIcon);
        Drawable shovelSelected = new TextureRegionDrawable(shovelIconSelected);
        ImageButton imageButton = new ImageButton(shovel, shovel, shovelSelected);
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (imageButton.isChecked()) {
                    if (GameMenuController.selectAndUnselectShovel()) {
                        screen.getShovelPlacementManager().setSelected(true);
                    }
                } else {
                    GameMenuController.selectAndUnselectShovel();
                    screen.getShovelPlacementManager().setSelected(false);
                }
            }
        });
        return imageButton;
    }


   private Consumer<PlantCardView> createSelectingMethod(){

        return new Consumer<PlantCardView>() {
            @Override
            public void accept(PlantCardView plantCardView) {
                if (selectedPlantsList.isAcitve){
                    boolean isSelected =
                        GameMenuController.selectAndUnselectPlant(plantCardView.getType(), screen);
                    for (PlantCardView cardView : selectedPlantsList.plantCardViewList){
                        cardView.setSelectedState(false);
                    }
                    if (isSelected){
                        SFXManager.getInstance().playSound(GameSFX.SEED_LIFT);
                        plantCardView.setSelectedState(true);
                        screen.getPlantPlacementManager().selectPlant(plantCardView.getType(),
                            null);
                    }
                }
            }
        };
   }

    public void resize(float stageWidth, float stageHeight) {
        topBar.pack();
        topBar.setSize(stageWidth, topBar.getHeight());
        topBar.invalidate();
        topBar.validate();
        topBar.setPosition(0, stageHeight - topBar.getHeight());

        if (conveyorBeltView != null) {
            float x = (stageWidth - conveyorBeltView.getWidth()) / 2f;
            float y = stageHeight - conveyorBeltView.getHeight() - 5f;
            conveyorBeltView.setPosition(x, y);
        }

        plantFoodBank.setPosition(10*MARGIN, MARGIN);
    }

    public void update(GameWorld world, float delta) {
        if (conveyorBeltView != null) {
            conveyorBeltView.update(world);
        }
        sunCounter.update(world);
        plantFoodBank.update(world);
        resourcesTable.update();
        waveProgressBar.update(world != null ? world.getWaveManager() : null, delta);
        selectedPlantsList.update();
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
                    if(world.getLevelSetup() instanceof BeghouledSetup){
                        game.setScreen(new MainMenuScreen(game));
                    } else{
                        game.setScreen(new LevelMenuScreen(game));
                    }

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
            if (App.getCurrentUser().isDebugMode()){
                ImageButton buyBtn = MainMenuScreen.createImageButton("IMAGE_UI_HUD_INGAME_COIN_BUY",
                    "IMAGE_UI_HUD_INGAME_COIN_BUY_DOWN",
                    game.textureBank);
                buyBtn.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameWorld world = App.getCurrentGame();
                        world.addSunToPlayer(100);
                        update(world);
                    }
                });
                add(buyBtn).size(40,40).pad(5);
            }
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
        private final TextureRegion waveFlag;

        private float fillInsetLeftPct = 0.05f;
        private float fillInsetRightPct = 0.08f;
        private float fillInsetTopPct = 0.30f;
        private float fillInsetBottomPct = 0.30f;

        private float displayedProgress = 0f;
        private int totalWaves = 0;

        WaveProgressBar(Main game) {
            meterBackground = game.textureBank.region("IMAGE_UI_HUD_INGAME_ZOMBOSS_PROGRESS_METER");
            zombieHead = game.textureBank.region("IMAGE_UI_HUD_INGAME_PROGRESS_METER_ZOMBIEHEAD");
            solidGreen = UiUtils.getSolidColorRegion(new Color(0.35f, 0.85f, 0.25f, 1f));
            waveFlag = game.textureBank.region("IMAGE_UI_CLAIM_SMALL");

            if (meterBackground != null) {
                setSize(meterBackground.getRegionWidth(), meterBackground.getRegionHeight());
            } else {
                setSize(500f, 60f);
            }
        }

        void update(WaveManager waveManager, float delta) {
            float target = (waveManager != null) ? MathUtils.clamp(waveManager.getOverallProgress(), 0f, 1f) : 0f;
            displayedProgress = MathUtils.lerp(displayedProgress, target, Math.min(1f, delta * 4f));
            totalWaves = (waveManager != null) ? waveManager.getTotalWavesCount() : 0;
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

            if (waveFlag != null && totalWaves > 1) {
                float flagSize = h * 0.9f;
                for (int i = 1; i < totalWaves; i++) {
                    float frac = (float) i / totalWaves;
                    float flagX = trackX + trackW * frac - flagSize / 2f;
                    float flagY = y + h / 2f - flagSize / 2f;
                    batch.draw(waveFlag, flagX, flagY, flagSize, flagSize);
                }
            }

            if (zombieHead != null) {
                float headSize = h * 1.3f;
                float headX = trackX + fillW - headSize / 2f;
                float headY = y + h / 2f - headSize / 2f;
                batch.draw(zombieHead, headX, headY, headSize, headSize);
            }
        }
    }

    public static class PlantFoodBank extends Group {
        private static final int MAX_PLANT_FOOD = 3;
        private static final float[] PIP_OFFSET_X_PCT = {0.422f, 0.545f, 0.655f};
        private static final float PIP_OFFSET_Y_PCT = 0.5f;

        private final ImageButton plantfoodBtn;
        private final BankVisual visual;
        private final CheckBox[] pips = new CheckBox[MAX_PLANT_FOOD];

        private final TextureRegion leafIcon;
        private final TextureRegion leafIconSelected;


        PlantFoodBank(Main game, Skin skin, GameScreen screen) {
            visual = new BankVisual(game);
            addActor(visual);
            setSize(visual.getWidth(), visual.getHeight());

            leafIcon = game.textureBank.region("IMAGE_UI_HUD_INGAME_PLANTFOOD_BUTTON");
            leafIconSelected = game.textureBank.region("IMAGE_UI_HUD_INGAME_PLANTFOOD_BUTTON_DOWN");
            Drawable leaf = new TextureRegionDrawable(leafIcon);
            Drawable leafSelected = new TextureRegionDrawable(leafIconSelected);

            plantfoodBtn = new ImageButton(leaf, leaf, leafSelected);

            Table plantfoodBtnWrapper = new Table();
            plantfoodBtnWrapper.add(plantfoodBtn);
            addActor(plantfoodBtnWrapper);
            plantfoodBtnWrapper.moveBy(45,45);

            plantfoodBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (plantfoodBtn.isChecked()) {
                        if (GameMenuController.selectAndUnselectPlantfood()) {
                            screen.getPlantfoodPlacementManager().setSelected(true);
                        }
                    } else {
                        GameMenuController.selectAndUnselectPlantfood();
                        screen.getPlantfoodPlacementManager().setSelected(false);
                    }
                }
            });



            for (int i = 0; i < MAX_PLANT_FOOD; i++) {
                CheckBox pip = new CheckBox("", skin);
                pip.setTouchable(Touchable.disabled);
                pips[i] = pip;
                addActor(pip);
            }
            positionPips();

            if (App.getCurrentUser().isDebugMode()) {
                ImageButton buyBtn = MainMenuScreen.createImageButton("IMAGE_UI_HUD_INGAME_COIN_BUY",
                    "IMAGE_UI_HUD_INGAME_COIN_BUY_DOWN", game.textureBank);
                float btnSize = 38f;
                buyBtn.setSize(btnSize, btnSize);
                buyBtn.setPosition(visual.getWidth() + 8f, visual.getHeight() / 2f - btnSize / 2f);
                buyBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameWorld world = App.getCurrentGame();
                        if (world != null && world.getPlantFoods() < MAX_PLANT_FOOD) {
                            world.setPlantFoods(world.getPlantFoods() + 1);
                            update(world);
                        }
                    }
                });
                addActor(buyBtn);
                setSize(visual.getWidth() + btnSize + 8f, visual.getHeight());
            }
        }

        private void positionPips() {
            float w = visual.getWidth();
            float h = visual.getHeight();
            for (int i = 0; i < MAX_PLANT_FOOD; i++) {
                CheckBox pip = pips[i];
                pip.pack();
                float cx = w * PIP_OFFSET_X_PCT[i];
                float cy = h * PIP_OFFSET_Y_PCT;
                pip.setPosition(cx - pip.getWidth() / 2f, cy - pip.getHeight() / 2f);
            }
        }

        void update(GameWorld world) {
            if (world == null) return;
            int current = Math.min(MAX_PLANT_FOOD, world.getPlantFoods());
            for (int i = 0; i < MAX_PLANT_FOOD; i++) {
                pips[i].setChecked(i < current);
            }
        }

        public void setSelected(boolean state){
            plantfoodBtn.setChecked(state);
        }

        private static class BankVisual extends Table {

            private final TextureRegion bankIcon;

            BankVisual(Main game) {
                bankIcon = game.textureBank.region("IMAGE_UI_HUD_INGAME_PLANTFOOD_BANK");

                if (bankIcon != null) {
                    setSize(bankIcon.getRegionWidth(), bankIcon.getRegionHeight());
                } else {
                    setSize(220f, 90f);
                }
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                float x = getX(), y = getY(), w = getWidth(), h = getHeight();

                if (bankIcon != null) batch.draw(bankIcon, x, y, w, h);

            }
        }
    }

    public static class SelectedPlantsList extends PlantsTable{
        private final PlantType[] slots = new PlantType[8];
        private final ArrayList<PlantCardView> plantCardViewList = new ArrayList<PlantCardView>();
        private final Main game;
        private PlantType imitatorCardType;
        private boolean isAcitve = false;

        public SelectedPlantsList(int column, int pad, boolean upgradeBar,
                                  int cardWidth, int cardHeight, Consumer<PlantCardView> cardClickMethod, Main game) {
            super(column, pad, upgradeBar, cardWidth, cardHeight, cardClickMethod);
            this.game = game;
            build();
        }

        @Override
        public void build(){
            this.clear();
            plantCardViewList.clear();
            User user = App.getCurrentUser();
            if (user == null) return;
            int i = 1;
            for (PlantType plantType : slots){
                Table cardTable = new Table();
                if (plantType == null){
                    Stack stack = new Stack();
                    stack.add(new Image(game.textureBank.region("IMAGE_UI_PACKETS_EMPTY_PACKET")));
                    cardTable.add(stack).size(cardWidth, cardHeight);
                }
                else{
                    int cardLevel = user.getUnlockedPlantsLevels().getOrDefault(plantType, 0);
                    PlantCard card;
                    if (plantType == PlantType.IMITATER ){
                        if (imitatorCardType == null) return;
                        int targetLevel = user.getUnlockedPlantsLevels().getOrDefault(imitatorCardType, 0);
                        card = PlantCardFactory.createImitatorCard(imitatorCardType, targetLevel,
                            cardLevel);
                        cardLevel = targetLevel;
                    }
                    else{
                        card = PlantCardFactory.createCard(plantType, Math.max(1, cardLevel));
                    }
                    PlantCardView plantCardView = new PlantCardView(false, user.hasBoost(plantType), false
                        , cardLevel, card.getSunCost(), plantType);
                    cardTable.add(plantCardView).size(cardWidth, cardHeight);
                    plantCardView.setClickMethod(cardClickMethod);
                    plantCardViewList.add(plantCardView);
                }
                this.add(cardTable).top();
                i++;
                if (i > column){
                    i = 1;
                    this.row();
                }
            }
        }

        public void unselectPlants(){
            for (PlantCardView plantCardView : plantCardViewList){
                plantCardView.setSelectedState(false);
            }
        }

        public void setImitatorCardType(PlantType imitatorCardType) {
            this.imitatorCardType = imitatorCardType;
        }

        public void activate(java.util.List<PlantCard> cards){
            isAcitve = true;
            for (PlantCardView plantCardView : plantCardViewList){
                for (PlantCard card : cards){
                    if (plantCardView.getType() == card.getType()){
                        plantCardView.setCard(card);
                        break;
                    }
                }
            }
        }

        public void update(){
            if (!isAcitve) return;
            for (PlantCardView plantCardView : plantCardViewList){
                plantCardView.update();
            }
        }


        public boolean hasPlant(PlantType plantType){
            for (PlantType type : slots){
                if (type == plantType){
                    return true;
                }
            }
            return false;
        }

        public void removePlant(PlantType type){
            for (int i = 0; i < 8; i++){
                if (slots[i] == type){
                    slots[i] = null;
                    for (int j = i + 1; j < 8; j++){
                        if (slots[j] != null){
                            slots[j-1] = slots[j];
                            slots[j] = null;
                        }
                    }
                    break;
                }
            }
        }

        public void addPlant(PlantType type){
            for (int i = 0; i < 8; i++){
                if (slots[i] == null){
                    slots[i] = type;
                    break;
                }
            }
        }

        public PlantType[] getSlots() {
            return slots;
        }
    }

    public void setInGameDetailsVisibility(boolean state){
        if (!state){
            sunCounter.setVisible(false);
            waveProgressBar.setVisible(false);
            plantFoodBank.setVisible(false);
            selectedPlantsList.setVisible(false);
            shovelBtn.setVisible(false);
            return;
        }

        moveAndSetVisible(sunCounter, 0, -200);
        moveAndSetVisible(shovelBtn, 0, -200);
        moveAndSetVisible(waveProgressBar, 0, -200);
        moveAndSetVisible(plantFoodBank,0, 200);
    }

    public void setSelectedPlantsVisibility(){
        moveAndSetVisible(selectedPlantsList, 250, 0);
    }

    private void moveAndSetVisible(Actor actor, float xAmount, float yAmount){
        actor.addAction(Actions.sequence(
            Actions.moveBy(-xAmount, -yAmount),
            Actions.visible(true),
            Actions.moveBy(xAmount, yAmount, 1f, Interpolation.bounceIn)
        ));
    }

    public PlantFoodBank getPlantFoodBank() {
        return plantFoodBank;
    }

    public ResourcesTable getResourcesTable() {
        return resourcesTable;
    }

    public ImageButton getShovelBtn() {
        return shovelBtn;
    }

    public SelectedPlantsList getSelectedPlantsList() {
        return selectedPlantsList;
    }
}
