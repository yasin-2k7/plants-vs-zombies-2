package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.enums.Chapter;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.LevelFactory;
//import com.pvz2.models.world.LevelFactory;
//import view.terminalView.AppView;
//import view.terminalView.GameMenuView;
//import view.terminalView.PlantMenuView;

import java.util.List;

public class LevelMenuController implements MenuController {

    public static Cell[][] getGameCells() {
        return App.getCurrentGame().getGrid();
    }

    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {

    }

    public String chooseLevel(int level) {
        User user = App.getCurrentUser();
        if (user.getUnlockedChapter() == user.getCurrentChapter().ordinal() + 1 && user.getUnlockedLevel() < level) {
            return "this level is locked!";
        }

        GameWorld game = LevelFactory.createLevel(user.getCurrentChapter(), level);
        App.setCurrentGame(game);
        App.getCurrentUser().setCurrentLevel(level);
        game.initialize();
        if (game.isConveyorMode()) {
            //needs edit
//            AppView.setCurrentScreen(GameMenuView.getInstance());
            return "level started!";
        }
        else {
            //needs edit
//            AppView.setCurrentScreen(PlantMenuView.getInstance());
//            PlantMenuView.getInstance().getController().reset();
            return "";
        }
    }

    public List<String> getLevelsToShow() {
        Chapter currentChapter = App.getCurrentUser().getCurrentChapter();

        if (currentChapter == null) {
            return List.of();
        }

        switch (currentChapter) {
            case EGYPT:
                return List.of("Egypt - Level 1", "Egypt - Level 2", "Egypt - Level 3");
            case BIG_WAVE_BEACH:
                return List.of("Big Wave Beach - Level 1", "Big Wave Beach - Level 2");
            default:
                return List.of();
        }
    }

    public void showCurrentMenu() {
        //needs edit
//        GameMenuView.getInstance().showResult("Current menu: level menu");
    }
}
