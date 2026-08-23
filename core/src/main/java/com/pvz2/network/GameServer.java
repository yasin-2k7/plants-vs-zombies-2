package com.pvz2.network;

import com.google.gson.Gson;
import com.pvz2.models.core.PasswordHasher;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserDataManager;
import com.pvz2.network.messages.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer {
    private static final int PORT = 5000;
    private static final Gson GSON = NetworkGson.INSTANCE;

    private final Map<String, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    // in-memory only — a server restart invalidates every existing token, forcing re-login.
    // Fine for a course project; a persistent session store would be the real-world fix.
    private final Map<String, String> sessionTokens = new ConcurrentHashMap<>(); // token -> username

    public static void main(String[] args) {
        new GameServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection: " + clientSocket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(clientSocket, this);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dispatch(ClientHandler sender, NetworkMessage msg) {
        switch (msg.type) {
            case "LOGIN" -> handleLogin(sender, msg);
            case "REGISTER" -> handleRegister(sender, msg);
            case "AUTO_LOGIN" -> handleAutoLogin(sender, msg);
            case "LOGOUT" -> handleLogout(sender, msg);
            case "UPDATE_USERNAME" -> handleUpdateUsername(sender, msg);
            default -> System.err.println("Unknown message type: " + msg.type);
        }
    }

    private void handleLogin(ClientHandler sender, NetworkMessage msg) {
        LoginRequest req = GSON.fromJson(msg.payload, LoginRequest.class);
        User user = UserDataManager.loadUser(req.username);
        boolean success = user != null && user.checkPassword(req.password);

        LoginResponse response = new LoginResponse();
        if (success) {
            user.setHashPassword(null);
            user.setSecurityA(null);

            String token = UUID.randomUUID().toString();
            sessionTokens.put(token, req.username);
            sender.setUsername(req.username);
            registerOnline(req.username, sender);

            response.success = true;
            response.user = user;
            response.token = token;
        } else {
            response.success = false;
            response.errorMessage = "Invalid username or password.";
        }

        sender.send("LOGIN", msg.requestId, response);
    }

    private void handleRegister(ClientHandler sender, NetworkMessage msg) {
        RegisterRequest req = GSON.fromJson(msg.payload, RegisterRequest.class);

        if (UserDataManager.userExists(req.username)) {
            sender.send("REGISTER", msg.requestId, response(false, "Username already exists."));
            return;
        }

        User newUser = new User();
        newUser.setUsername(req.username);
        newUser.setHashPassword(PasswordHasher.hashSHA256(req.password));
        newUser.setNickname(req.nickname);
        newUser.setEmail(req.email);
        newUser.setGender(req.gender);
        newUser.setSecurityQ(req.securityQ);
        newUser.setSecurityA(PasswordHasher.hashSHA256(req.securityA));

        boolean saved = UserDataManager.saveUser(newUser);
        sender.send("REGISTER", msg.requestId, response(saved,
            saved ? "Register successfully. Redirecting to Login Menu..." : "Error: could not save user data."));
    }

    private RegisterResponse response(boolean success, String message) {
        RegisterResponse r = new RegisterResponse();
        r.success = success;
        r.message = message;
        return r;
    }

    private void handleUpdateUsername(ClientHandler sender, NetworkMessage msg) {
        UpdateUsernameRequest req = GSON.fromJson(msg.payload, UpdateUsernameRequest.class);
        RegisterResponse response = new RegisterResponse(); // reusing success/message shape — fine for a simple ack

        String oldUsername = sessionTokens.get(req.token);
        if (oldUsername == null) {
            response.success = false;
            response.message = "Session expired. Please log in again.";
            sender.send("UPDATE_USERNAME", msg.requestId, response);
            return;
        }

        if (UserDataManager.userExists(req.newUsername)) {
            response.success = false;
            response.message = "That username is already taken.";
            sender.send("UPDATE_USERNAME", msg.requestId, response);
            return;
        }

        User user = UserDataManager.loadUser(oldUsername);
        if (user == null) {
            response.success = false;
            response.message = "User not found.";
            sender.send("UPDATE_USERNAME", msg.requestId, response);
            return;
        }

        user.setUsername(req.newUsername);
        boolean saved = UserDataManager.updateUsername(oldUsername, user);

        if (saved) {
            sessionTokens.put(req.token, req.newUsername);
            onlineUsers.remove(oldUsername);
            onlineUsers.put(req.newUsername, sender);
            sender.setUsername(req.newUsername);
        }

        response.success = saved;
        response.message = saved ? "Username updated successfully." : "Could not save the new username.";
        sender.send("UPDATE_USERNAME", msg.requestId, response);
    }

    private void handleAutoLogin(ClientHandler sender, NetworkMessage msg) {
        AutoLoginRequest req = GSON.fromJson(msg.payload, AutoLoginRequest.class);
        String username = sessionTokens.get(req.token);

        LoginResponse response = new LoginResponse();
        if (username == null) {
            response.success = false;
            response.errorMessage = "Session expired. Please log in again.";
        } else {
            User user = UserDataManager.loadUser(username);
            if (user == null) {
                sessionTokens.remove(req.token);
                response.success = false;
                response.errorMessage = "User no longer exists.";
            } else {
                user.setHashPassword(null);
                user.setSecurityA(null);

                sender.setUsername(username);
                registerOnline(username, sender);

                response.success = true;
                response.user = user;
                response.token = req.token;
            }
        }
        sender.send("AUTO_LOGIN", msg.requestId, response);
    }

    private void handleLogout(ClientHandler sender, NetworkMessage msg) {
        LogoutRequest req = GSON.fromJson(msg.payload, LogoutRequest.class);
        sessionTokens.remove(req.token);
        if (sender.getUsername() != null) {
            onlineUsers.remove(sender.getUsername());
            sender.setUsername(null);
        }
        sender.send("LOGOUT", msg.requestId, new AckResponse(true));
    }

    public void registerOnline(String username, ClientHandler handler) {
        onlineUsers.put(username, handler);
    }

    public void onDisconnect(ClientHandler handler) {
        if (handler.getUsername() != null) {
            onlineUsers.remove(handler.getUsername());
        }
    }

    public ClientHandler getOnlineUser(String username) {
        return onlineUsers.get(username);
    }
}
