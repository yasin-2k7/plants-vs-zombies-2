package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TravelLogCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    TRAVEL_LOG_PAGE("");

    private final String pattern;
    private final Pattern compiledPattern;

    TravelLogCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

}
