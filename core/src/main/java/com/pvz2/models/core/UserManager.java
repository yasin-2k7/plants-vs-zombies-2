package com.pvz2.models.core;

import com.pvz2.network.NetworkClient;
import com.pvz2.network.NetworkMessage;
import com.pvz2.network.messages.*;

public class UserManager {
    private static User currentUser;

    public static boolean loadInitialUser() {
        String token = UserDataManager.getSessionToken();
        if (token == null) return false;

        try {
            AutoLoginRequest req = new AutoLoginRequest(token);
            NetworkMessage reply = NetworkClient.get().sendRequest("AUTO_LOGIN", req, 5000);
            LoginResponse resp = NetworkClient.get().parsePayload(reply, LoginResponse.class);

            if (resp.success) {
                applyLoggedInUser(resp.user, resp.token);
                return true;
            } else {
                UserDataManager.clearSessionToken(); // stale/expired token — don't keep retrying it
                return false;
            }
        } catch (InterruptedException e) {
            return false; // server unreachable — fall through to login screen
        }
    }

    public static String register(String username, String password, String nickname, String email,
                                  String gender, String securityQ, String securityA) {
        try {
            RegisterRequest req = new RegisterRequest(username, password, nickname, email, gender, securityQ, securityA);
            NetworkMessage reply = NetworkClient.get().sendRequest("REGISTER", req, 5000);
            RegisterResponse resp = NetworkClient.get().parsePayload(reply, RegisterResponse.class);
            return resp.message;
        } catch (InterruptedException e) {
            return "Error: could not reach server.";
        }
    }

    public static String login(String username, String password, boolean stayLoggedIn) {
        try {
            LoginRequest req = new LoginRequest(username, password);
            NetworkMessage reply = NetworkClient.get().sendRequest("LOGIN", req, 5000);
            LoginResponse resp = NetworkClient.get().parsePayload(reply, LoginResponse.class);

            if (resp.success) {
                applyLoggedInUser(resp.user, resp.token);
                if (stayLoggedIn) {
                    UserDataManager.saveSessionToken(resp.token);
                }
                return "Login successful! Welcome " + currentUser.getNickname();
            } else {
                return resp.errorMessage;
            }
        } catch (InterruptedException e) {
            return "Error: could not reach server.";
        }
    }

    private static void applyLoggedInUser(User user, String token) {
        user.afterLoad();
        currentUser = user;
        App.setCurrentUser(user);
        user.initQuests();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        if (currentUser != null) {
            String token = UserDataManager.getSessionToken();
            if (token != null) {
                try {
                    NetworkClient.get().sendRequest("LOGOUT", new LogoutRequest(token), 3000);
                } catch (InterruptedException ignored) {
                    // best-effort — clear local state regardless of whether the server ack arrives
                }
            }
            currentUser = null;
            App.setCurrentUser(null);
            UserDataManager.clearSessionToken();
        }
    }

    public static String changeUsername(String newUsername) {
        String token = UserDataManager.getSessionToken();
        if (token == null) return "You must be logged in to do this.";

        try {
            UpdateUsernameRequest req = new UpdateUsernameRequest(token, newUsername);
            NetworkMessage reply = NetworkClient.get().sendRequest("UPDATE_USERNAME", req, 5000);
            RegisterResponse resp = NetworkClient.get().parsePayload(reply, RegisterResponse.class);

            if (resp.success && currentUser != null) {
                currentUser.setUsername(newUsername); // keep the client's cached copy in sync
            }
            return resp.message;
        } catch (InterruptedException e) {
            return "Error: could not reach server.";
        }
    }
}
