package models.quest.reward;

import models.core.User;

public class UnlockableReward implements Reward{
    private String targetId;
    private UnlockableType type;

    public UnlockableReward(){}

    @Override
    public void apply(User user) {
    }
}
