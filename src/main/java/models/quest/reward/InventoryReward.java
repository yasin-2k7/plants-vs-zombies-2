package models.quest.reward;

import models.core.User;
import models.plant.Plant;

public class InventoryReward implements Reward{
    private Class<? extends Plant> plantType;
    private int quantity;

    public InventoryReward(){}

    @Override
    public void apply(User user) {
    }
}
