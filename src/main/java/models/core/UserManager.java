package models.core;

import java.util.HashMap;

public class UserManager {
    private HashMap<String, User> users;
    private User currentUser;

    public UserManager(){}

    public String register(String username,
                           String password,
                           String nickname,
                           String email,
                           String gender,
                           String securityQ,
                           String securityA){
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setHashPassword(PasswordHasher.hashSHA256(password));
        newUser.setNickname(nickname);
        newUser.setEmail(email);
        newUser.setGender(gender);
        newUser.setSecurityQ(securityQ);
        newUser.setSecurityA(PasswordHasher.hashSHA256(securityA));

        boolean isSaved = UserDataManager.saveUser(newUser);
        if (isSaved) {
            return "Register successfully. Redirecting to Login Menu...";
        } else {
            return "Error: Could not save user data to disk.";

        }

    }

    public String login(String username, String password){
        return "login";
    }

    public void logout(){}

    public User getCurrentUser(){
        return currentUser;
    }
}
