package view.terminalView;

import controller.LoginMenuController;
import controller.SignupMenuController;
import models.enums.commands.LoginMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class LoginMenuView implements View{
    private static LoginMenuView instance;
    private LoginMenuController controller;
    public static LoginMenuView getInstance(LoginMenuController controller){
        if (instance == null){
            instance = new LoginMenuView(controller);
        }
        return instance;
    }
    private String result;
    private String username;
    private String password;
    private String email;

    public LoginMenuView(LoginMenuController controller) {
        this.controller = controller;
    }

    @Override
    public void processCommand(String command) {
        boolean commandFound = false;
        for(LoginMenuCommands loginMenuCommands : LoginMenuCommands.values()){
            Matcher matcher = loginMenuCommands.matcher(command);
            if(matcher.matches()){
                commandFound = true;
                switch (loginMenuCommands){
                    case LOGIN:
                        username = matcher.group(1);
                        password = matcher.group(2);
                        String stayLoggedIn = matcher.group(3);
                        if(stayLoggedIn == null){
                            result = controller.loginUser(username, password, false);
                        } else{
                            result = controller.loginUser(username, password, true);
                        }
                        System.out.println(result);
                        break;
                    case FORGET_PASSWORD:
                        username = matcher.group(1);
                        email = matcher.group(2);
                        result = controller.forgetPassword(username, email);
                        System.out.println(result);
                        break;
                    case ANSWER:
                        String answer = matcher.group(1);
                        result = controller.answerSQ(answer);
                        System.out.println(result);
                        break;
                    case NEW_PASSWORD:
                        String newPass = matcher.group(1);
                        result = controller.newPassword(newPass);
                        System.out.println(result);
                        break;
                    case MENU_ENTER:
                        controller.changeMenu();
                        break;
                    case MENU_EXIT:
                        controller.exitMenu();
                        break;
                    case MENU_SHOW_CURRENT:
                        System.out.println(AppView.currentScreen);
                }
                break;
            }
        }
        if (!commandFound) {
            System.out.println("Invalid command!");
        }
    }
}
