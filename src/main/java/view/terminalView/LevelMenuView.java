package view.terminalView;

import controller.LevelMenuController;
import models.enums.commands.LevelMenuCommands;
import view.View;

import java.util.List;
import java.util.regex.Matcher;

public class LevelMenuView implements View{
    private static LevelMenuView instance;
    private LevelMenuController controller;
    public static LevelMenuView getInstance(LevelMenuController controller){
        if (instance == null){
            instance = new LevelMenuView(controller);
        }
        return instance;
    }
    public LevelMenuView(LevelMenuController controller){
        this.controller = controller;
    }

    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for(LevelMenuCommands levelMenuCommands : LevelMenuCommands.values()) {
            Matcher matcher = levelMenuCommands.matcher(command);
            if (matcher.matches()) {
                commandFound = true;
                switch (levelMenuCommands){
                    case SHOW_LEVELS:
                        List<String> levels = controller.getLevelsToShow();
                        for(String level : levels){
                            System.out.println(level);
                        }
                        break;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        break;
                    case CHOOSE_LEVEL:
                        int level = Integer.parseInt(matcher.group(1));
                        System.out.println(controller.chooseLevel(level));
                        break;

                    case MENU_EXIT:
                        controller.exitMenu();
                        System.out.println("main menu");
                        break;


                }
                break;
            }
        }
        if(!commandFound){
            System.out.println("invalid command in level menu.");
        }
    }


}
