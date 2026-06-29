package models.core;

import controller.MenuController;
import models.plant.PlantFactory;
import models.world.GameWorld;

public class App {
    private static User currentUser;
    private static MenuController currentMenu;
    private static GameWorld currentGame;
    private static final PlantFactory factory = new PlantFactory();

    private static final int CELL_HEIGHT = 80;
    private static final int CELL_WIDTH = 50;
    private static final int FIRST_CELL_X = 100;
    private static final int FIRST_CELL_Y = 400;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static MenuController getCurrentMenu() {
        return currentMenu;
    }

    public static GameWorld getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(GameWorld currentGame) {
        App.currentGame = currentGame;
    }

    public static int getCellHeight() {
        return CELL_HEIGHT;
    }

    public static int getCellWidth() {
        return CELL_WIDTH;
    }

    public static int getFirstCellX() {
        return FIRST_CELL_X;
    }

    public static int getFirstCellY() {
        return FIRST_CELL_Y;
    }

    public static PlantFactory getFactory() {
        return factory;
    }

}
