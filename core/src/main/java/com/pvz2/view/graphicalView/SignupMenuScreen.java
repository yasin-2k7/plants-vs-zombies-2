package com.pvz2.view.graphicalView;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.Main;
import pvz.libpvz.pam.ClipRef;

public class SignupMenuScreen extends MenuScreen{
    private SpriteBatch batch;
    private ClipRef backgroundClip;

    public SignupMenuScreen(Main game) {
        super(game);
        batch = new SpriteBatch();

        // بارگذاری انیمیشن پس‌زمینه منو با libPVZ
        game.pamPlayer.loadSync("MENUS/MAIN_MENU/MAIN_MENU.PAM");
        backgroundClip = game.pamPlayer.getClip("MENUS/MAIN_MENU/MAIN_MENU.PAM", "idle");
    }

    @Override
    protected void buildUI() {

    }
}
