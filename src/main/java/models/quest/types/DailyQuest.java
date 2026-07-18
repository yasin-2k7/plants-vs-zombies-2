package models.quest.types;

import models.quest.Quest;
import models.quest.QuestPriority;
import models.quest.QuestStats;
import models.quest.reward.Reward;
import java.time.LocalDate;
import java.util.function.Predicate;

public class DailyQuest extends Quest {
    private LocalDate questDate;

    public DailyQuest(String id, String description, QuestPriority priority, Predicate<QuestStats> condition, Reward reward) {
        super(id, description, priority, condition, reward);
        this.questDate = LocalDate.now();
    }

    public boolean isAvailableToday() {
        return LocalDate.now().equals(questDate) && !isCompleted();
    }
}