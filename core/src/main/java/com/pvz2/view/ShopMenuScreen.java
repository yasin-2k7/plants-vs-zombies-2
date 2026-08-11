package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pvz2.Main;
import com.pvz2.controller.ShopMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.shop.DailyOffer;
import com.pvz2.models.shop.ShopItem;
import com.pvz2.models.shop.ShopList;
import pvz.skin.BorderedTable;

import java.util.List;

public class ShopMenuScreen extends MenuScreen {

    private final ShopMenuController controller;
    private final ShopList shopList;

    private Table mainTable;
    private Table itemsGrid;
    private ResourcesTable resourcesTable;

    public ShopMenuScreen(Main game) {
        super(game);
        this.controller = new ShopMenuController();
        this.shopList = new ShopList();
    }

    @Override
    protected void buildUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        TextureRegion bgRegion = game.textureBank.region("IMAGE_MAINMENU_BACKGROUND");
        if (bgRegion != null) {
            mainTable.setBackground(new TextureRegionDrawable(bgRegion));
        }

        Table topBar = new Table();

        ImageButton backBtn = MainMenuScreen.createImageButton(
            "IMAGE_UI_MAINMENU_BACK_BTN_NORMAL",
            "IMAGE_UI_MAINMENU_BACK_BTN_PRESSED",
            game.textureBank
        );

        if (backBtn != null) {
            backBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    fadeAndSwitchScreen(new MainMenuScreen(game));
                }
            });
            topBar.add(backBtn).size(60, 60).left().pad(10);
        }

        Label titleLabel = new Label("STORE", skin, "big_outline");
        titleLabel.setFontScale(1.1f);
        topBar.add(titleLabel).expandX().center();

        if (App.getCurrentUser() != null) {
            resourcesTable = new ResourcesTable(App.getCurrentUser(), game);
            topBar.add(resourcesTable).right().pad(10);
        }

        mainTable.add(topBar).growX().padTop(10).padBottom(10).row();

        itemsGrid = new Table();
        itemsGrid.top();

        refreshShopItems();

        ScrollPane scrollPane = new ScrollPane(itemsGrid, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFlickScroll(true);
        scrollPane.setOverscroll(false, false);

        stage.setScrollFocus(scrollPane);

        mainTable.add(scrollPane).grow().pad(10).row();

        mainStack.add(mainTable);
    }


    public void refreshShopItems() {
        itemsGrid.clear();
        int cols = 0;
        int maxCols = 3;

        DailyOffer dailyOffer = shopList.getDailyOffer();
        if (dailyOffer != null && dailyOffer.isAvailableToday()) {
            Table dailyCard = createDailyOfferCard(dailyOffer);
            itemsGrid.add(dailyCard).width(210).height(270).pad(10);
            cols++;
            if (cols % maxCols == 0) itemsGrid.row();
        }

        List<ShopItem> permanentItems = shopList.getPermanentItems();
        for (ShopItem item : permanentItems) {
            Table itemCard = createPermanentItemCard(item);
            itemsGrid.add(itemCard).width(210).height(270).pad(10);
            cols++;
            if (cols % maxCols == 0) itemsGrid.row();
        }
    }

    private Table createDailyOfferCard(DailyOffer offer) {
        BorderedTable card = new BorderedTable();
        card.pad(10);
        card.top();

        Label badge = new Label("DAILY OFFER", skin, "big_outline");
        badge.setFontScale(0.48f);
        badge.setColor(Color.YELLOW);

        Label titleLabel = new Label(offer.getName(), skin, "big_outline");
        titleLabel.setFontScale(0.5f);
        titleLabel.setWrap(true);
        titleLabel.setAlignment(com.badlogic.gdx.utils.Align.center);

        Image itemImg = getItemImage(offer.getPlantType().name());

        TextButton buyBtn = new TextButton(offer.getCoinCost() + " Coins", skin, "purple");
        buyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPurchaseConfirmation(offer.getName(), offer.getCoinCost() + " Coins", () -> {
                    String result = controller.buyItem(offer.getId(), 1, offer.getPlantType().name());
                    handlePurchaseResult(result);
                });
            }
        });

        card.add(badge).center().padTop(5).row();
        card.add(titleLabel).width(170).height(40).center().padBottom(5).row();
        card.add(itemImg).size(80, 80).center().padBottom(10).row();
        card.add().expandY().row();
        card.add(buyBtn).width(150).height(42).padBottom(15).bottom();

        return card;
    }

    private Table createPermanentItemCard(ShopItem item) {
        BorderedTable card = new BorderedTable();
        card.pad(10);
        card.top();

        Label titleLabel = new Label(item.getName(), skin, "big_outline");
        titleLabel.setFontScale(0.5f);
        titleLabel.setWrap(true);
        titleLabel.setAlignment(com.badlogic.gdx.utils.Align.center);

        Image itemImg = getItemImage(item.getName());

        String priceText = item.getCoinCost() > 0 ? item.getCoinCost() + " Coins" : item.getDiamondCost() + " Gems";
        TextButton buyBtn = new TextButton(priceText, skin, "green");

        buyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (item.getName().equalsIgnoreCase("Specific Seed Packet")) {
                    showPlantSelectionDialog(item, priceText);
                } else {
                    showPurchaseConfirmation(item.getName(), priceText, () -> {
                        String result = controller.buyItem(item.getId(), 1, null);
                        handlePurchaseResult(result);
                    });
                }
            }
        });

        card.add(titleLabel).width(170).height(45).center().padBottom(5).row();
        card.add(itemImg).size(80, 80).center().padBottom(10).row();
        card.add().expandY().row();
        card.add(buyBtn).width(150).height(42).padBottom(15).bottom();

        return card;
    }

    private void showPurchaseConfirmation(String itemName, String priceText, Runnable onConfirm) {
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(createSolidColor(new Color(0, 0, 0, 0.65f)));
        overlay.setTouchable(Touchable.enabled);

        BorderedTable popup = new BorderedTable();
        popup.pad(20);

        Label title = new Label("Purchase Confirmation", skin, "big_outline");
        title.setFontScale(0.85f);

        Label message = new Label("Would you like to purchase\n" + itemName + " for " + priceText + "?", skin);
        message.setWrap(true);
        message.setAlignment(com.badlogic.gdx.utils.Align.center);
        message.setColor(Color.BLACK);

        TextButton yesBtn = new TextButton("Yes", skin, "green");
        TextButton noBtn = new TextButton("Cancel", skin, "purple");

        yesBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                onConfirm.run();
            }
        });

        noBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
            }
        });

        popup.add(title).padBottom(15).row();
        popup.add(message).width(320).padBottom(20).row();

        Table buttons = new Table();
        buttons.add(noBtn).width(120).padRight(15);
        buttons.add(yesBtn).width(120);
        popup.add(buttons);

        overlay.add(popup).width(400);
        stage.addActor(overlay);
    }

    private void showPlantSelectionDialog(ShopItem item, String priceText) {
        User user = App.getCurrentUser();
        if (user == null || user.getUnlockedPlantsLevels().isEmpty()) {
            showResultDialog("Error", "You have no unlocked plants to buy seeds for.");
            return;
        }

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(createSolidColor(new Color(0, 0, 0, 0.75f)));
        overlay.setTouchable(Touchable.enabled);

        BorderedTable popup = new BorderedTable();
        popup.pad(15);

        Label title = new Label("Select Plant for Seed", skin, "big_outline");
        title.setFontScale(0.75f);

        Table plantGrid = new Table();
        plantGrid.top();
        int col = 0;

        for (PlantType plant : user.getUnlockedPlantsLevels().keySet()) {
            BorderedTable plantCard = new BorderedTable();
            plantCard.pad(5);

            Image plantImg = getItemImage(plant.name());
            Label plantName = new Label(plant.name(), skin);
            plantName.setFontScale(0.4f);
            plantName.setWrap(true);
            plantName.setAlignment(com.badlogic.gdx.utils.Align.center);

            plantCard.add(plantImg).size(55, 55).center().row();
            plantCard.add(plantName).width(80).padTop(2).center();

            plantCard.setTouchable(Touchable.enabled);
            plantCard.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    overlay.remove();
                    showPurchaseConfirmation(item.getName() + " (" + plant.name() + ")", priceText, () -> {
                        String result = controller.buyItem(item.getId(), 1, plant.name());
                        handlePurchaseResult(result);
                    });
                }
            });

            plantGrid.add(plantCard).width(95).height(100).pad(5);
            col++;
            if (col % 3 == 0) plantGrid.row();
        }

        ScrollPane scroll = new ScrollPane(plantGrid, skin);
        scroll.setFadeScrollBars(false);
        stage.setScrollFocus(scroll);

        TextButton cancelBtn = new TextButton("Cancel", skin, "purple");
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
            }
        });

        popup.add(title).padBottom(10).row();
        popup.add(scroll).width(340).height(220).padBottom(10).row();
        popup.add(cancelBtn).width(120).height(38);

        overlay.add(popup);
        stage.addActor(overlay);
    }

    private void handlePurchaseResult(String result) {
        boolean isError = result.toLowerCase().startsWith("error");
        showResultDialog(isError ? "Error" : "Success", result);
        if (!isError) {
            refreshShopItems();
            if (resourcesTable != null) {
                resourcesTable.update();
            }
        }
    }

    private void showResultDialog(String titleStr, String messageStr) {
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(createSolidColor(new Color(0, 0, 0, 0.65f)));
        overlay.setTouchable(Touchable.enabled);

        BorderedTable popup = new BorderedTable();
        popup.pad(20);

        Label title = new Label(titleStr, skin, "big_outline");
        Label msg = new Label(messageStr, skin);
        msg.setWrap(true);
        msg.setAlignment(com.badlogic.gdx.utils.Align.center);

        TextButton okBtn = new TextButton("OK", skin);
        okBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
            }
        });

        popup.add(title).padBottom(10).row();
        popup.add(msg).width(300).padBottom(15).row();
        popup.add(okBtn).width(100);

        overlay.add(popup);
        stage.addActor(overlay);
    }

    private Image getItemImage(String name) {
        TextureRegion reg = null;
        String cleanName = name.toLowerCase();

        if (cleanName.contains("food")) {
            reg = game.textureBank.region("IMAGE_UI_DANGERROOM_PLANTFOOD_ICON");
        } else if (cleanName.contains("seed") || cleanName.contains("packet")) {
            reg = game.textureBank.region("IMAGE_UI_PACKETS_READY_PREMIUM");
        } else if (cleanName.contains("pot") || cleanName.contains("sprout")) {
            reg = game.textureBank.region("IMAGE_UI_PACKETS_THYMEWARP");
        } else if (cleanName.contains("random") || cleanName.contains("mystery")) {
            reg = game.textureBank.region("IMAGE_UI_STOREMULTI_SEEDPACKETICON");
        } else {
            reg = game.textureBank.region("IMAGE_UI_PACKETS_" + name.toUpperCase());
        }

        if (reg == null) {
            reg = game.textureBank.region("IMAGE_UI_CLAIM_SMALL");
        }

        return (reg != null) ? new Image(reg) : new Image();
    }

    private TextureRegionDrawable createSolidColor(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}
