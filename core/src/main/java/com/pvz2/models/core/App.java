package com.pvz2.models.core;

import com.pvz2.Main;
import com.pvz2.models.greenhouse.GreenHouse;
import com.pvz2.models.plant.factory.PlantFactory;
import com.pvz2.models.world.GameWorld;

public class App {
    private static final PlantFactory FACTORY = new PlantFactory();
    private static final float CELL_HEIGHT = 125f;
    private static final float CELL_WIDTH = 142f;
    private static final float FIRST_CELL_Y = 175f;
    private static final float FIRST_CELL_X = 532f;
    private static User currentUser;
    private static GameWorld currentGame;
    private static Main gameApp;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
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

    public static float getFirstCellX() {
        return FIRST_CELL_X;
    }

    public static float getCellWidth() {
        return CELL_WIDTH;
    }

    public static float getFirstCellY() {
        return FIRST_CELL_Y;
    }

    public static PlantFactory getFactory() {
        return FACTORY;
    }

    public static GreenHouse getGreenhouse() {
        if (currentUser == null) return null;
        return currentUser.getGreenhouse();
    }

    public static float getCellCenterX(int col) {
        return getFirstCellX() + col * getCellWidth();
    }

    public static float getCellCenterY(int row) {
        return getFirstCellY() + row * getCellHeight();
    }

    public static String getArmoredZombieName(String id) {
        if (id == null) {
            return "Regular Zombie";
        }
        switch (id) {
            case "ZombieArmor1":
                return "ZombieConeHead";
            case "ZombieArmor2":
                return "ZombieBucketHead";
            case "ZombieDarkArmor3":
                return "ZombieKnight";
            case "ZombieArmor4":
                return "ZombieBrickHead";
            default:
                return id;
        }
    }

    public static String getZombieId(String name) {
        if (name == null) {
            return null;
        }
        switch (name) {
            case "ZombieConehead":
            case "ZombieConeHead":
                return "ZombieArmor1";
            case "ZombieBuckethead":
            case "ZombieBucketHead":
                return "ZombieArmor2";
            case "ZombieKnight":
                return "ZombieDarkArmor3";
            case "ZombieBrickhead":
            case "ZombieBrickHead":
                return "ZombieArmor4";
            default:
                return name;
        }
    }

    public static void setGameApp(Main gameApp) {
        App.gameApp = gameApp;
    }

    public static Main getGameApp() {
        return gameApp;
    }

}
