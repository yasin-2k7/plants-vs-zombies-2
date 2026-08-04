package com.pvz2.view.graphicalView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pvz2.Main;

public abstract class MenuScreen implements Screen {
    protected final Main game; // ارجاع به بازی اصلی شامل libPVZ

    protected Stage stage;
    protected Skin skin;

    protected Stack rootStack;
    protected Stack modalStack;
    protected Stack toastStack;
    private Stack mainStack;

    protected float stateTime = 0f; // زمان انیمیشن‌ها

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport);
        // skin = GameAssetManager.skin; // اسکین UI خود را ست کنید

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

    /**
     * تغییر صفحه با افکت FadeOut
     */
    public void fadeAndSwitchScreen(final Screen targetScreen) {
        Gdx.input.setInputProcessor(null);
        final Screen currentScreen = this;
        stage.getRoot().addAction(Actions.sequence(
            Actions.fadeOut(0.4f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(targetScreen); // تغییر صفحه از طریق کلاس اصلی
                    currentScreen.dispose();
                }
            })
        ));
    }
}
