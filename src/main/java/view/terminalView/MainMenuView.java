package view.terminalView;

import controller.MainMenuController;
import controller.SignupMenuController;
import models.enums.commands.LoginMenuCommands;
import models.enums.commands.MainMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class MainMenuView implements View{
    private static MainMenuView instance;
    private MainMenuController controller;

    public MainMenuView(MainMenuController controller) {
        this.controller = controller;
    }

    public static MainMenuView getInstance(MainMenuController controller){
        if (instance == null){
            instance = new MainMenuView(controller);
        }
        return instance;
    }
    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for(MainMenuCommands mainMenuCommands : MainMenuCommands.values()) {
            Matcher matcher = mainMenuCommands.matcher(command);
            if (matcher.matches()) {
                commandFound = true;
                switch (mainMenuCommands){
                    case MENU_ENTER:
                        controller.changeMenu();
                        break;
                    case MENU_SHOW_CURRENT:
                        System.out.println(AppView.currentScreen);
                        break;

                }
                break;
            }

        }
        if(!commandFound){
            System.out.println("invalid command for main menu");
        }
    }
}
