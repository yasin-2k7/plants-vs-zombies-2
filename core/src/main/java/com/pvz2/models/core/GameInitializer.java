package com.pvz2.models.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pvz2.models.plant.card.PlantCardFactory;
import java.io.Reader;

public class GameInitializer {
    public static void loadPlantUpgrades() {
        try {
            FileHandle file = Gdx.files.internal("data/upgradeRules.json");

            if (!file.exists()) {
                throw new IllegalStateException("Upgrades file not found in assets!");
            }

            try (Reader reader = file.reader("UTF-8")) {
                PlantCardFactory.init(reader);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
