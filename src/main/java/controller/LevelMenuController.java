package controller;

import models.core.App;
import models.enums.Chapter;
import models.world.Cell;

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
}
