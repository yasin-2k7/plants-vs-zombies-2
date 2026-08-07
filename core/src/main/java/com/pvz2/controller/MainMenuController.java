package com.pvz2.controller;

import com.pvz2.Main;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserManager;
import com.pvz2.models.mupoint.MuPointLevel;
import com.pvz2.models.world.GameWorld;
import com.pvz2.view.*;


public class MainMenuController implements MenuController {
    MainMenuScreen mainMenuScreen;

    public MainMenuController(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    @Override
    public void changeMenu() {
    }

    public void enterMenu(String menuName) {
        switch (menuName.toLowerCase()) {
            case "play":
                mainMenuScreen.fadeAndSwitchScreen(new ChapterMenuScreen(mainMenuScreen.getGame()));
                break;
            case "settings":
                mainMenuScreen.fadeAndSwitchScreen(new SettingMenuScreen(mainMenuScreen.getGame()));
                break;
            case "news":
                mainMenuScreen.fadeAndSwitchScreen(new NewsMenuScreen(mainMenuScreen.getGame()));
                break;
            case "profile":
                mainMenuScreen.fadeAndSwitchScreen(new ProfileMenuScreen(mainMenuScreen.getGame()));
                break;
            case "mu point":
                GameWorld game = MuPointLevel.createMuPointLevel();
                App.setCurrentGame(game);
//
//                AppView.setCurrentScreen(PlantMenuView.getInstance());
//                PlantMenuView.getInstance().getController().reset();

            case "leaderboard":
                mainMenuScreen.fadeAndSwitchScreen(new LeaderboardMenuScreen(mainMenuScreen.getGame()));
                break;
        }

    }


    @Override
    public void exitMenu() {
        logout();
    }

    public void logout() {
        User user = App.getCurrentUser();
        if (user == null) {
            return;
        }
        UserManager.logout();
        App.setCurrentUser(null);
        mainMenuScreen.fadeAndSwitchScreen(new SignupMenuScreen(mainMenuScreen.getGame()));
    }
}
