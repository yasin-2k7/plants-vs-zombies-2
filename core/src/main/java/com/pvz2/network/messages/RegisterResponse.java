package com.pvz2.network.messages;

public class RegisterResponse {
    public boolean success;
    public String message; // human-readable result, shown either way — matches UserManager.register's existing String return
    public RegisterResponse() {}
}
