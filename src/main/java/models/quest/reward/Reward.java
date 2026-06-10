package models.quest.reward;

import models.core.User;

public interface Reward {
    void apply(User user);
}
