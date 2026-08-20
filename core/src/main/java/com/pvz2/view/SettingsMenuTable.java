package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pvz2.Main;
import com.pvz2.controller.GameMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;

public class SettingsMenuTable extends Table {

    public SettingsMenuTable(Main game, Skin skin) {
        super(skin);
        pad(20);

        User user = App.getCurrentUser();

        Label difficultyLabel = new Label("Difficulty Level:", skin, "medium");
        difficultyLabel.setColor(Color.BLACK);
        int currentDiff = (user != null) ? user.getGameDifficulty() : 1;
        final Label difficultyValLabel = new Label(String.valueOf(currentDiff), skin, "medium");
        difficultyValLabel.setColor(Color.BLACK);

        final Slider difficultySlider = new Slider(1f, 5f, 1f, false, skin);
        difficultySlider.setValue(currentDiff);
        difficultySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int val = (int) difficultySlider.getValue();
                difficultyValLabel.setText(String.valueOf(val));
                if (user != null) {
                    user.setGameDifficulty(val);
                }
            }
        });

        Label speedLabel = new Label("Game Speed:", skin, "medium");
        speedLabel.setColor(Color.BLACK);
        int currentSpeed = (user != null) ? user.getGameSpeed() : 1;
        final Label speedValLabel = new Label(currentSpeed + "x", skin, "medium");
        speedValLabel.setColor(Color.BLACK);

        final Slider speedSlider = new Slider(1f, 3f, 1f, false, skin);
        speedSlider.setValue(currentSpeed);
        speedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int val = (int) speedSlider.getValue();
                speedValLabel.setText(val + "x");
                if (user != null) {
                    user.setGameSpeed(val);
                }
            }
        });

        final CheckBox showGridCheckBox = new CheckBox(" Show Lawn Grid Lines (Red)", skin);
        showGridCheckBox.getLabel().setColor(Color.BLACK);
        if (user != null) {
            showGridCheckBox.setChecked(user.isShowGrid());
        }
        showGridCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (user != null) {
                    user.setShowGrid(showGridCheckBox.isChecked());
                }
            }
        });

        final CheckBox debugCheckBox = new CheckBox(" Enable Debug Mode (Add Sun, Food, Coins, Gems)", skin);
        debugCheckBox.getLabel().setColor(Color.BLACK);
        if (user != null) {
            debugCheckBox.setChecked(user.isDebugMode());
        }
        debugCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (user != null) {
                    user.setDebugMode(debugCheckBox.isChecked());
                    if (GameMenuController.getScreen() instanceof MainMenuScreen mainMenuScreen){
                        mainMenuScreen.getResourcesTable().build();
                    }
                }
            }
        });

        add(difficultyLabel).left().padBottom(15);
        add(difficultySlider).width(220).padBottom(15).padLeft(10);
        add(difficultyValLabel).left().padLeft(15).padBottom(15).row();

        add(speedLabel).left().padBottom(20);
        add(speedSlider).width(220).padBottom(20).padLeft(10);
        add(speedValLabel).left().padLeft(15).padBottom(20).row();

        add(showGridCheckBox).colspan(3).left().padBottom(15).row();
        add(debugCheckBox).colspan(3).left().padBottom(15).row();
    }
}
