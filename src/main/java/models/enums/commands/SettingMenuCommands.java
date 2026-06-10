package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SettingMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    MENU_SETTINGS_CHANGE_DIFFICULTY("");

    private final String pattern;
    private final Pattern compiledPattern;

    SettingMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
