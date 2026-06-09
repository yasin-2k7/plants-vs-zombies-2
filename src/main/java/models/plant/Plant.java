package models.plant;

import models.enums.PlantType;

import java.util.ArrayList;

public class Plant {
    private PlantType type;
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

    public Plant(PlantType type) {
        this.type = type;
    }
}