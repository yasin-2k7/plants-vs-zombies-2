package com.pvz2.models.core;

import com.pvz2.models.plant.card.PlantCardFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class GameInitializer {
    public static void loadPlantUpgrades() {
        try (InputStream inputStream = GameInitializer.class
                .getClassLoader()
                .getResourceAsStream("upgradeRules.json")) {

            if (inputStream == null) {
                throw new IllegalStateException("Upgrades file not found in resources!");
            }

            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                PlantCardFactory.init(reader);
            }

        } catch (Exception e) {
            return;
        }
    }
}
