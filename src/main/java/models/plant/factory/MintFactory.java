package models.plant.factory;

import models.core.App;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.card.PlantCard;
import models.plant.components.MintComponent;

import java.util.Map;
import java.util.function.Supplier;

public class MintFactory {
    static void register(Map<PlantType, Supplier<Plant>> registry) {
        registry.put(PlantType.ENLIGHTEN_MINT, () -> buildMint(PlantType.ENLIGHTEN_MINT));
        registry.put(PlantType.APPEASE_MINT, () -> buildMint(PlantType.APPEASE_MINT));
        registry.put(PlantType.ARMA_MINT, () -> buildMint(PlantType.ARMA_MINT));
        registry.put(PlantType.BOMBARD_MINT, () -> buildMint(PlantType.BOMBARD_MINT));
        registry.put(PlantType.ENFORCE_MINT, () -> buildMint(PlantType.ENFORCE_MINT));
        registry.put(PlantType.REINFORCE_MINT, () -> buildMint(PlantType.REINFORCE_MINT));
        registry.put(PlantType.ENCHANT_MINT, () -> buildMint(PlantType.ENCHANT_MINT));
        registry.put(PlantType.PIERCE_MINT, () -> buildMint(PlantType.PIERCE_MINT));
        registry.put(PlantType.CAT_TAIL_MINT, () -> buildMint(PlantType.CAT_TAIL_MINT));

    }

    private static Plant buildMint(PlantType type) {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(type);
        boolean resetCooldown = level >= 4;
        Plant p = new Plant(type, 300, 0);
        p.addComponent(new MintComponent(type, _ -> {
            for (Plant plant : App.getCurrentGame().getActivePlants()) {
                if (plant.getType().family == type.family) {
                    plant.activatePlantFood();
                }
            }
            if (resetCooldown) {
                for (PlantCard card : App.getCurrentGame().getPlantLists()) {
                    card.reset();
                }
            }
        }));
        return p;
    }
}
