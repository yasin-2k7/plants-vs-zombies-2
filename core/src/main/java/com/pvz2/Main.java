package com.pvz2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.GameInitializer;
import com.pvz2.models.core.UserManager;
import com.pvz2.models.zombie.ZombieRegistry;
import com.pvz2.view.*;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;
import pvz.skin.PvzSkin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public TextureBank textureBank;
    public PamPlayer pamPlayer;
    public Skin skin;
    public SpriteBatch batch;



    @Override
    public void create() {
        boolean foundUser = UserManager.loadInitialUser();

        GameInitializer.loadPlantUpgrades();
        ZombieRegistry.init();

        batch = new SpriteBatch();

        FileHandle assetsFolder = Gdx.files.internal("");
        textureBank = new TextureBank("768", assetsFolder);
        pamPlayer = new PamPlayer(textureBank, assetsFolder);
        skin = PvzSkin.get();
        App.setGameApp(this);

        if(foundUser){
            for (String zombieName : App.getCurrentUser().getShowedZombies().keySet()){
                App.getCurrentUser().getShowedZombies().put(zombieName, true);
            }
            MainMenuScreen mainMenuScreen = new MainMenuScreen(this);
            setScreen(mainMenuScreen);
            GameMenuController.setScreen(mainMenuScreen);
        } else {
            setScreen(new LoginMenuScreen(this));
        }
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
