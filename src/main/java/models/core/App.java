package models.core;

import controller.MenuController;
import models.greenhouse.GreenHouse;
import models.plant.PlantFactory;
import models.world.GameWorld;
import view.View;

public class App {
    private static User currentUser;
    private static MenuController currentMenu;
    private static View currentScreen;
    private static GameWorld currentGame;
    private static final PlantFactory factory = new PlantFactory();

    private static final float CELL_HEIGHT = 100;
    private static final float CELL_WIDTH = 100;
    private static final float FIRST_CELL_X = 0;
    private static final float FIRST_CELL_Y = 0;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {App.currentUser = currentUser;}

    public static MenuController getCurrentMenu() {
        return currentMenu;
    }

    public static GameWorld getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(GameWorld currentGame) {
        App.currentGame = currentGame;
    }

    public static float getCellHeight() {
        return CELL_HEIGHT;
    }

    public static float getCellWidth() {
        return CELL_WIDTH;
    }

    public static float getFirstCellX() {
        return FIRST_CELL_X;
    }

    public static float getFirstCellY() {
        return FIRST_CELL_Y;
    }

    public static PlantFactory getFactory() {
        return factory;
    }

    public static GreenHouse getGreenhouse() {
        if (currentUser == null) return null;
        return currentUser.getGreenhouse();
    }

    public static View getCurrentScreen() {
        return currentScreen;
    }
}
