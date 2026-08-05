package com.pvz2.controller;


import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.enums.Chapter;

public class ChapterMenuController implements MenuController {

    @Override
    public void changeMenu() {
        //needs edit
    }

    @Override
    public void exitMenu() {
        //needs edit
    }

    public String chooseChapter(Chapter chapter) {
        User user = App.getCurrentUser();
        if (user.getUnlockedChapter() <= chapter.ordinal()) {
            return "this chapter is locked!";
        }
        user.setCurrentChapter(chapter);
//        AppView.currentScreen = LevelMenuView.getInstance(new LevelMenuController());
        //needs edit
        return "you choose " + chapter;
    }

    public void greenHouse() {
//        AppView.setCurrentScreen(GreenhouseMenuView.getInstance());
        //needs edit
    }

    public void cheatAdd(String type, int amount) {
        switch (type) {
            case "coin":
                App.getCurrentUser().setCoins(App.getCurrentUser().getCoins() + amount);
                break;
            case "diamond":
                App.getCurrentUser().setGems(App.getCurrentUser().getGems() + amount);
                break;
        }
        //needs edit
//        ChapterMenuView.getInstance().showResult("Added successfully.");
    }

    public void travelLog() {
//        AppView.setCurrentScreen(TravelLogMenuView.getInstance());
        //needs edit
    }

    public void leaderboard() {
//        AppView.setCurrentScreen(LeaderboardMenuView.getInstance());
        //needs edit
    }

    public void coinWallet() {
        //needs edit
//        ChapterMenuView.getInstance().showResult("Your coins amount: " + App.getCurrentUser().getCoins());
    }

    public void gemWallet() {
        //needs edit
//        ChapterMenuView.getInstance().showResult("Your gems amount: " + App.getCurrentUser().getGems());
    }

    public void showCurrentMenu() {
        //needs edit
//        GameMenuView.getInstance().showResult("Current menu: chapter menu");
    }


}
