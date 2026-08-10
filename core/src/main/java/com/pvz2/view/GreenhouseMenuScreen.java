package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.pvz2.Main;
import com.pvz2.controller.GreenhouseMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.greenhouse.GreenHouse;
import com.pvz2.models.greenhouse.Pot;

public class GreenhouseMenuScreen extends MenuScreen {

    private final GreenhouseMenuController controller = new GreenhouseMenuController();
    private Table gridTable;
    private Label statusLabel;

    private static final String TEX_BG = "IMAGE_BACKGROUNDS_ZEN_GARDEN";
    private static final String TEX_POT_EMPTY = "IMAGE_ZEN_GARDEN_GROWING_PLANT_SLOT_GROWING_PLANT_SLOT_184X161";
    private static final String TEX_POT_LOCKED = "IMAGE_ZEN_GARDEN_LOCKED_POT_ICON";
    private static final String TEX_PLANT_PREFIX = "IMAGE_ZEN_GARDEN_PLANT_";

    private static final Color READY_COLOR = Color.WHITE;
    private static final Color GROWING_TINT = new Color(0.55f, 0.55f, 0.55f, 1f);

    public GreenhouseMenuScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        Image backgroundImage = new Image(game.textureBank.region(TEX_BG));
        backgroundImage.setFillParent(true);
        mainStack.add(backgroundImage);

        gridTable = new Table();
        gridTable.setFillParent(true);
        gridTable.center().padTop(145).padLeft(15);
        mainStack.add(gridTable);

        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.YELLOW);
        statusLabel.setAlignment(Align.center);

        Table statusTable = new Table();
        statusTable.bottom().setFillParent(true);
        statusTable.add(statusLabel).padBottom(30);
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
                    .height(110)
                    .padTop(50f)
                    .padBottom(50f)
                    .padLeft(40f)
                    .padRight(40f);
            }
            gridTable.row();
        }
    }

    private Table createPotWidget(final Pot pot, final int x, final int y) {
        Table cell = new Table();
        if (pot == null) return cell;

        final Stack potStack = new Stack();

        if (pot.isLocked()) {
            Image lockImage = new Image(game.textureBank.region(TEX_POT_LOCKED));
            potStack.add(lockImage);
            cell.add(potStack).size(52, 52).row();

        } else if (pot.isEmpty()) {
            Image emptyImage = new Image(game.textureBank.region(TEX_POT_EMPTY));
            potStack.add(emptyImage);
            cell.add(potStack).size(80, 80).row();

        } else {
            String plantTexName = TEX_PLANT_PREFIX + pot.getPlantType().name();
            Image plantImage;
            try {
                plantImage = new Image(game.textureBank.region(plantTexName));
            } catch (Exception e) {
                plantImage = new Image(game.textureBank.region(TEX_POT_EMPTY));
            }

            if (pot.isReady()) {
                plantImage.setColor(READY_COLOR);
                potStack.add(plantImage);
                cell.add(potStack).size(80, 80).row();
            } else {
                plantImage.setColor(GROWING_TINT);
                potStack.add(plantImage);

                Table timerTable = new Table();
                try {
                    Image timerBg = new Image(game.textureBank.region("finish_timer_background"));
                    timerTable.background(timerBg.getDrawable());
                } catch (Exception ignored) {}

                long remainingHours = pot.getRemainingHours();
                Label timeLabel = new Label(remainingHours + "h", skin);
                timeLabel.setFontScale(0.7f);
                timeLabel.setColor(Color.WHITE);
                timerTable.add(timeLabel).pad(2);

                Table growButtonTable = new Table();
                try {
                    Image growBtnBg = new Image(game.textureBank.region("IMAGE_ZEN_GARDEN_BUTTON_UNLOCK_ACTIVE"));
                    growButtonTable.background(growBtnBg.getDrawable());
                } catch (Exception ignored) {}

                int gemCost = (int) Math.ceil(remainingHours);
                Label gemLabel = new Label(String.valueOf(gemCost), skin);
                gemLabel.setFontScale(0.7f);
                gemLabel.setColor(Color.WHITE);

                Image gemImage;
                try {
                    gemImage = new Image(game.textureBank.region("GEM_LARGE"));
                } catch (Exception e) {
                    gemImage = new Image(game.textureBank.region(TEX_POT_EMPTY));                }

                growButtonTable.add(gemLabel).padRight(4);
                growButtonTable.add(gemImage).size(16, 16);

                Table overlayTable = new Table();
                overlayTable.top().padTop(5);
                overlayTable.add(timerTable).row();

                Table bottomOverlay = new Table();
                bottomOverlay.bottom().padBottom(5);
                bottomOverlay.add(growButtonTable);

                potStack.add(overlayTable);
                potStack.add(bottomOverlay);

                cell.add(potStack).size(80, 90).row();
            }
        }

        potStack.setTouchable(Touchable.enabled);
        potStack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float cx, float cy) {
                String resultMessage;
                if (pot.isLocked()) {
                    resultMessage = "This pot is locked. Unlock it first via shop.";
                } else if (pot.isEmpty()) {
                    resultMessage = controller.plantPot(x, y);
                } else if (pot.isReady()) {
                    resultMessage = controller.collect(x, y);
                } else {
                    resultMessage = controller.grow(x, y);
                }

                showToast(resultMessage);
                refreshGrid();
            }
        });

        return cell;
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
}
