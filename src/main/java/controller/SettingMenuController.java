package controller;

import models.core.App;
import models.core.User;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.MainMenuView;

public class SettingMenuController implements MenuController{
    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        AppView.currentScreen = MainMenuView.getInstance();
    }

    public String changeDifficulty(int newLevel){
        if (newLevel < 1 || newLevel > 5) {
            return "Difficulty level must be between 1 and 5";
        }

        User user = App.getCurrentUser();
        user.setGameDifficulty(newLevel);
        return "Difficulty level changed to " + newLevel;
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: settings menu");
    }
}
