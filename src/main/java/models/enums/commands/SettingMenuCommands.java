package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SettingMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT("\\s*menu\\s+show\\s+current\\s*"),
    MENU_EXIT("\\s*menu\\s+exit\\s*"),
    MENU_SETTINGS_CHANGE_DIFFICULTY("\\s*menu\\s+settings\\s+change-difficulty\\s+-l\\s+([1-5])");

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
