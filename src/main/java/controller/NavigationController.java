package controller;

import models.core.App;

public class NavigationController {
    public void changeCurrentMenu(){
        App.getCurrentMenu().changeMenu();
    }
}
