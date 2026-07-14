package controller;

import models.core.App;
import models.core.User;
import view.terminalView.*;

public class MainMenuController implements MenuController {

    @Override
    public void changeMenu() {
    }

    public String enterMenu(String menuName) {
        switch (menuName.toLowerCase()) {
            case "play":
                AppView.setCurrentScreen(GameMenuView.getInstance());
                return "Entering Game menu...";
            case "settings":
                AppView.setCurrentScreen(SettingMenuView.getInstance());
                return "Entering Settings menu...";
            case "news":
                AppView.setCurrentScreen(NewsMenuView.getInstance());
                return "Entering News menu...";
            case "profile":
                AppView.setCurrentScreen(ProfileMenuView.getInstance());
                return "Entering Profile menu...";
            default:
                return "Invalid menu name.";
        }
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

    public String showCurrentMenu() {
        return "Main Menu";
    }
}