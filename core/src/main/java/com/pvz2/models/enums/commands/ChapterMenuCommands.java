package com.pvz2.models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ChapterMenuCommands {
    MENU_ENTER("\\s*menu\\s+enter\\s+collection\\s+menu\\s*"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("\\s*menu\\s+exit\\s*"),
    MENU_GREENHOUSE("\\s*menu\\s+greenhouse\\s*"),
    MENU_TRAVEL_LOG("\\s*menu\\s+travel\\s+log\\s*"),
    MENU_LEADERBOARD("\\s*menu\\s+leaderboard\\s*"),
    MENU_COIN_WALLET("\\s*menu\\s+coin\\s+wallet\\s*"),
    MENU_GEM_WALLET("\\s*menu\\s+gem\\s+wallet\\s*"),
    MENU_CHEAT_ADD("\\s*menu\\s+cheat\\s+add\\s+(?<amount>\\d+)\\s+(?<type>coin|diamond)\\s*"),
    CHOOSE_CHAPTER("^choose\\s+adventure\\s+(\\S+)$");


    private final Pattern compiledPattern;

    ChapterMenuCommands(String pattern) {
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
