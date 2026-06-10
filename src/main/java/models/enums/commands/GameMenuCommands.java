package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    ADVANCE_TIME(""),
    COLLECT_SUN(""),
    SHOW_SUN_AMOUNT(""),
    CHEAT_ADD_SUNS(""),
    RELEASE_THE_NUKE(""),
    PLANT_PLANT(""),
    CHEAT_REMOVE_COOLDOWN(""),
    PLUCK_PLANT(""),
    FEED_PLANT(""),
    CHEAT_ADD_PLANT_FOOD(""),
    SHOW_MAP(""),
    SHOW_PLANTS_STATUS(""),
    SHOW_TILE_STATUS(""),
    ZOMBIE_INFO(""),
    CHEAT_SPAWN_ZOMBIE(""),
    START_ZOMBIE_WAVES("");

    private final String pattern;
    private final Pattern compiledPattern;

    GameMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

}
