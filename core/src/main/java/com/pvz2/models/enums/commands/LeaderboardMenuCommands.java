package com.pvz2.models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LeaderboardMenuCommands {
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    SORT_LEADERBOARD("\\s*leaderboard\\s*-field\\s+(?<field>\\S+)\\s+-order\\s+(?<order>ascending|descending)\\s*"),
    MENU_EXIT("\\s*menu\\s+exit\\s*");

    private final Pattern compiledPattern;

    LeaderboardMenuCommands(String pattern) {
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
