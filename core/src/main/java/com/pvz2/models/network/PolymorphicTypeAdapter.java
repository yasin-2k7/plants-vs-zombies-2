package com.pvz2.models.network;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Generic (de)serializer for an abstract/interface base type whose concrete subclasses
 * aren't known ahead of time by Gson's reflection — e.g. Zombie, and likely Obstacle or
 * Projectile if those are structured the same way (abstract base + one subclass per kind).
 *
 * Gson's default adapters serialize fine using an object's runtime class, but on the way
 * back in they only know a field's DECLARED type (List<Zombie>) and always try to build
 * that exact type — which fails immediately for an abstract class. This writes the actual
 * class name alongside the object's data, and uses it to reconstruct the right subclass.
 *
 * Register once per abstract base type in NetworkGson:
 *   .registerTypeAdapter(Zombie.class, new PolymorphicTypeAdapter<Zombie>())
 * No per-subclass registration needed — Gson resolves this by declared element type, so
 * one registration covers every subclass appearing anywhere as a Zombie-typed field/list.
 */
public class PolymorphicTypeAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    private static final String TYPE_FIELD = "@type";
    private static final String DATA_FIELD = "data";

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        Class<?> actualClass = src.getClass();
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty(TYPE_FIELD, actualClass.getName());
        wrapper.add(DATA_FIELD, context.serialize(src, actualClass));
        return wrapper;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject wrapper = json.getAsJsonObject();
        String className = wrapper.get(TYPE_FIELD).getAsString();
        try {
            Class<?> actualClass = Class.forName(className);
            return context.deserialize(wrapper.get(DATA_FIELD), actualClass);
        } catch (ClassNotFoundException e) {
            // Client and server share the same models classes throughout this project, so this
            // should never actually happen — but fail loudly rather than silently dropping data.
            throw new JsonParseException("Unknown subclass while deserializing: " + className, e);
        }
    }
}
