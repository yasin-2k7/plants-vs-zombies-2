package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.User;

public class SettingMenuController implements MenuController {
    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        //needs edit
//        AppView.currentScreen = MainMenuView.getInstance();
    }

    public String changeDifficulty(int newLevel) {
        if (newLevel < 1 || newLevel > 5) {
            return "Difficulty level must be between 1 and 5";
        }

        User user = App.getCurrentUser();
        user.setGameDifficulty(newLevel);
        return "Difficulty level changed to " + newLevel;
    }

    public void showCurrentMenu() {
        //needs edit
//        GameMenuView.getInstance().showResult("Current menu: settings menu");
    }
}
