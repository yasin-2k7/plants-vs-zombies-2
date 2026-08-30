package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pvz2.Main;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.world.GameWorld;
import com.pvz2.view.audios.GameSFX;
import com.pvz2.view.audios.SFXManager;
import java.util.function.Consumer;

public class OnlineGameHud extends Group {
    private static final float MARGIN = 20f;

    private final Table topBar;
    private final GameHUD.SunCounter sunCounter;
    private final ImageButton shovelBtn;
    private final SelectedPlantsList selectedPlantsList;
    private final SelectedZombiesList selectedZombiesList;
    private final OnlineGameScreen screen;

    public OnlineGameHud(Main game, Skin skin, OnlineGameScreen screen) {
        this.screen = screen;

        sunCounter = new GameHUD.SunCounter(game, skin);
        selectedPlantsList = new SelectedPlantsList(1, 1, false,
            150, 100, createSelectingMethod(), game);
        selectedPlantsList.setNoBoost(true);
        selectedZombiesList = new SelectedZombiesList(100, 200, createZombieClickMethod());
        selectedZombiesList.build(screen.controller.getWorld().getZombieCards());
        shovelBtn = createShovelBtn(game);
        topBar = new Table();
        topBar.add(sunCounter).left().pad(MARGIN);
        topBar.add(shovelBtn).pad(5);

        topBar.add(selectedPlantsList).left().pad(MARGIN);
        topBar.add(selectedZombiesList).right().pad(MARGIN);

        addActor(topBar);
    }

    private Consumer<ZombieCardView> createZombieClickMethod() {
        return new Consumer<ZombieCardView>() {
            @Override
            public void accept(ZombieCardView card) {
                boolean isSelected =
                    screen.controller.selectAndUnselectZombie(card.getZombieName(), screen);
                    for (ZombieCardView cardView : selectedZombiesList.getZombieCardViewList()){
                        cardView.setSelectedState(false);
                    }
                    if (isSelected){
                        SFXManager.getInstance().playSound(GameSFX.SEED_LIFT);
                        card.setSelectedState(true);
                        screen.getZombiePlacementManager().selectZombie(card.getZombieName());
                    }

            }
        };
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
                    if (screen.controller.selectAndUnselectShovel()) {
                        screen.getShovelPlacementManager().setSelected(true);
                    }
                } else {
                    screen.controller.selectAndUnselectShovel();
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
                if (selectedPlantsList.isActive()){
                    boolean isSelected =
                        screen.controller.selectAndUnselectPlant(plantCardView.getType(), screen);
                    for (PlantCardView cardView : selectedPlantsList.getPlantCardViewList()){
                        cardView.setSelectedState(false);
                    }
                    if (isSelected){
                        SFXManager.getInstance().playSound(GameSFX.SEED_LIFT);
                        plantCardView.setSelectedState(true);
                        screen.getPlantPlacementManager().selectPlant(plantCardView.getType(), null);
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

    }

    public void update(GameWorld world, float delta) {
        sunCounter.update(world);
        selectedPlantsList.update();
        if (selectedPlantsList.getSlots()[0] == null){
            if (world != null){
                int i = 0;
                for (PlantCard plantCard : world.getPlantLists()){
                    selectedPlantsList.getSlots()[i] = plantCard.getType();
                    i++;
                }
            }
        }
    }

    public SelectedZombiesList getSelectedZombiesList() {
        return selectedZombiesList;
    }

    public ImageButton getShovelBtn() {
        return shovelBtn;
    }

    public SelectedPlantsList getSelectedPlantsList() {
        return selectedPlantsList;
    }
}
