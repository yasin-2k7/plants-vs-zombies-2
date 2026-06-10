package models.quest.types;

import models.quest.Quest;

import java.time.LocalDate;

public class DailyQuest extends Quest {
    private LocalDate questDate;

    public DailyQuest(){}

    public boolean isAvailableToday(){
        return false;
    }
}
