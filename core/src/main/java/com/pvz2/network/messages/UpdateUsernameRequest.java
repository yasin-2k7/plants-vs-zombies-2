package com.pvz2.network.messages;
public class UpdateUsernameRequest {
    public String token;
    public String newUsername;
    public UpdateUsernameRequest() {}
    public UpdateUsernameRequest(String token, String newUsername) {
        this.token = token; this.newUsername = newUsername;
    }
}
