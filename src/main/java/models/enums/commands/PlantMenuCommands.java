package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PlantMenuCommands {
    MENU_ENTER("menu enter level"),
    MENU_SHOW_CURRENT("menu show current"),
    MENU_EXIT("menu exit"),
    SHOW_ALL_PLANTS("show all plants"),
    SHOW_AVAILABLE_PLANTS("show available plants"),
    ADD_PLANT("add plant -t (\\w+)"),
    REMOVE_PLANT("remove plant -t (\\w+)"),
    BOOST_PLANT("boost plant -t (\\w+)"),
    START_GAME("start game");

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
