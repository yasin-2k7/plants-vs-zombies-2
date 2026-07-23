package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CollectionMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT("\\s*menu\\s+show\\s+current\\s*"),
    MENU_EXIT("\\s*menu\\s+exit\\s*"),
    MENU_COLLECTION_SHOW_PLANTS("\\s*menu\\s+collection\\s+show\\s+plants\\s*"),
    MENU_COLLECTION_SHOW_ALL_PLANTS("\\s*menu\\s+collection\\s+show\\s+all\\s+plants\\s*"),
    MENU_COLLECTION_SHOW_ZOMBIES("\\s*menu\\s+collection\\s+show\\s+zombies\\s*"),
    MENU_COLLECTION_SHOW_ALL_ZOMBIES("\\s*menu\\s+collection\\s+show\\s+all\\s+zombies\\s*"),
    MENU_COLLECTION_SHOW_ONE_PLANT("\\s*menu\\s+collection\\s+show-plant\\s+-p\\s+(?<plant>\\S+)\\s*"),
    MENU_COLLECTION_SHOW_ONE_ZOMBIE("\\s*menu\\s+collection\\s+show\\s+zombie\\s+-z\\s+(?<zombie>\\S+)\\s*"),
    MENU_COLLECTION_UPGRADE("\\s*menu\\s+collection\\s+upgrade-plant\\s+-p\\s+(?<plant>\\S+)\\s*"),
    MENU_COLLECTION_PURCHASE("\\s*menu\\s+collection\\s+purchase-plant\\s+-p\\s+(?<plant>\\S+)\\s*");

    private final Pattern compiledPattern;

    CollectionMenuCommands(String pattern) {
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

}
