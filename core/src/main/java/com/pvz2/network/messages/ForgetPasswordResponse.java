package com.pvz2.network.messages;
public class ForgetPasswordResponse {
    public boolean success;
    public String message;      // shown either way — the "Please answer: <question>" text, or an error
    public String securityQ;    // only set on success, so the client can display it
}
