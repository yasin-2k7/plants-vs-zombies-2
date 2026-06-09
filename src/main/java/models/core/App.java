package models.core;

import controller.MenuController;

public class App {
    private static User currentUser;
    private MenuController currentMenu;


    public static User getCurrentUser() {
        return currentUser;
    }
}
