package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.pvz2.Main;
import com.pvz2.controller.GreenhouseMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.greenhouse.GreenHouse;
import com.pvz2.models.greenhouse.Pot;
import pvz.libpvz.pam.PamPlayer;

public class GreenhouseMenuScreen extends MenuScreen {

    private final GreenhouseMenuController controller = new GreenhouseMenuController();
    private Table gridTable;
    private Label statusLabel;

    private static final String TEX_BG = "IMAGE_BACKGROUNDS_ZEN_GARDEN";
    private static final String TEX_POT_EMPTY = "IMAGE_ZEN_GARDEN_GROWING_PLANT_SLOT_GROWING_PLANT_SLOT_184X161";
    private static final String TEX_POT_LOCKED = "IMAGE_ZEN_GARDEN_LOCKED_POT_ICON";
    private static final String TEX_PLANT_PREFIX = "768/INITIAL/PLANT/";

    private static final Color READY_COLOR = Color.WHITE;
    private static final Color GROWING_TINT = new Color(0.6f, 0.6f, 0.6f, 1f);

    public GreenhouseMenuScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        stage.setViewport(new FillViewport(1800, 1000));

        Image backgroundImage = new Image(game.textureBank.region(TEX_BG));
        backgroundImage.setFillParent(true);
        mainStack.add(backgroundImage);

        Table topTable = new Table();
        topTable.top().left().setFillParent(true);

        Button backBtn = createBackButton();
        topTable.add(backBtn).size(60, 60).pad(15);
        mainStack.add(topTable);

        gridTable = new Table();
        gridTable.setFillParent(true);
        gridTable.top().padTop(280);
        mainStack.add(gridTable);

        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.YELLOW);
        statusLabel.setAlignment(Align.center);

        Table statusTable = new Table();
        statusTable.bottom().setFillParent(true);
        statusTable.add(statusLabel).padBottom(25);
        toastStack.add(statusTable);

        refreshGrid();
    }

    private void refreshGrid() {
        gridTable.clearChildren();

        GreenHouse greenHouse = (App.getCurrentUser() != null) ? App.getCurrentUser().getGreenhouse() : null;
        if (greenHouse == null) {
            gridTable.add(new Label("No user logged in.", skin));
            return;
        }

        for (int r = 1; r <= 3; r++) {
            for (int c = 1; c <= 4; c++) {
                Pot pot = greenHouse.getPot(c, r);
                Table potWidget = createPotWidget(pot, c, r);

                gridTable.add(potWidget)
                    .width(100)
                    .height(100)
                    .pad(50f, 40f, 50f, 40f);
            }
            gridTable.row();
        }
    }

    private Table createPotWidget(final Pot pot, final int x, final int y) {
        Table cell = new Table();
        if (pot == null) return cell;

        final Stack potStack = new Stack();

        if (pot.isLocked()) {
            // ۱. تصویر قفل و دکمه خرید روی گلدان قفل شده
            Image lockImage = new Image(game.textureBank.region(TEX_POT_LOCKED));
            potStack.add(lockImage);

            Table buyOverlay = new Table();
            buyOverlay.setFillParent(true);

            TextButton buyBtn = new TextButton("Unlock", skin);
            buyBtn.getLabel().setFontScale(0.45f);
            buyBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float cx, float cy) {
                    //String resultMessage = controller.unlockPot(x, y);
                    //showToast(resultMessage);
                    refreshGrid();
                }
            });

            buyOverlay.bottom().add(buyBtn).width(70).height(24).padBottom(-5);
            potStack.add(buyOverlay);

        } else if (pot.isEmpty()) {
            Image emptyImage = new Image(game.textureBank.region(TEX_POT_EMPTY));
            potStack.add(emptyImage);

            potStack.setTouchable(Touchable.enabled);
            potStack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float cx, float cy) {
                    showSeedChooserDialog(x, y);
                }
            });

        } else {
            Image potBg = new Image(game.textureBank.region(TEX_POT_EMPTY));
            potStack.add(potBg);

            String pamPath = PlantsCollectionMenuScreen.getPlantAnimAddress(pot.getPlantType());
            String clip = PlantsCollectionMenuScreen.getPlantInitialClip(pot.getPlantType());

            PamActor plantPamActor = new PamActor(game.pamPlayer, pamPath, clip, 0.6f, null);

            if (pot.isReady()) {
                plantPamActor.setColor(READY_COLOR);
            } else {
                plantPamActor.setColor(GROWING_TINT);
            }

            Table plantContainer = new Table();
            plantContainer.setFillParent(true);

            plantContainer.add(plantPamActor).center().padBottom(40f);
            potStack.add(plantContainer);

            if (!pot.isReady()) {
                Table overlayTable = new Table();
                overlayTable.setFillParent(true);

                Table timerTable = new Table();
                TextureRegion timerRegion = game.textureBank.region("finish_timer_background");
                if (timerRegion != null) timerTable.background(new TextureRegionDrawable(timerRegion));

                long remainingHours = pot.getRemainingHours();
                Label timeLabel = new Label(remainingHours + "h", skin);
                timeLabel.setFontScale(0.55f);
                timeLabel.setColor(Color.WHITE);
                timerTable.add(timeLabel).pad(2, 6, 2, 6);

                Table growButtonTable = new Table();
                TextureRegion btnRegion = game.textureBank.region("IMAGE_ZEN_GARDEN_BUTTON_UNLOCK_ACTIVE");
                if (btnRegion != null) growButtonTable.background(new TextureRegionDrawable(btnRegion));

                int gemCost = (int) Math.ceil(remainingHours);
                Label gemLabel = new Label(String.valueOf(gemCost), skin);
                gemLabel.setFontScale(0.55f);
                gemLabel.setColor(Color.WHITE);

                TextureRegion gemRegion = game.textureBank.region("GEM_LARGE");
                Image gemImage = (gemRegion != null) ? new Image(gemRegion) : new Image(game.textureBank.region(TEX_POT_EMPTY));

                growButtonTable.add(gemLabel).padRight(2);
                growButtonTable.add(gemImage).size(14, 14);

                overlayTable.top().add(timerTable).padTop(-5).row();
                overlayTable.bottom().add(growButtonTable).padBottom(-2);

                potStack.add(overlayTable);
            }

            potStack.setTouchable(Touchable.enabled);
            potStack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float cx, float cy) {
                    String resultMessage;
                    if (pot.isReady()) {
                        resultMessage = controller.collect(x, y);
                    } else {
                        resultMessage = controller.grow(x, y);
                    }
                    showToast(resultMessage);
                    refreshGrid();
                }
            });
        }

        cell.add(potStack).size(95, 95);
        return cell;
    }

    private void showSeedChooserDialog(final int x, final int y) {
        final Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setTouchable(Touchable.enabled);

        Table dialogBox = new Table();
        try {
            TextureRegion bgRegion = game.textureBank.region("IMAGE_UI_MAINMENU_DIALOG_BG");
            if (bgRegion != null) dialogBox.background(new TextureRegionDrawable(bgRegion));
        } catch (Exception ignored) {}

        dialogBox.pad(20);

        Label titleLabel = new Label("Choose a sprout to plant:", skin);
        dialogBox.add(titleLabel).colspan(3).padBottom(15).row();

        String[] seeds = {"PEASHOOTER", "SUNFLOWER", "WALLNUT"};

        for (final String seedName : seeds) {
            Table seedCard = new Table();

            String plantTex = "IMAGE_UI_PACKETS_" + seedName;
            Image seedImg;

            try {
                TextureRegion reg = game.textureBank.region(plantTex);
                seedImg = (reg != null) ? new Image(reg) : new Image(game.textureBank.region(TEX_POT_EMPTY));
            } catch (Exception e) {
                seedImg = new Image(game.textureBank.region(TEX_POT_EMPTY));
            }
            seedCard.add(seedImg).size(60, 60).row();

            TextButton plantBtn = new TextButton("Plant", skin);
            plantBtn.getLabel().setFontScale(0.5f);
            plantBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float cx, float cy) {
                    String resultMessage = controller.plantPot(x, y);
                    showToast(resultMessage);
                    refreshGrid();
                    overlay.remove();
                }
            });

            seedCard.add(plantBtn).width(60).height(25).padTop(5);
            dialogBox.add(seedCard).pad(10);
        }

        dialogBox.row();
        TextButton closeBtn = new TextButton("Cancel", skin);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                overlay.remove(); // بستن دیالوگ
            }
        });

        dialogBox.add(closeBtn).colspan(3).padTop(15);
        overlay.add(dialogBox);

        stage.addActor(overlay);
    }

    private Button createBackButton() {
        TextureRegion backNorm = game.textureBank.region("IMAGE_UI_MAINMENU_BACK_BTN_NORMAL");
        TextureRegion backPress = game.textureBank.region("IMAGE_UI_MAINMENU_BACK_BTN_PRESSED");

        if (backNorm != null) {
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.up = new TextureRegionDrawable(backNorm);
            if (backPress != null) style.down = new TextureRegionDrawable(backPress);

            ImageButton btn = new ImageButton(style);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    fadeAndSwitchScreen(new MainMenuScreen(game));
                }
            });
            return btn;
        }

        TextButton textBtn = new TextButton("Back", skin, "purple");
        textBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeAndSwitchScreen(new MainMenuScreen(game));
            }
        });
        return textBtn;
    }

    private void showToast(String message) {
        statusLabel.setText(message);
        statusLabel.clearActions();
        statusLabel.getColor().a = 1f;
        statusLabel.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(
            com.badlogic.gdx.scenes.scene2d.actions.Actions.delay(3f),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut(1f)
        ));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }
}
