package view.terminalView;

import controller.LevelMenuController;
import controller.SettingMenuController;
import models.enums.commands.LoginMenuCommands;
import models.enums.commands.SettingMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class SettingMenuView implements View{
    private static SettingMenuView instance;
    private SettingMenuController controller;

    public SettingMenuView(SettingMenuController controller) {
        this.controller = controller;
    }

    public static SettingMenuView getInstance(SettingMenuController controller){
        if (instance == null){
            instance = new SettingMenuView(controller);
            return instance;
        }
        return instance;
    }
    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for(SettingMenuCommands settingMenuCommands : SettingMenuCommands.values()) {
            Matcher matcher = settingMenuCommands.matcher(command);
            if (matcher.matches()) {
                commandFound = true;
                switch (settingMenuCommands){
                    case MENU_EXIT:
                        controller.exitMenu();
                        break;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        break;
                    case MENU_SETTINGS_CHANGE_DIFFICULTY:
                        String result = controller.changeDifficulty(Integer.parseInt(matcher.group(1)));
                        System.out.println(result);
                        break;
                }
                break;
            }
        }
        if(!commandFound){
            System.out.println("invalid command in settings menu");
        }
    }


}
