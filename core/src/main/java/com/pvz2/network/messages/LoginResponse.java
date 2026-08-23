package com.pvz2.network.messages;

import com.pvz2.models.core.User;

public class LoginResponse {
    public boolean success;
    public User user;         // null if login failed; hashPassword/securityA are nulled before sending
    public String token;      // null if login failed
    public String errorMessage;
    public LoginResponse() {}
}
