package view.terminalView;

import controller.LevelMenuController;
import controller.LoginMenuController;
import controller.ProfileMenuController;
import view.View;

public class ProfileMenuView implements View {
    private static ProfileMenuView instance;
    private ProfileMenuController controller;
    public static ProfileMenuView getInstance(){
        if (instance == null){
            instance = new ProfileMenuView();
            instance.controller = new ProfileMenuController();
            return instance;
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {

    }
}
