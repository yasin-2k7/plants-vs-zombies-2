package com.pvz2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pvz2.Main;

public abstract class MenuScreen implements Screen {
    protected final Main game;

    protected Stage stage;
    protected Skin skin;

    private Stack rootStack;
    protected Stack modalStack;
    protected Stack toastStack;
    protected Stack mainStack;

    protected final Queue<Notif> toastQueue = new Queue<>();
    protected boolean hasNotification = false;
    private static Drawable dimBackground;

    protected static class Notif {
        String title;
        String message;

        public Notif(String title, String message) {
            this.title = title;
            this.message = message;
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
                    currentScreen.dispose();
                }
            })
        ));
    }

    public Table createToastNotification(String title, String message) {
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

    public void addToast(String title, String message){
        toastQueue.addLast(new Notif(title, message));
        if (!hasNotification){
            showNextToast();
        }
    }

    protected void showNextToast() {
        if (toastQueue.isEmpty()) {
            hasNotification = false;
            return;
        }

        hasNotification = true;
        Notif notif = toastQueue.removeFirst();

        final Table toastTable = createToastNotification(notif.title, notif.message);
        final Table wrapper = new Table();
        wrapper.pad(10).right().top();

        toastTable.setColor(toastTable.getColor().r, toastTable.getColor().g, toastTable.getColor().b, 1f);
        wrapper.add(toastTable);
        toastStack.addActor(wrapper);

        toastTable.addAction(Actions.sequence(
            Actions.moveBy(0, 200f),
            Actions.moveBy(0, -200, 0.5f, Interpolation.bounceIn),
            Actions.delay(3.0f),
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
