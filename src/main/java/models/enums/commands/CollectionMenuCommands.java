package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CollectionMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    MENU_COLLECTION_SHOW_PLANTS(""),
    MENU_COLLECTION_SHOW_ALL_PLANTS(""),
    MENU_COLLECTION_SHOW_ZOMBIES(""),
    MENU_COLLECTION_SHOW_ALL_ZOMBIES(""),
    MENU_COLLECTION_SHOW_ONE_PLANT(""),
    MENU_COLLECTION_SHOW_ONE_ZOMBIE(""),
    MENU_COLLECTION_UPGRADE(""),
    MENU_COLLECTION_PURCHASE("");

    private final String pattern;
    private final Pattern compiledPattern;

    CollectionMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

}
