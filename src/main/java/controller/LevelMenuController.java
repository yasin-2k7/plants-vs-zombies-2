package controller;

import models.core.App;
import models.core.User;
import models.enums.Chapter;
import models.enums.LevelType;
import models.world.Cell;
import models.world.ChapterWorld.AncientEgyptWorld;
import models.world.GameWorld;
import models.world.LevelFactory;
import models.world.levelSetup.NormalLevelSetup;
import models.world.loseCondition.NormalLose;
import models.world.winCondition.NormalWin;
import models.zombie.wave.Wave;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.PlantMenuView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LevelMenuController implements MenuController{

    public static Cell[][] getGameCells(){
        return App.getCurrentGame().getGrid();
    }

    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {

    }

    public String chooseLevel(int level){
        User user = App.getCurrentUser();
        if (user.getUnlockedChapter() == user.getCurrentChapter().ordinal()+1 && user.getUnlockedLevel() < level){
            return "this level is locked!";
        }

        GameWorld game = LevelFactory.createLevel(user.getCurrentChapter(), level);
        App.setCurrentGame(game);
        if (game.isConveyorMode())
            AppView.setCurrentScreen(GameMenuView.getInstance());
        else{
            AppView.setCurrentScreen(PlantMenuView.getInstance());
            PlantMenuView.getInstance().getController().reset();
        }
        return "level started!";

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

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: level menu");
    }
}
