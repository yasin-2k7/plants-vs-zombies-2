package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("\\s*menu\\s+exit\\s*"),
    MENU_PROFILE_CHANGE_USERNAME("\\s*menu\\s+profile\\s+change-username\\s+-u\\s+(\\S+)\\s*"),
    MENU_PROFILE_CHANGE_NICKNAME("\\s*menu\\s+profile\\s+change-nickname\\s+-n\\s+(\\S+)\\s*"),
    MENU_PROFILE_CHANGE_EMAIL("\\s*menu\\s+profile\\s+change-email\\s+-e\\s+(\\S+)\\s*"),
    MENU_PROFILE_CHANGE_PASSWORD("\\s*menu\\s+profile\\s+change-password\\s+-p\\s+(\\S+)\\s+-o\\s+(\\S+)\\s*"),
    MENU_PROFILE_SHOW_INFO("\\s*menu\\s+profile\\s+show-info\\s*");

    private final Pattern compiledPattern;

    ProfileMenuCommands(String pattern) {
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
