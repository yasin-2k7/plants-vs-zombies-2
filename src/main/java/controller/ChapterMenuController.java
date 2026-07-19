package controller;

import models.core.App;
import models.core.User;
import models.enums.Chapter;
import view.View;
import view.terminalView.*;

public class ChapterMenuController implements MenuController{

    @Override
    public void changeMenu() {
        AppView.currentScreen = CollectionMenuView.getInstance();
    }

    @Override
    public void exitMenu() {
        AppView.setCurrentScreen(MainMenuView.getInstance());
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

    public void greenHouse(){
        AppView.setCurrentScreen(GreenhouseMenuView.getInstance());
    }

    public void cheatAdd(String type, int amount){
        switch (type){
            case "coin":
                App.getCurrentUser().setCoins(App.getCurrentUser().getCoins()+amount);
                break;
            case "diamond":
                App.getCurrentUser().setGems(App.getCurrentUser().getGems()+amount);
                break;
        }
        ChapterMenuView.getInstance().showResult("Added successfully.");
    }

    public void travelLog(){
        AppView.setCurrentScreen(TravelLogMenuView.getInstance());
    }

    public void leaderboard(){
        AppView.setCurrentScreen(LeaderboardMenuView.getInstance());
    }

    public void coinWallet(){
        ChapterMenuView.getInstance().showResult("Your coins amount: " + App.getCurrentUser().getCoins());
    }

    public void gemWallet(){
        ChapterMenuView.getInstance().showResult("Your gems amount: " + App.getCurrentUser().getGems());
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: chapter menu");
    }


}
