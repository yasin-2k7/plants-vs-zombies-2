package controller;

import models.core.App;
import models.core.PasswordHasher;
import models.core.User;
import models.core.UserDataManager;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.MainMenuView;
import view.terminalView.SignupMenuView;

import java.util.ArrayList;
import java.util.List;

public class ProfileMenuController implements MenuController{
    private SignupMenuController signupMenuController;
    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        AppView.currentScreen = MainMenuView.getInstance();
    }

    public String changeUsername(String newUsername){
        User user = App.getCurrentUser();

        if(user.getUsername().equals(newUsername)){
            return "new username and your username are similar.";
        }



        user.setUsername(newUsername);
        return "your username changed";
    }

    public String changeNickname(String newNickname){
        User user = App.getCurrentUser();

        if(user.getNickname().equals(newNickname)){
            return "new nickname and your nickname are similar.";
        }


        user.setNickname(newNickname);
        return "your nickname changed";
    }

    public String changeEmail(String newEmail){
        User user = App.getCurrentUser();

        if(user.getEmail().equals(newEmail)){
            return "new email and your email are similar.";
        }



        user.setEmail(newEmail);
        return "your email changed";
    }

    public String changePassword(String password, String newPassword){
        User user = App.getCurrentUser();

        String hashPassword = PasswordHasher.hashSHA256(password);
        String hashNewPass = PasswordHasher.hashSHA256(newPassword);


        if(!user.getHashPassword().equals(hashPassword)){
            return "your password is incorrect.";
        }

        if(user.getHashPassword().equals(hashNewPass)){
            return "new pass and your pass are similar.";
        }



        user.setHashPassword(hashPassword);
        return "your pass changed.";


    }

    public String showInfo(){
        User user = App.getCurrentUser();

        String info = "Username: " + user.getUsername() + "\n" +
                    "Nickname: " + user.getNickname() + "\n" +
                    "Games number: "  + "\n" +
                    "Coins: " + user.getCoins() + "\n" +
                    "Gems: " + user.getGems() + "\n" +
                    "Levels number: " + (user.getUserLevel() - 1) + "\n" +
                    "Mu point: ";

        return info;
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: profile menu");
    }

}
