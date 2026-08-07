package com.pvz2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pvz2.models.core.GameInitializer;
import com.pvz2.models.core.UserManager;
import com.pvz2.view.SignupMenuScreen;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;
import pvz.skin.PvzSkin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public TextureBank textureBank;
    public PamPlayer pamPlayer;
    public Skin skin;
    private SpriteBatch batch;



    @Override
    public void create() {
        UserManager.init();
        GameInitializer.loadPlantUpgrades();

        batch = new SpriteBatch();

        FileHandle assetsFolder = Gdx.files.internal("");
        textureBank = new TextureBank("768", assetsFolder);
        pamPlayer = new PamPlayer(textureBank, assetsFolder);
        skin = PvzSkin.get();


        setScreen(new SignupMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (textureBank != null) textureBank.dispose();
    }
}
