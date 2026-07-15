package view.terminalView;

import view.View;

public class LoginMenuView implements View{
    private static LoginMenuView instance;
    public LoginMenuView getInstance(){
        if (instance == null){
            instance = new LoginMenuView();
            return instance;
        }
        return instance;
    }
    @Override
    public void processCommand(String command) {

    }
}
