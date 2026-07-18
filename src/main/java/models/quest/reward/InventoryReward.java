package models.quest.reward;

import models.core.User;
import models.enums.PlantType;

public class InventoryReward implements Reward {
    private PlantType plantType;
    private int quantity;

    public InventoryReward(PlantType plantType, int quantity) {
        this.plantType = plantType;
        this.quantity = quantity;
    }

    @Override
    public void apply(User user) {
        user.addSeedPackets(plantType, quantity);
    }
}