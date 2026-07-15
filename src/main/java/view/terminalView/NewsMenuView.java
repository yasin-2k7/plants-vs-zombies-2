package view.terminalView;

import controller.LevelMenuController;
import controller.LoginMenuController;
import controller.NewsMenuController;
import view.View;

public class NewsMenuView implements View{
    private static NewsMenuView instance;
    private NewsMenuController controller;
    public NewsMenuView getInstance(){
        if (instance == null){
            instance = new NewsMenuView(controller);
            return instance;
        }
        return instance;
    }
    public NewsMenuView(NewsMenuController controller) {
        this.controller = controller;
    }

    @Override
    public void processCommand(String command) {

    }
}
