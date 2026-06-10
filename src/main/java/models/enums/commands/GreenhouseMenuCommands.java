package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GreenhouseMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    SHOW_GREENHOUSE(""),
    PLANT_POT(""),
    COLLECT(""),
    GROW(""),
    ENTER_SHOP("");

    private final String pattern;
    private final Pattern compiledPattern;

    GreenhouseMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
