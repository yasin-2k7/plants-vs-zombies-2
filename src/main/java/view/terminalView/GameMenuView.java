package view.terminalView;

import view.View;

public class GameMenuView implements View{
    private static GameMenuView instance;
    private GameMenuView() {
    }

    public static GameMenuView getInstance() {
        if (instance == null) {
            instance = new GameMenuView();
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {

    }
}
