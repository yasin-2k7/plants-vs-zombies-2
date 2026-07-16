package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GreenhouseMenuCommands {
    MENU_ENTER("^menu\\s+enter\\s+greenhouse$"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("^menu\\s+exit$"),
    SHOW_GREENHOUSE("^show\\s+greenhouse$"),
    PLANT_POT("^plant\\s+pot\\s+at\\s+\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)$"),
    COLLECT("^collect\\s+\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)$"),
    GROW("^grow\\s+\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)$"),
    ENTER_SHOP("^enter\\s+shop$");

    private final String pattern;
    private final Pattern compiledPattern;

    GreenhouseMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

    public String getPattern() {
        return pattern;
    }
}