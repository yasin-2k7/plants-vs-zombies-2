package models.core;

import java.util.HashMap;

public class UserManager {
    private static HashMap<String, User> users;
    private static User currentUser;

    public UserManager(){
        loadInitialUser();// **جدید**: در ابتدای برنامه، آخرین کاربر لاگین‌کرده را بارگذاری می‌کند
    }

    private void loadInitialUser() {
        String loggedInUsername = UserDataManager.getLoggedInUsername();
        if (loggedInUsername != null) {
            User user = UserDataManager.loadUser(loggedInUsername);
            if (user != null) {
                this.currentUser = user;
                App.setCurrentUser(user);
            }
        }
    }

    public static String register(String username,
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

    public static String login(String username, String password) {
        User user = UserDataManager.loadUser(username);
        if (user == null) {
            return "Error: Username not found.";
        }

        if (user.checkPassword(password)) {
            currentUser = user;
            App.setCurrentUser(user);
            UserDataManager.saveLoggedInUser(username);
            return "Login successful! Welcome " + user.getNickname();
        } else {
            return "Error: Incorrect password.";
        }
    }

    public static void logout() {
        if (currentUser != null) {
            UserDataManager.saveUser(currentUser); // ذخیره نهایی قبل از خروج
            currentUser = null;
            App.setCurrentUser(null);
            UserDataManager.clearLoggedInUser();
            System.out.println("You have been logged out.");
        }
    }
    public static User getCurrentUser(){
        return currentUser;
    }
}
