package view.terminalView;

import controller.LevelMenuController;
import controller.LoginMenuController;
import controller.NewsMenuController;
import models.enums.commands.LeaderboardMenuCommands;
import models.enums.commands.NewsMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class NewsMenuView implements View{
    private static NewsMenuView instance;
    private NewsMenuController controller;

    public static NewsMenuView getInstance(){
        if (instance == null){
            instance = new NewsMenuView(new NewsMenuController());
            return instance;
        }
        return instance;
    }
    public NewsMenuView(NewsMenuController controller) {
        this.controller = controller;
    }

    @Override
    public void processCommand(String command) {
        command = command.trim();

        for (NewsMenuCommands cmd : NewsMenuCommands.values()) {
            Matcher matcher = cmd.matcher(command);
            if (matcher.matches()) {
                switch (cmd) {
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        return;
                    case MENU_EXIT:
                        controller.exitMenu();
                        return;
                    case MENU_NEWS_SHOW_ALL:
                        controller.showNews();
                        return;
                    case MENU_NEWS_SHOW_UNREAD:
                        controller.showNewsUnread();
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("Unknown command in Leaderboard menu.");
    }

    public void showResult(String message){
        System.out.println(message);
    }




}
