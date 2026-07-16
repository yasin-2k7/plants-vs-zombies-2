package view.terminalView;

import controller.CollectionMenuController;
import controller.LevelMenuController;
import controller.LoginMenuController;
import view.View;

public class CollectionMenuView implements View {
    private static CollectionMenuView instance;

    public static CollectionMenuView getInstance(){
        if (instance == null){
            instance = new CollectionMenuView(new CollectionMenuController());
            return instance;
        }
        return instance;
    }
    public CollectionMenuView(CollectionMenuController controller) {
        this.controller = controller;
    }

    private CollectionMenuController controller;

    @Override
    public void processCommand(String command) {

    }


}
