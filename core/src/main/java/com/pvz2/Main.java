package com.pvz2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pvz2.models.core.GameInitializer;
import com.pvz2.models.core.UserManager;
import com.pvz2.view.AtlasManager;
import com.pvz2.view.SignupMenuScreen;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;
import pvz.skin.PvzSkin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public TextureBank textureBank;
    public PamPlayer pamPlayer;
    public Skin skin;
    public AtlasManager atlasManager;

    @Override
    public void create() {
        UserManager.init();
        GameInitializer.loadPlantUpgrades();

        FileHandle assetsFolder = Gdx.files.internal("");
        textureBank = new TextureBank("768", assetsFolder);
        pamPlayer = new PamPlayer(textureBank, assetsFolder);
        skin = PvzSkin.get();
        atlasManager = new AtlasManager();
        atlasManager.load(
            Gdx.files.internal("RESOURCES.json"),
            Gdx.files.internal("ATLASES"),
            "768"          // pick the resolution bucket you want
        );

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
