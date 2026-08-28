package com.pvz2.network;

import com.google.gson.JsonElement;

public class NetworkMessage {
    public String type;
    public JsonElement payload;
    public String requestId;

    public NetworkMessage() {}

    public NetworkMessage(String type, JsonElement payload) {
        this.type = type;
        this.payload = payload;
    }
}
