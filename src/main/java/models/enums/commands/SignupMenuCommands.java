package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignupMenuCommands {
    MENU_ENTER("^menu\\s+enter\\s+login\\s+menu$"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("^menu\\s+exit$"),
    REGISTER("^register\\s+-u\\s+(\\S+)\\s+-p\\s+(\\S+)\\s+(\\S+)\\s+-n\\s+(\\S+)\\s+-e\\s+(\\S+)\\s+-g\\s+(\\S+)$"),
    PICK_QUESTION("^pick question\\s+-q\\s+(\\d+)\\s+-a\\s+(\\S+)\\s+-c\\s+(\\S+)$");

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
