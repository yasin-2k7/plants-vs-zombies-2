// فایل: view/terminalView/TravelLogMenuView.java
package com.pvz2.view.terminalView;

import controller.TravelLogMenuController;
import com.pvz2.models.enums.commands.TravelLogCommands;
import view.View;

import java.util.regex.Matcher;

public class TravelLogMenuView implements View {
    private static TravelLogMenuView instance;
    private TravelLogMenuController controller;

    private TravelLogMenuView(TravelLogMenuController controller) {
        this.controller = controller;
    }

    public static TravelLogMenuView getInstance() {
        if (instance == null) {
            instance = new TravelLogMenuView(new TravelLogMenuController());
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for (TravelLogCommands cmd : TravelLogCommands.values()) {
            Matcher matcher = cmd.matcher(command);
            if (matcher.matches()) {
                commandFound = true;
                switch (cmd) {
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        break;
                    case MENU_EXIT:
                        controller.exitMenu();
                        break;
                    case TRAVEL_LOG_PAGE:
                        String pageName = matcher.group(1);
                        String result = controller.changePage(pageName);
                        System.out.println(result);
                        controller.displayCurrentPage();
                        break;
                    case PLAY_MINIGAME:
                        String minigameName = matcher.group(1);
                        int level = Integer.parseInt(matcher.group(2));
                        controller.selectMinigame(minigameName, level);
                        break;
                    case MENU_ENTER:
                        System.out.println("Entering sub-menu not implemented.");
                        break;
                    default:
                        break;
                }
                break;
            }
        }
        if (!commandFound) {
            System.out.println("Invalid command in Travel Log.");
        }
    }

    public void showCurrentPage() {
        controller.displayCurrentPage();
    }
}
