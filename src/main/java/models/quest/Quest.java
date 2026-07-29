package models.quest;

import models.core.User;
import models.quest.reward.Reward;

import java.util.function.Predicate;

public abstract class Quest implements Comparable<Quest> {
    private String id;
    private String description;
    private QuestPriority priority;
    private boolean isCompleted;
    private Reward reward;
    private transient Predicate<QuestStats> condition;
    private boolean endGameDependent;

    public Quest(String id, String description, QuestPriority priority,
                 Predicate<QuestStats> condition, Reward reward, boolean endGameDependent) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.condition = condition;
        this.reward = reward;
        this.isCompleted = false;
        this.endGameDependent = endGameDependent;
    }

    public boolean isEndGameDependent() {
        return endGameDependent;
    }

    public void complete(User user) {
        if (!this.isCompleted && this.reward != null) {
            this.reward.apply(user);
            this.isCompleted = true;
        }
    }

    public boolean checkCompletion(QuestStats stats) {
        if (!isCompleted && condition != null && condition.test(stats)) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Quest other) {
        return this.priority.compareTo(other.priority);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public QuestPriority getPriority() {
        return priority;
    }

}