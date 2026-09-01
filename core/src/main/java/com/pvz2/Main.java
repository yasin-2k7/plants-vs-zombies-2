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
import com.pvz2.network.NetworkClient;
import com.pvz2.view.*;
import com.pvz2.view.audios.AudioManager;
import com.pvz2.view.audios.GameMusic;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;
import pvz.skin.PvzSkin;

import java.io.IOException;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public TextureBank textureBank;
    public PamPlayer pamPlayer;
    public Skin skin;
    public SpriteBatch batch;



    @Override
    public void create() {
        try {
            NetworkClient.get().connect("localhost", 8080);
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
            // fall through — screens should handle a disconnected NetworkClient gracefully,
            // e.g. sendRequest already throws/times out rather than hanging forever
        }

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
            AudioManager.getInstance().playMusic(GameMusic.TITLE, true);
            GameMenuController.setScreen(mainMenuScreen);
        } else {
            setScreen(new LoginMenuScreen(this));
            AudioManager.getInstance().playMusic(GameMusic.TITLE, true);
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
