package controller;

import view.terminalView.AppView;
import view.terminalView.ChapterMenuView;

public class MainMenuController implements MenuController{
    @Override
    public void changeMenu() {
        AppView.currentScreen = ChapterMenuView.getInstance(new ChapterMenuController());
    }

    @Override
    public void exitMenu() {

    }
}
