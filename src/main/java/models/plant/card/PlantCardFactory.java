package models.plant.card;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.enums.PlantType;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

public class PlantCardFactory {
    private static Map<String, Map<Integer, UpgradeConfig>> upgradeRules;

    public static void init(String jsonContent) {
        // upgradeRules = gson.fromJson(...);
    }

    public static void init(Reader reader) {
        Gson gson = new Gson();
        Type typeOfHashMap = new TypeToken<Map<String, Map<Integer, UpgradeConfig>>>(){}.getType();
        upgradeRules = gson.fromJson(reader, typeOfHashMap);
    }

    public static PlantCard createCard(PlantType type, int userLevel) {
        PlantCard card = new PlantCard(type, type.baseSunCost, type.baseCoolDown);

        if (upgradeRules != null && upgradeRules.containsKey(type.name())) {
            UpgradeConfig config = upgradeRules.get(type.name()).get(userLevel);

            if (config != null) {
                card.setSunCost(card.getSunCost() + config.getSunCostModifier());
                card.setMaxCooldownTicks(card.getMaxCooldownTicks() - config.getCooldownReductionTicks());
            }
        }

        return card;
    }
}
