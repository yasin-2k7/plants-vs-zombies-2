package view.terminalView;

import controller.GameMenuController;
import controller.SignupMenuController;
import models.enums.commands.GameMenuCommands;
import models.enums.commands.SignupMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class GameMenuView implements View{
    private static GameMenuView instance;
    public static GameMenuView getInstance(){
        if (instance == null){
            instance = new GameMenuView();
            instance.controller = new GameMenuController();
            return instance;
        }
        return instance;
    }
    private GameMenuController controller;
    @Override
    public void processCommand(String command) {
        for (GameMenuCommands gameMenuCommands : GameMenuCommands.values()) {
            Matcher matcher = gameMenuCommands.matcher(command);
            if (matcher.matches()) {
                switch (gameMenuCommands) {
                    case ADVANCE_TIME:
                        int count = Integer.parseInt(matcher.group("count"));
                        if (count <= 0){
                            System.out.println("Count must be an integer bigger than 0!");
                            return;
                        }
                        controller.advanceTime(count);
                        break;

                }
            }
        }
    }

    public void showResult(String message){
        System.out.println(message);
    }

}
