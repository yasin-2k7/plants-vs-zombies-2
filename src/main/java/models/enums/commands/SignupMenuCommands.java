package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignupMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    REGISTER(""),
    PICK_QUESTION("");

    private final String pattern;
    private final Pattern compiledPattern;

    SignupMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
