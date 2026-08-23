package com.pvz2.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NetworkGson {
    public static final Gson INSTANCE = new GsonBuilder().create(); // compact — no pretty-printing, must stay one line per message
}
