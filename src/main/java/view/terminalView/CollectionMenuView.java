package view.terminalView;

import controller.CollectionMenuController;
import controller.LevelMenuController;
import controller.LoginMenuController;
import view.View;

public class CollectionMenuView implements View {
    private static CollectionMenuView instance;
    private CollectionMenuController controller;
    public CollectionMenuView getInstance(){
        if (instance == null){
            instance = new CollectionMenuView(controller);
            return instance;
        }
        return instance;
    }
    public CollectionMenuView(CollectionMenuController controller) {
        this.controller = controller;
    }

    @Override
    public void processCommand(String command) {

    }
}
