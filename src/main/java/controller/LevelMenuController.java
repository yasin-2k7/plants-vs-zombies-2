package controller;

import models.core.App;
import models.world.Cell;

public class LevelMenuController implements MenuController{

    public static Cell[][] getGameCells(){
        return App.getCurrentGame().getGrid();
    }

    @Override
    public void changeMenu() {

    }
}
