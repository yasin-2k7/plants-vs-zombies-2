package models.core;

import java.util.HashMap;

public class UserManager {
    private HashMap<String, User> users;
    private User currentUser;

    public UserManager(){}

    public String register(String username, String password){
        return "register";
    }

    public String login(String username, String password){
        return "login";
    }

    public void logout(){}

    public User getCurrentUser(){
        return currentUser;
    }
}
