package controller;

import models.core.App;
import models.core.User;
import models.mupoint.MuPointLevel;
import models.world.GameWorld;
import view.terminalView.*;

public class MainMenuController implements MenuController {
    @Override
    public void changeMenu() {
    }

    public String enterMenu(String menuName) {
        switch (menuName.toLowerCase()) {
            case "play":
                AppView.setCurrentScreen(ChapterMenuView.getInstance());
                return "Entering Chapter menu...";
            case "settings":
                AppView.setCurrentScreen(SettingMenuView.getInstance(new SettingMenuController()));
                return "Entering Settings menu...";
            case "news":
                AppView.setCurrentScreen(NewsMenuView.getInstance());
                return "Entering News menu...";
            case "profile":
                AppView.setCurrentScreen(ProfileMenuView.getInstance(new ProfileMenuController()));
                return "Entering Profile menu...";
            case "green house":
                AppView.setCurrentScreen(GreenhouseMenuView.getInstance());
                return "Entering green House...";
            case "travel log":
                AppView.setCurrentScreen(TravelLogMenuView.getInstance());
                TravelLogMenuView.getInstance().showCurrentPage();
                return "Entering Travel Log...";
            case "mu point":
                GameWorld game = MuPointLevel.createMuPointLevel();
                App.setCurrentGame(game);

                AppView.setCurrentScreen(PlantMenuView.getInstance());
                PlantMenuView.getInstance().getController().reset();
                return "Entering Mu Point...";

            case "leaderboard":
                AppView.setCurrentScreen(LeaderboardMenuView.getInstance());
                LeaderboardMenuView.getInstance().showLeaderboard();
                return "Entering Leaderboard...";
            default:
                return "Invalid menu name.";
        }
    }

    public void showMenus() {
        MainMenuView.getInstance().showResult("MENUS\n" +
                "-play\n" +
                "-setting\n" +
                "-news\n" +
                "-profile\n" +
                "-green house\n" +
                "-travel log\n" +
                "-leaderboard");
    }


    @Override
    public void exitMenu() {

    }

    public String logout() {
        User user = App.getCurrentUser();
        if (user == null) {
            return "No user is logged in.";
        }
        App.setCurrentUser(null);
        AppView.setCurrentScreen(SignupMenuView.getInstance());
        return "Logged out successfully.";
    }


    public void showCurrentMenu() {
        GameMenuView.getInstance().showResult("Current menu: main menu");
    }
}