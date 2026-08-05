package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserManager;
import com.pvz2.models.mupoint.MuPointLevel;
import com.pvz2.models.world.GameWorld;


public class MainMenuController implements MenuController {
    @Override
    public void changeMenu() {
    }

    public String enterMenu(String menuName) {
        //needs edit
//        switch (menuName.toLowerCase()) {
//            case "play":
//                AppView.setCurrentScreen(ChapterMenuView.getInstance());
//                return "Entering Chapter menu...";
//            case "settings":
//                AppView.setCurrentScreen(SettingMenuView.getInstance(new SettingMenuController()));
//                return "Entering Settings menu...";
//            case "news":
//                AppView.setCurrentScreen(NewsMenuView.getInstance());
//                return "Entering News menu...";
//            case "profile":
//                AppView.setCurrentScreen(ProfileMenuView.getInstance(new ProfileMenuController()));
//                return "Entering Profile menu...";
//            case "green house":
//                AppView.setCurrentScreen(GreenhouseMenuView.getInstance());
//                return "Entering green House...";
//            case "travel log":
//                AppView.setCurrentScreen(TravelLogMenuView.getInstance());
//                TravelLogMenuView.getInstance().showCurrentPage();
//                return "Entering Travel Log...";
//            case "mu point":
//                GameWorld game = MuPointLevel.createMuPointLevel();
//                App.setCurrentGame(game);
//
//                AppView.setCurrentScreen(PlantMenuView.getInstance());
//                PlantMenuView.getInstance().getController().reset();
//                return "Entering Mu Point...";
//
//            case "leaderboard":
//                AppView.setCurrentScreen(LeaderboardMenuView.getInstance());
//                LeaderboardMenuView.getInstance().showLeaderboard();
//                return "Entering Leaderboard...";
//            default:
//                return "Invalid menu name.";
//        }
        return null;
    }

    public void showMenus() {
        //needs edit
//        MainMenuView.getInstance().showResult("MENUS\n" +
//                "-play\n" +
//                "-setting\n" +
//                "-news\n" +
//                "-profile\n" +
//                "-green house\n" +
//                "-travel log\n" +
//                "-leaderboard");
    }


    @Override
    public void exitMenu() {

    }

    public String logout() {
        User user = App.getCurrentUser();
        if (user == null) {
            return "No user is logged in.";
        }
        UserManager.logout();
        App.setCurrentUser(null);
        //needs edit
//        AppView.setCurrentScreen(SignupMenuView.getInstance());
        return "Logged out successfully.";
    }


    public void showCurrentMenu() {
        //needs edit
//        GameMenuView.getInstance().showResult("Current menu: main menu");
    }
}
