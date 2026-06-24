package models.core;

import controller.MenuController;
import controller.NewsMenuController;
import models.projectile.strategy.FirePeaStrategy;
import models.projectile.strategy.HitStrategy;
import models.projectile.strategy.IcePeaStrategy;
import models.projectile.strategy.RegularPeaStrategy;
import models.world.GameWorld;

import java.util.ArrayList;

public class App {
    private static User currentUser;
    private static MenuController currentMenu;
    private static GameWorld currentGame;



    public static final HitStrategy FIRE_PEA = new FirePeaStrategy();
    public static final HitStrategy ICE_PEA = new IcePeaStrategy();
    public static final HitStrategy REGULAR_PEA = new RegularPeaStrategy();
    public static final HitStrategy ICE_MELON = new FirePeaStrategy();

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
}
