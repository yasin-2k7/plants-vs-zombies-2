package controller;

import view.terminalView.GreenhouseMenuView;

public class GreenhouseMenuController implements MenuController{

    private GreenhouseMenuView view;

    public GreenhouseMenuController() {
        this.view = new GreenhouseMenuView();
    }

    @Override
    public void changeMenu() {

    }

    public GreenhouseMenuView getView() {
        return view;
    }
}
