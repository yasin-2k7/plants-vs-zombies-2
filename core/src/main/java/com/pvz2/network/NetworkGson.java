package com.pvz2.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pvz2.models.core.LocalDateAdapter;

import java.time.LocalDate;

public class NetworkGson {
    public static final Gson INSTANCE = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getDeclaredClass() == java.util.Random.class;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz == java.util.Random.class;
            }
        })
        .create();
}
