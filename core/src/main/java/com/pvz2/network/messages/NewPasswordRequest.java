package com.pvz2.network.messages;
public class NewPasswordRequest {
    public String username;
    public String newPassword;
    public NewPasswordRequest() {}
    public NewPasswordRequest(String username, String newPassword)
    { this.username = username; this.newPassword = newPassword; }
}
