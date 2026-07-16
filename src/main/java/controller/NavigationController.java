package controller;

import models.core.App;
import view.terminalView.GameMenuView;

public class NavigationController {
    public void changeCurrentMenu(){
        App.getCurrentMenu().changeMenu();
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: navigation menu");
    }
}
