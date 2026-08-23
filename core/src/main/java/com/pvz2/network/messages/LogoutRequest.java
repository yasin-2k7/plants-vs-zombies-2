package com.pvz2.network.messages;
public class LogoutRequest {
    public String token;
    public LogoutRequest() {}
    public LogoutRequest(String token) { this.token = token; }
}
