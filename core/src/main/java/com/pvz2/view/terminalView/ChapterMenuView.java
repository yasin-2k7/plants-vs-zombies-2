package com.pvz2.view.terminalView;

import com.pvz2.controller.ChapterMenuController;
import com.pvz2.models.enums.Chapter;
import com.pvz2.models.enums.commands.ChapterMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class ChapterMenuView implements View {
    private static ChapterMenuView instance;
    String result;
    private ChapterMenuController controller;

    public ChapterMenuView(ChapterMenuController controller) {
        this.controller = controller;
    }

    public static ChapterMenuView getInstance() {
        if (instance == null) {
            instance = new ChapterMenuView(new ChapterMenuController());
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for (ChapterMenuCommands chapterMenuCommands : ChapterMenuCommands.values()) {
            Matcher matcher = chapterMenuCommands.matcher(command);
            if (matcher.matches()) {
                commandFound = true;
                switch (chapterMenuCommands) {
                    case CHOOSE_CHAPTER:
                        String chapterStr = matcher.group(1);
                        Chapter chapter = Chapter.fromString(chapterStr);
                        if (chapter != null) {
                            result = controller.chooseChapter(chapter);
                            System.out.println(result);
                        } else {
                            System.out.println("invalid chapter");
                        }
                        break;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        break;
                    case MENU_ENTER:
                        controller.changeMenu();
                        break;
                    case MENU_EXIT:
                        controller.exitMenu();
                        break;
                    case MENU_GREENHOUSE:
                        controller.greenHouse();
                        break;
                    case MENU_CHEAT_ADD:
                        String type = matcher.group("type");
                        int amount = Integer.parseInt(matcher.group("amount"));
                        controller.cheatAdd(type, amount);
                        break;
                    case MENU_TRAVEL_LOG:
                        controller.travelLog();
                        break;
                    case MENU_LEADERBOARD:
                        controller.leaderboard();
                        break;
                    case MENU_GEM_WALLET:
                        controller.gemWallet();
                        break;
                    case MENU_COIN_WALLET:
                        controller.coinWallet();
                        break;
                }
                break;
            }
        }
        if (!commandFound) {
            System.out.println("invalid command in chapter menu.");
        }

    }


    public void showResult(String message) {
        System.out.println(message);
    }


}
