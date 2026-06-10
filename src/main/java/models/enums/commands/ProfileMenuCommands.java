package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    MENU_PROFILE_CHANGE_USERNAME(""),
    MENU_PROFILE_CHANGE_NICKNAME(""),
    MENU_PROFILE_CHANGE_EMAIL(""),
    MENU_PROFILE_CHANGE_PASSWORD(""),
    MENU_PROFILE_SHOW_INFO("");

    private final String pattern;
    private final Pattern compiledPattern;

    ProfileMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
