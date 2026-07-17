package models.quest.reward;

import models.core.User;
import models.enums.PlantType;

public class UnlockableReward implements Reward {
    private Object target;
    private UnlockableType type;

    public UnlockableReward(Object target, UnlockableType type) {
        this.target = target;
        this.type = type;
    }

    @Override
    public void apply(User user) {
        if (type == UnlockableType.LEVEL && target instanceof Integer) {
            user.advanceLevel();
            user.notifyLevelUnlock(String.valueOf(target));
        } else if (type == UnlockableType.CHAPTER && target instanceof Integer) {
        } else if (type == UnlockableType.PLANT && target instanceof PlantType) {
            PlantType plantType = (PlantType) target;
            user.unlockPlant(plantType);
            user.notifyPlantUnlock(plantType.name());
        }
    }
}