package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pvz2.Main;
import com.ray3k.tenpatch.TenPatchDrawable;
import pvz.libpvz.pam.ClipRef;
import pvz.skin.BorderedTable;

public class SignupMenuScreen extends MenuScreen{
    private ClipRef backgroundClip;
    TextureRegion textureRegion;

    public SignupMenuScreen(Main game) {
        super(game);
        textureRegion = game.textureBank.region("IMAGE_MAINMENU_BACKGROUND");
//        // بارگذاری انیمیشن پس‌زمینه منو با libPVZ
//        game.pamPlayer.loadSync("IMAGE_MAINMENU_BACKGROUND");
//        backgroundClip = game.pamPlayer.getClip("MENUS/MAIN_MENU/MAIN_MENU.PAM", "idle");
    }

    @Override
    protected void buildUI() {
        Image backgroundImage = new Image(textureRegion);

        mainStack.add(backgroundImage);
        TextButton button = new TextButton("HELLO", skin);
        BorderedTable myTable = new BorderedTable();


        myTable.add(button);
        Table wrapper = new Table();
        wrapper.add(myTable);
        mainStack.add(wrapper);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
