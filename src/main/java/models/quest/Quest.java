package models.quest;

import models.core.User;
import models.quest.reward.Reward;

import java.util.Queue;
import java.util.function.Predicate;

public abstract class Quest implements Comparable<Quest>{
    private String id;
    private String description;
    private QuestPriority priority;
    private boolean isCompleted;
    private Reward reward;
    private Predicate<QuestStats> condition;

    public Quest(){}

    public void complete(User user){}

    public boolean checkCompletion(QuestStats stats){
        return false;
    }

    @Override
    public int compareTo(Quest other){
        return 0;
    }


}
