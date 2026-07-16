package controller;

import view.terminalView.GameMenuView;

public class CollectionMenuController implements MenuController{

    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {

    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: collection menu");
    }
}
