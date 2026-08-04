package com.pvz2.view.terminalView;

import controller.LeaderboardMenuController;
import com.pvz2.models.enums.commands.LeaderboardMenuCommands;
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
                    case SORT_LEADERBOARD:
                        String field = matcher.group("field");
                        boolean ascending = matcher.group("order").equals("ascending");
                        controller.showList(field, ascending);
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("Unknown command in Leaderboard menu.");
    }

    public void showLeaderboard() {
        controller.showList("LAST_STAGE", false);
    }

    public void showResult(String message) {
        System.out.println(message);
    }

}
