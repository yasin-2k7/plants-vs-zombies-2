package models.plant;

import java.util.ArrayList;

public class Plant {
    private String name;
    private int health;
    private ArrayList<GameComponent> components = new ArrayList<>();

    public void addComponent(GameComponent comp) {
        components.add(comp);
    }

    public void update() {
        // تک‌تک اجزا کار خودشان را انجام می‌دهند
        for (GameComponent comp : components) {
            comp.update(this);
        }
    }
}