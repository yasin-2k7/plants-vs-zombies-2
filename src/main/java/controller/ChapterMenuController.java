package controller;

import models.core.App;
import models.core.User;
import models.enums.Chapter;
import view.View;
import view.terminalView.AppView;
import view.terminalView.ChapterMenuView;
import view.terminalView.LevelMenuView;

public class ChapterMenuController implements MenuController{

    @Override
    public void changeMenu() {
        AppView.currentScreen = LevelMenuView.getInstance(new LevelMenuController());
    }

    @Override
    public void exitMenu() {

    }

    public String chooseChapter(Chapter chapter){
        User user = App.getCurrentUser();
        if (user.getUnlockedChapter() <= chapter.ordinal()){
            return "this chapter is locked!";
        }
        user.setCurrentChapter(chapter);
        AppView.currentScreen = LevelMenuView.getInstance(new LevelMenuController());
        return "you choose " + chapter;
    }


}
