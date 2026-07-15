package view.terminalView;

import controller.ChapterMenuController;
import models.enums.Chapter;
import models.enums.commands.ChapterMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class ChapterMenuView implements View {
    private static ChapterMenuView instance;
    private ChapterMenuController controller;
    public ChapterMenuView(ChapterMenuController controller) {
        this.controller = controller;
    }

    public static ChapterMenuView getInstance(){
        if (instance == null){
            instance = new ChapterMenuView(new ChapterMenuController());
        }
        return instance;
    }

    String result;

    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for(ChapterMenuCommands chapterMenuCommands : ChapterMenuCommands.values()) {
            Matcher matcher = chapterMenuCommands.matcher(command);
            if (matcher.matches()) {
                commandFound = true;
                switch (chapterMenuCommands){
                    case CHOOSE_CHAPTER:
                        String chapterStr = matcher.group(1);
                        Chapter chapter = Chapter.fromString(chapterStr);
                        if(chapter != null){
                            result = controller.chooseChapter(chapter);
                            System.out.println(result);
                        } else {
                            System.out.println("invalid chapter");
                        }
                        break;
                    case MENU_SHOW_CURRENT:
                        System.out.println(AppView.currentScreen);
                        break;
                }
                break;
            }
        }
        if(!commandFound){
            System.out.println("invalid command in chapter menu.");
        }

    }
}
