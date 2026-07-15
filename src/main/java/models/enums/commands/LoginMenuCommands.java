package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuCommands {
    MENU_ENTER("^menu\\s+enter\\s+main\\s+menu$"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("^menu\\s+exit$"),
    LOGIN("^login\\s+-u\\s+(\\S+)\\s+-p\\s+(\\S+)(\\s+-stay-logged-in)?$"),
    FORGET_PASSWORD("^forget\\s+password\\s+-u\\s+(\\S+)\\s+-e\\s+(\\S+)$"),
    ANSWER("^answer\\s+-a\\s+(\\S+)$"),
    NEW_PASSWORD("^(\\S+)$");

    private final String pattern;
    private final Pattern compiledPattern;

    LoginMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
