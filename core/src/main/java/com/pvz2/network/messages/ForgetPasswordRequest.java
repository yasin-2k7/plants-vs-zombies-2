package com.pvz2.network.messages;
public class ForgetPasswordRequest {
    public String username, email;
    public ForgetPasswordRequest() {}
    public ForgetPasswordRequest(String username, String email) { this.username = username; this.email = email; }
}
