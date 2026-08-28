package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pvz2.Main;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.UserManager;
import com.pvz2.network.NetworkClient;
import com.pvz2.network.onlineIZombie.ClientGameController;
import com.pvz2.network.onlineIZombie.messages.ChallengeAnswerRequest;
import com.pvz2.network.onlineIZombie.messages.ChallengeAnswerResponse;
import com.pvz2.network.onlineIZombie.messages.MatchFound;
import pvz.skin.BorderedTable;

import java.util.function.Consumer;

public abstract class MenuScreen implements Screen {
    protected final Main game;

    protected Stage stage;
    protected Skin skin;

    private Stack rootStack;
    protected Stack modalStack;
    protected Stack toastStack;
    protected Stack mainStack;

    protected OrthographicCamera worldCamera;
    protected Viewport worldViewport;

    protected final Queue<Notif> toastQueue = new Queue<>();
    protected boolean hasNotification = false;
    private static Drawable dimBackground;

    protected static class Notif {
        String title;
        String message;
        boolean urgent;

        public Notif(String title, String message) {
            this(title, message, false);
        }

        public Notif(String title, String message, boolean urgent) {
            this.title = title;
            this.message = message;
            this.urgent = urgent;
        }
    }

    protected void initWorldCamera(float worldWidth, float worldHeight) {
        worldCamera = new OrthographicCamera();
        worldViewport = new FillViewport(worldWidth, worldHeight, worldCamera);
        worldCamera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        worldCamera.update();
    }

    protected void applyWorldViewport() {
        if (worldViewport != null) {
            worldViewport.apply();
            game.batch.setProjectionMatrix(worldCamera.combined);
        }
    }

    protected float stateTime = 0f; // زمان انیمیشن‌ها

    public MenuScreen(Main game) {
        this.game = game;
        this.skin = game.skin;
    }

    @Override
    public void show() {
        ExtendViewport viewport = new ExtendViewport(1800, 1000);
        stage = new Stage(viewport);

        mainStack = new Stack();
        modalStack = new Stack();
        toastStack = new Stack();

        rootStack = new Stack();



        rootStack.setFillParent(true);

        stage.addActor(rootStack);
        rootStack.add(mainStack);
        rootStack.add(toastStack);
        rootStack.add(modalStack);

        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(Actions.fadeIn(0.4f));

        Gdx.input.setInputProcessor(stage);

        // ساخت عناصر ویجت منو در کلاس‌های فرزند
        buildUI();
        NetworkClient.get().onPush("CHALLENGE_ANSWER", msg -> {
            ChallengeAnswerResponse response = NetworkClient.get().parsePayload(msg, ChallengeAnswerResponse.class);
            if (!response.success){
                addToast("Error", response.errorMessage);
            }
        });
    }

    /**
     * هر منو فرعی عناصر UI (دکمه‌ها، جدول‌ها و...) را در این متد می‌سازد.
     */
    protected abstract void buildUI();

    /**
     * رسم پس‌زمینه (انیمیشن PAM یا تصویر ثابت).
     */
    protected void drawBackground(float delta) {}

    @Override
    public void render(float delta) {
        stateTime += delta;

        // ۱. الزامی: آپدیت صف بافت‌های libPVZ در هر فریم
        game.textureBank.update();

        ScreenUtils.clear(0, 0, 0, 1);

        stage.getViewport().apply();
        game.batch.setProjectionMatrix(stage.getCamera().combined);

        // ۲. رسم پس‌زمینه (انیمیشن‌های PAM یا عکس ثابت)
        drawBackground(delta);

        // ۳. به روزرسانی و رسم Scene2D UI
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
    }

    public Stack getModalStack() {
        return modalStack;
    }


    public void fadeAndSwitchScreen(final Screen targetScreen) {
        Gdx.input.setInputProcessor(null);
        final Screen currentScreen = this;
        stage.getRoot().addAction(Actions.sequence(
            Actions.fadeOut(0.4f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(targetScreen);
                    GameMenuController.setScreen((MenuScreen) targetScreen);
                    currentScreen.dispose();
                }
            })
        ));
    }

    public Table createToastNotification(String title, String message, boolean urgent) {
        Table toast = new Table();

        toast.setBackground(getDimBackground());
        toast.pad(15);

        Table textTable = new Table();
        textTable.left();

        Label titleLabel = new Label(title, skin, "big");
        titleLabel.setColor(Color.GOLD);

        Label messageLabel = new Label(message, skin, "medium");

        textTable.add(titleLabel).left().row();
        textTable.add(messageLabel).left().padTop(4);

        toast.add(textTable).expandX().fillX();

        return toast;
    }

    public void addToast(String title, String message) {
        addToast(title, message, false);
    }

    public void addToast(String title, String message, boolean urgent) {
        toastQueue.addLast(new Notif(title, message, urgent));
        if (!hasNotification) {
            showNextToast();
        }
    }

    protected void showChallengePopup(String fromUsername, Consumer<Boolean> response) {
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(MainMenuScreen.createSolidColor(new Color(0, 0, 0, 0.65f)));
        overlay.setTouchable(Touchable.enabled);
        overlay.addListener(new ClickListener());
        BorderedTable popupBox = new BorderedTable();
        popupBox.center().pad(20);
        Table topBar = new Table();
        Label titleLabel = new Label("Challenge request", skin, "big_outline");
        topBar.add(titleLabel).center();
        Label username = new Label("from user " + fromUsername, skin, "medium_outline");
        popupBox.add(topBar).growX().pad(10).row();
        popupBox.add(username).growX().row();
        TextButton acceptBtn = new TextButton("ACCEPT", skin);
        TextButton refuseBtn = new TextButton("REFUSE", skin);
        acceptBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                NetworkClient.get().onPush("MATCH_FOUND", msg -> {
                    MatchFound info = NetworkClient.get().parsePayload(msg, MatchFound.class);
                    ClientGameController controller = new ClientGameController(info);
                    fadeAndSwitchScreen(new OnlineGameScreen(game, controller));
                });
                overlay.remove();
                response.accept(true);
            }
        });
        refuseBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                response.accept(false);
            }
        });
        popupBox.add(refuseBtn);
        popupBox.add(acceptBtn);
        overlay.add(popupBox).width(400).height(300);
        stage.addActor(overlay);
    }

    protected void respondToChallenge(String inviteId, boolean accept) {
        new Thread(() -> {
            try {
                NetworkClient.get().sendRequest("CHALLENGE_ANSWER",
                    new ChallengeAnswerRequest(inviteId, accept), 5000);
            } catch (InterruptedException ignored) {}
        }).start();
    }

    protected void showNextToast() {
        if (toastQueue.isEmpty()) {
            hasNotification = false;
            return;
        }
        hasNotification = true;
        Notif notif = toastQueue.removeFirst();

        //**  toastهای معمولی (Added/Removed/Error و...) فعلاً غیرفعالن
//        if (!notif.urgent) {
//            showNextToast();
//            return;
//        }
        //**

        presentToast(notif);
    }

    protected void presentToast(Notif notif) {
        final Table toastTable = createToastNotification(notif.title, notif.message, notif.urgent);
        final Table wrapper = new Table();
        wrapper.pad(10).right().top();

        toastTable.setColor(toastTable.getColor().r, toastTable.getColor().g, toastTable.getColor().b, 1f);
        wrapper.add(toastTable);
        toastStack.addActor(wrapper);

        toastTable.addAction(Actions.sequence(
            Actions.visible(false),
            Actions.moveBy(0, 200f),
            Actions.visible(true),
            Actions.moveBy(0, -200, 0.2f, Interpolation.bounceIn),
            Actions.delay(0.4f),
            Actions.fadeOut(0.3f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    wrapper.remove();
                    showNextToast();
                }
            })
        ));
    }

    public static Drawable getDimBackground() {
        if (dimBackground == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(0f, 0f, 0f, 0.8f);
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            dimBackground = new TextureRegionDrawable(texture);
        }
        return dimBackground;
    }

    public Main getGame() {
        return game;
    }
}
