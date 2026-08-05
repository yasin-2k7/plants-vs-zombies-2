package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pvz2.Main;
import pvz.libpvz.pam.ClipRef;

public class SignupMenuScreen extends MenuScreen{
    private SpriteBatch batch;
    private ClipRef backgroundClip;
    TextureRegion textureRegion;

    public SignupMenuScreen(Main game) {
        super(game);
        batch = new SpriteBatch();
        textureRegion = game.atlasManager.get("IMAGE_UI_MAINMENU_MAINMENU_CONTENT_OFFLINE");
//        // بارگذاری انیمیشن پس‌زمینه منو با libPVZ
//        game.pamPlayer.loadSync("IMAGE_MAINMENU_BACKGROUND");
//        backgroundClip = game.pamPlayer.getClip("MENUS/MAIN_MENU/MAIN_MENU.PAM", "idle");
    }

    @Override
    protected void buildUI() {
        Image backgroundImage = new Image(textureRegion);

        mainStack.add(backgroundImage);
        TextButton button = new TextButton("HELLO", skin);
        Table myTable = new Table();
        myTable.add(button);
        mainStack.add(myTable);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
