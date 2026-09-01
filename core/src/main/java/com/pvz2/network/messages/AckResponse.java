package com.pvz2.network.messages;
public class AckResponse {
    public boolean success;
    public String message;
    public AckResponse() {}
    public AckResponse(boolean success) { this.success = success; }
}
