package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ChapterMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT(""),
    MENU_ENTER_CHAPTER(""),
    MENU_GREENHOUSE(""),
    MENU_TRAVEL_LOG(""),
    MENU_LEADERBOARD(""),
    MENU_COIN_WALLET(""),
    MENU_GEM_WALLET(""),
    MENU_CHEAT_ADD(""),
    CHOOSE_CHAPTER("^choose\\s+adventure\\s+(\\S+)$");


    private final String pattern;
    private final Pattern compiledPattern;

    ChapterMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
