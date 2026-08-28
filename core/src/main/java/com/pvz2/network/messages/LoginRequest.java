package com.pvz2.network.messages;

public class LoginRequest {
    public String username;
    public String password;

    public LoginRequest() {} // Gson needs a no-arg constructor to deserialize into

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
