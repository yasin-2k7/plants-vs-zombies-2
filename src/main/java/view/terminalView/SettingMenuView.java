package view.terminalView;

import controller.LevelMenuController;
import controller.SettingMenuController;
import view.View;

public class SettingMenuView implements View{
    private static SettingMenuView instance;
    private SettingMenuController controller;

    public SettingMenuView(SettingMenuController controller) {
        this.controller = controller;
    }

    public static SettingMenuView getInstance(SettingMenuController controller){
        if (instance == null){
            instance = new SettingMenuView(new SettingMenuController());
            return instance;
        }
        return instance;
    }
    @Override
    public void processCommand(String command) {

    }
}
