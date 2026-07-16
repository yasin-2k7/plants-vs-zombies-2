package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PlantMenuCommands {
    MENU_ENTER("^menu\\s+enter\\s+level$"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("^menu\\s+exit$"),
    SHOW_ALL_PLANTS("^show\\s+all\\s+plants$"),
    SHOW_AVAILABLE_PLANTS("^show\\s+available\\s+plants$"),
    ADD_PLANT("^add\\s+plant\\s+-t\\s+(\\w+)$"),
    REMOVE_PLANT("^remove\\s+plant\\s+-t\\s+(\\w+)$"),
    BOOST_PLANT("^boost\\s+plant\\s+-t\\s+(\\w+)$"),
    START_GAME("^start\\s+game$");

    private final String pattern;
    private final Pattern compiledPattern;

    PlantMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}
