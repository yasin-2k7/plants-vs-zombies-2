package controller;

import models.core.App;
import models.core.PasswordHasher;
import models.core.User;
import models.core.UserDataManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileMenuController implements MenuController{
    private SignupMenuController signupMenuController;
    @Override
    public void changeMenu() {

    }

    public String changeUsername(String newUsername){
        User user = App.getCurrentUser();

        if(user.getUsername().equals(newUsername)){
            return "new username and your username are similar.";
        }

        List<String> errors;
        errors = signupMenuController.getUsernameErrors(newUsername);
        if(!errors.isEmpty()){
            for(String error : errors){
                return error;
            }
        }

        user.setUsername(newUsername);
        return "your username changed";
    }

    public String changeNickname(String newNickname){
        User user = App.getCurrentUser();

        if(user.getNickname().equals(newNickname)){
            return "new nickname and your nickname are similar.";
        }

        List<String> errors;
        errors = signupMenuController.getNicknameErrors(newNickname);
        if(!errors.isEmpty()){
            for(String error : errors){
                return error;
            }
        }

        user.setNickname(newNickname);
        return "your nickname changed";
    }

    public String changeEmail(String newEmail){
        User user = App.getCurrentUser();

        if(user.getEmail().equals(newEmail)){
            return "new email and your email are similar.";
        }

        List<String> errors;
        errors = signupMenuController.getEmailErrors(newEmail);
        if(!errors.isEmpty()){
            for(String error : errors){
                return error;
            }
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

        
    }

}
