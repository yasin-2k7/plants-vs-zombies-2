package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LevelMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("\\s*menu\\s+exit\\s*"),
    CHOOSE_LEVEL("^choose\\s+level\\s+([1-4])$"),
    SHOW_LEVELS("^show\\s+levels$");

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
