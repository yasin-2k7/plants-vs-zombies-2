package models.enums;

import models.core.User;

import java.util.Comparator;

public enum LeaderboardSortField {
    LAST_STAGE(Comparator.comparingInt(User::getCompletedMainLevels)),
    MINI_GAMES(Comparator.comparingInt(user -> user.getMiniGameLevels().size())),
    DAILY_QUESTS(Comparator.comparingInt(User::getDailyQuestsCount)),
    NORMAL_QUESTS(Comparator.comparingInt(User::getNormalQuestsCount)),
    HIGH_SCORE(Comparator.comparingInt(User::getMaxMupoint));

    private final Comparator<User> comparator;

    LeaderboardSortField(Comparator<User> comparator) {
        this.comparator = comparator;
    }

    public Comparator<User> getComparator() {
        return comparator;
    }
}
