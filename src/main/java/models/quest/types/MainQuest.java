package models.quest.types;

import models.quest.Quest;
import models.quest.QuestPriority;
import models.quest.QuestStats;
import models.quest.reward.Reward;

import java.util.function.Predicate;

public class MainQuest extends Quest {
    public MainQuest(String id, String description, Predicate<QuestStats> condition, Reward reward) {
        this(id, description, condition, reward, false);
    }

    public MainQuest(String id, String description, Predicate<QuestStats> condition,
                     Reward reward, boolean endGameDependent) {
        super(id, description, QuestPriority.CRITICAL, condition, reward, endGameDependent);
    }
}