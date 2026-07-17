package models.quest.types;

import models.quest.Quest;
import models.quest.QuestPriority;
import models.quest.QuestStats;
import models.quest.reward.Reward;
import java.util.function.Predicate;

public class MainQuest extends Quest {
    public MainQuest(String id, String description, Predicate<QuestStats> condition, Reward reward) {
        super(id, description, QuestPriority.CRITICAL, condition, reward);
    }
}