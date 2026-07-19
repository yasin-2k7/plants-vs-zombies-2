package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NewsMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("\\s*menu\\s+exit\\s*"),
    MENU_NEWS_SHOW_UNREAD("\\s*menu\\s+news\\s+show\\s+unread\\s*"),
    MENU_NEWS_SHOW_ALL("\\s*menu\\s+news\\s+show\\s+all\\s*");

    private final String pattern;
    private final Pattern compiledPattern;

    NewsMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
