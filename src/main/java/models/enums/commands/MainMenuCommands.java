package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands {
    MENU_ENTER("^menu\\s+enter\\s+(.+)$"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_LOGOUT("^menu\\s+logout$");

    private final String pattern;
    private final Pattern compiledPattern;

    MainMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
