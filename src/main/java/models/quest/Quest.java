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

    public Quest(String id, String description, QuestPriority priority, Predicate<QuestStats> condition, Reward reward) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.condition = condition;
        this.reward = reward;
        this.isCompleted = false;
    }

    public void complete(User user) {
        if (this.isCompleted && this.reward != null) {
            this.reward.apply(user);
        }
    }

    public boolean checkCompletion(QuestStats stats) {
        if (!isCompleted && condition != null && condition.test(stats)) {
            this.isCompleted = true;
        }
        return this.isCompleted;
    }

    @Override
    public int compareTo(Quest other) {
        return this.priority.compareTo(other.priority);
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return isCompleted; }
    public QuestPriority getPriority() {return priority;}
    public void setCompleted(boolean completed) {this.isCompleted = completed;}
}