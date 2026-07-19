package view.terminalView;

import controller.LeaderboardMenuController;
import controller.MainMenuController;
import models.enums.commands.LeaderboardMenuCommands;
import models.enums.commands.MainMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class LeaderboardMenuView implements View {

    private static LeaderboardMenuView instance;
    private LeaderboardMenuController controller;

    private LeaderboardMenuView(LeaderboardMenuController controller) {
        this.controller = controller;
    }
    public static LeaderboardMenuView getInstance() {
        if (instance == null) {
            instance = new LeaderboardMenuView(new LeaderboardMenuController());
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {
        command = command.trim();

        for (LeaderboardMenuCommands cmd : LeaderboardMenuCommands.values()) {
            Matcher matcher = cmd.matcher(command);
            if (matcher.matches()) {
                switch (cmd) {
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        return;
                    case MENU_EXIT:
                        controller.exitMenu();
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("Unknown command in Leaderboard menu.");
    }

}
