package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pvz2.Main;
import com.pvz2.controller.MainMenuController;

public class MainMenuScreen extends MenuScreen {
    private MainMenuController controller;

    private TextureRegion bg;
    private Image logoImg;
    private Image unreadBadge;

    private TextButton playBtn;
    private ImageButton newsBtn;
    private ImageButton settingsBtn;
    private Button leaderboardBtn;
    private Button muPoint;
    private Button profileBtn;
    private ImageButton backBtn;

    private Table mainTable;
    private Table topBar;
    private Table centerTable;
    private Table bottomBar;
    private Table badgeOverlay;
    private Stack newsStack;

    private boolean hasUnreadNews = true;

    public MainMenuScreen(Main game) {
        super(game);
        this.controller = new MainMenuController(this);
    }

    private void initFields() {
        if (mainTable != null) return;

        bg = game.textureBank.region("IMAGE_MAINMENU_BACKGROUND");

        TextureRegion logo = game.textureBank.region("IMAGE_UI_MAINMENU_PVZ2_LOGO_HORIZONTAL");
        logoImg = (logo != null) ? new Image(logo) : null;

        playBtn = new TextButton("PLAY", game.skin, "purple");

        newsBtn = createImageButton(
            "IMAGE_UI_HUD_NEWSBUTTON_BUTTONS_HUD_NEWS_NORMAL",
            "IMAGE_UI_HUD_NEWSBUTTON_BUTTONS_HUD_NEWS_SELECTED"
        );

        settingsBtn = createImageButton(
            "IMAGE_UI_HUD_SETTINGSBUTTON_BUTTONS_HUD_SETTINGS_NORMAL",
            "IMAGE_UI_HUD_SETTINGSBUTTON_BUTTONS_HUD_SETTINGS_SELECTED"
        );

        leaderboardBtn = new TextButton("", skin, "brown");
        Image cup = new Image(game.textureBank.region("IMAGE_UI_GAMECENTER_ICON"));
        leaderboardBtn.add(cup);

        muPoint = new TextButton("", skin, "brown");
        Image star = new Image(game.textureBank.region("IMAGE_UI_GENERIC_STAR_ICON"));
        muPoint.add(star);

        profileBtn = new TextButton("", skin, "brown");
        Image prof = new Image(game.textureBank.region("IMAGE_UI_MAINMENU_MM_PLAYERICON"));
        profileBtn.add(prof).padRight(5);

        backBtn = createImageButton(
            "IMAGE_UI_MAINMENU_BACK_BTN_NORMAL",
            "IMAGE_UI_MAINMENU_BACK_BTN_PRESSED"
        );

        unreadBadge = new Image(game.textureBank.region("IMAGE_UI_CLAIM_SMALL"));

        mainTable = new Table();
        topBar = new Table();
        centerTable = new Table();
        bottomBar = new Table();
        badgeOverlay = new Table();
        newsStack = new Stack();
    }

    @Override
    protected void buildUI() {
        initFields();

        mainTable.clear();
        mainTable.setFillParent(true);

        if (backBtn != null) {
            topBar.add(backBtn).left().top().pad(10);
        }
        topBar.add().expandX();
        mainTable.add(topBar).top().growX().row();

        if (logoImg != null) {
            mainTable.add(logoImg).prefWidth(400).prefHeight(100).padTop(20).row();
        }
        centerTable.add(playBtn).width(200).height(60).pad(20).row();
        mainTable.add(centerTable).expandY().center().row();

        if (newsBtn != null) {
            newsStack.add(newsBtn);

            badgeOverlay.top().right();
            badgeOverlay.add(unreadBadge).size(25, 25).padTop(-8).padRight(-5);

            newsStack.add(badgeOverlay);
            unreadBadge.setVisible(hasUnreadNews);
        }

        float btnSize = 70f;

        if (settingsBtn != null) bottomBar.add(settingsBtn).size(btnSize).padLeft(25).pad(10);
        if (newsBtn != null) bottomBar.add(newsStack).size(btnSize).pad(5);

        bottomBar.add().expandX();

        if (muPoint != null) bottomBar.add(muPoint).size(btnSize).pad(10);

        bottomBar.add().expandX();

        if (leaderboardBtn != null) bottomBar.add(leaderboardBtn).size(btnSize).pad(10);
        if (profileBtn != null) bottomBar.add(profileBtn).size(btnSize).padRight(25).pad(10);

        mainTable.add(bottomBar).bottom().growX().pad(10);

        if (bg != null) {
            mainTable.setBackground(new TextureRegionDrawable(bg));
        }
        mainStack.add(mainTable);
        setListeners();
    }

    private void setListeners(){
        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.exitMenu();
            }
        });
        playBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.enterMenu("play");
            }
        });
        settingsBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.enterMenu("settings");
            }
        });
        newsBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.enterMenu("news");
            }
        });
        muPoint.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.enterMenu("mu point");
            }
        });
        leaderboardBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.enterMenu("leaderboard");
            }
        });
        profileBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.enterMenu("profile");
            }
        });
    }

    private ImageButton createImageButton(String normalRegionKey, String selectedRegionKey) {
        TextureRegion normalReg = game.textureBank.region(normalRegionKey);
        TextureRegion selectedReg = game.textureBank.region(selectedRegionKey);

        if (normalReg == null) return null;

        TextureRegionDrawable upDrawable = new TextureRegionDrawable(normalReg);
        TextureRegionDrawable downDrawable = (selectedReg != null)
            ? new TextureRegionDrawable(selectedReg)
            : upDrawable;

        return new ImageButton(upDrawable, downDrawable);
    }

    public void setUnreadStatus(boolean hasUnread) {
        this.hasUnreadNews = hasUnread;
        if (unreadBadge != null) {
            unreadBadge.setVisible(hasUnread);
        }
    }
}
