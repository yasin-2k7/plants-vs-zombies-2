package com.pvz2.view;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class AtlasManager implements Disposable {
    private final Map<String, TextureRegion> regions = new HashMap<>();
    private final Map<String, Texture> atlasTextures = new HashMap<>();

    /**
     * @param resourcesJson e.g. Gdx.files.internal("RESOURCES.json")
     * @param atlasFolder   e.g. Gdx.files.internal("assets/ATLASES")
     * @param resFilter     "1536" or "768" — which resolution variant to load
     */
    public void load(FileHandle resourcesJson, FileHandle atlasFolder, String resFilter) {
        JsonValue root = new JsonReader().parse(resourcesJson);
        JsonValue groups = root.get("groups");

        // Pass 1: find atlas sheet definitions and load their PNGs
        for (JsonValue group : groups) {
            if (!"simple".equals(group.getString("type", ""))) continue;
            String res = group.getString("res", null);
            if (res != null && !res.equals(resFilter)) continue; // skip other resolutions, keep _Common (res==null)

            JsonValue resources = group.get("resources");
            if (resources == null) continue;

            for (JsonValue r : resources) {
                if (!"Image".equals(r.getString("type", ""))) continue;
                if (r.getBoolean("atlas", false)) {
                    String id = r.getString("id");
                    String path = r.getString("path").toUpperCase(); // e.g. atlases\AlwaysLoaded_1536_00
                    String fileName = path.substring(path.lastIndexOf('\\') + 1) + ".PNG";
                    FileHandle png = atlasFolder.child(fileName);
                    System.out.println("Looking for: " + png.path() + " exists=" + png.exists());
                    if (png.exists()) {
                        atlasTextures.put(id, new Texture(png));
                    }
                }
            }
        }
        System.out.println("Atlas textures loaded: " + atlasTextures.size());

        // Pass 2: build TextureRegions for every sprite that has a parent atlas
        for (JsonValue group : groups) {
            if (!"simple".equals(group.getString("type", ""))) continue;
            String res = group.getString("res", null);
            if (res != null && !res.equals(resFilter)) continue;

            JsonValue resources = group.get("resources");
            if (resources == null) continue;

            for (JsonValue r : resources) {
                if (!"Image".equals(r.getString("type", ""))) continue;
                if (r.getBoolean("atlas", false)) continue; // already handled above
                String parent = r.getString("parent", null);
                if (parent == null) continue;

                Texture tex = atlasTextures.get(parent);
                if (tex == null) continue;

                int ax = r.getInt("ax"), ay = r.getInt("ay");
                int aw = r.getInt("aw"), ah = r.getInt("ah");
                regions.put(r.getString("id"), new TextureRegion(tex, ax, ay, aw, ah));
            }
        }
    }

    public TextureRegion get(String id) {
        return regions.get(id);
    }

    @Override
    public void dispose() {
        for (Texture t : atlasTextures.values()) t.dispose();
    }
}
