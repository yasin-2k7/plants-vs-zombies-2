package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.PasswordHasher;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserDataManager;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.MainMenuView;

import java.util.List;

public class ProfileMenuController implements MenuController {
    private SignupMenuController signupMenuController = new SignupMenuController();

    @Override
    public void changeMenu() {
    }

    @Override
    public void exitMenu() {
        AppView.currentScreen = MainMenuView.getInstance();
    }

    public String changeUsername(String newUsername) {
        User user = App.getCurrentUser();
        String oldUsername = user.getUsername();

        if (user.getUsername().equals(newUsername)) {
            return "new username and your username are similar.";
        }

        List<String> errors = signupMenuController.getUsernameErrors(newUsername);
        if (!errors.isEmpty()) {
            return String.join("\n", errors);
        }

        user.setUsername(newUsername);


        UserDataManager.updateUsername(oldUsername, user);
        UserDataManager.saveUser(user);
        return "your username changed";
    }

    public String changeNickname(String newNickname) {
        User user = App.getCurrentUser();

        if (user.getNickname().equals(newNickname)) {
            return "new nickname and your nickname are similar.";
        }

        List<String> errors = signupMenuController.getNicknameErrors(newNickname);
        if (!errors.isEmpty()) {
            return String.join("\n", errors);
        }

        user.setNickname(newNickname);
        UserDataManager.saveUser(user);
        return "your nickname changed";
    }

    public String changeEmail(String newEmail) {
        User user = App.getCurrentUser();

        if (user.getEmail().equals(newEmail)) {
            return "new email and your email are similar.";
        }

        List<String> errors = signupMenuController.getEmailErrors(newEmail);
        if (!errors.isEmpty()) {
            return String.join("\n", errors);
        }

        user.setEmail(newEmail);
        UserDataManager.saveUser(user);
        return "your email changed";
    }

    public String changePassword(String password, String newPassword) {
        User user = App.getCurrentUser();

        String hashPassword = PasswordHasher.hashSHA256(password);
        String hashNewPass = PasswordHasher.hashSHA256(newPassword);

        if (!user.getHashPassword().equals(hashPassword)) {
            return "your password is incorrect.";
        }

        if (user.getHashPassword().equals(hashNewPass)) {
            return "new pass and your pass are similar.";
        }

        List<String> errors = signupMenuController.getPasswordErrors(newPassword, newPassword);
        if (!errors.isEmpty()) {
            return String.join("\n", errors);
        }

        user.setHashPassword(hashNewPass);
        UserDataManager.saveUser(user);
        return "your pass changed.";
    }

    public String showInfo() {
        User user = App.getCurrentUser();
        return "Username: " + user.getUsername() + "\n" +
                "Nickname: " + user.getNickname() + "\n" +
                "Games played: " + user.getGamesPlayed() + "\n" +
                "Coins: " + user.getCoins() + "\n" +
                "Gems: " + user.getGems() + "\n" +
                "Levels completed: " + user.getCompletedLevels() + "\n" +
                "Mu point: " + user.getMaxMupoint();
    }

    public void showCurrentMenu() {
        GameMenuView.getInstance().showResult("Current menu: profile menu");
    }

}
