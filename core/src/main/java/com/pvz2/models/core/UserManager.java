package com.pvz2.models.core;




public class UserManager {
    private static User currentUser;


    public static boolean loadInitialUser() {
        String loggedInUsername = UserDataManager.getLoggedInUsername();
        boolean found = false;
        if (loggedInUsername != null) {
            User user = UserDataManager.loadUser(loggedInUsername);
            if (user != null) {
                currentUser = user;
                App.setCurrentUser(user);
                user.initQuests();
                found = true;
            }
        }
        return found;
    }

    public static String register(String username,
                                  String password,
                                  String nickname,
                                  String email,
                                  String gender,
                                  String securityQ,
                                  String securityA) {
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

    public static String login(String username, String password, boolean stayLoggedIn) {
        User user = UserDataManager.loadUser(username);
        if (user == null) {
            return "Error: Username not found.";
        }

        if (user.checkPassword(password)) {
            currentUser = user;
            App.setCurrentUser(user);
            user.initQuests();
            if (stayLoggedIn) {
                UserDataManager.saveLoggedInUser(username);
            }
            return "Login successful! Welcome " + user.getNickname();
        } else {
            return "Error: Incorrect password.";
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        if (currentUser != null) {
            UserDataManager.saveUser(currentUser); // ذخیره نهایی قبل از خروج
            currentUser = null;
            App.setCurrentUser(null);
            UserDataManager.clearLoggedInUser();
        }
    }
}
