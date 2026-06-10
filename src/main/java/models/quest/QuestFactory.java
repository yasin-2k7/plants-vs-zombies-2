package models.quest;

import models.quest.reward.Reward;
import models.quest.types.DailyQuest;
import models.quest.types.EpicChallengeQuest;
import models.quest.types.MainQuest;

import java.util.function.Predicate;

public class QuestFactory {

    public static DailyQuest createDailyQuest(String id, String description,
                                              Predicate<QuestStats> condition,
                                              Reward reward) {
        return new DailyQuest();
    }

    public static MainQuest createMainQuest(String id, String description,
                                            Predicate<QuestStats> condition,
                                            Reward reward) {
        return new MainQuest();
    }

    public static EpicChallengeQuest createEpicQuest(String id, String description,
                                                     Predicate<QuestStats> condition,
                                                     Reward reward) {
        return new EpicChallengeQuest();
    }
}
