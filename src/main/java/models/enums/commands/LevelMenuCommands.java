package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LevelMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    SHOW_ALL_PLANTS(""),
    SHOW_AVAILABLE_PLANTS(""),
    ADD_PLANT(""),
    REMOVE_PLANT(""),
    BOOST_PLANT(""),
    START_GAME("");

    private final String pattern;
    private final Pattern compiledPattern;

    LevelMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
