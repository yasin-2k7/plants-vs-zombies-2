package view.terminalView;

import controller.LevelMenuController;
import controller.LoginMenuController;
import controller.ProfileMenuController;
import view.View;

public class ProfileMenuView implements View {
    private static ProfileMenuView instance;
    private ProfileMenuController controller;
    public ProfileMenuView getInstance(){
        if (instance == null){
            instance = new ProfileMenuView(controller);
            return instance;
        }
        return instance;
    }
    public ProfileMenuView(ProfileMenuController controller) {
        this.controller = controller;
    }

    @Override
    public void processCommand(String command) {

    }
}
