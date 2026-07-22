package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    MENU_ENTER("\\s*menu\\s+enter\\s+(?<name>.+)"),
    MENU_SHOW_CURRENT("\\s*menu\\s+show\\s+current\\s*"),
    MENU_EXIT("\\s*menu\\s+exit\\s*"),
    ADVANCE_TIME("\\s*advance\\s+time\\s+-t\\s+(?<count>\\d+)\\s+tick\\s*"),
    COLLECT_SUN("\\s*collect\\s+sun\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    SHOW_SUN_AMOUNT("\\s*show\\s+sun\\s+amount\\s*"),
    CHEAT_ADD_SUNS("\\s*cheat\\s+add\\s+-n\\s+(?<count>\\d+)\\s+suns\\s*"),
    RELEASE_THE_NUKE("\\s*release\\s+the\\s+nuke\\s*"),
    PLANT_PLANT("\\s*plant\\s+plant\\s+-t\\s+(?<type>\\S+)\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    CHEAT_REMOVE_COOLDOWN("(?i)\\s*cheat\\s+remove-cooldown\\s*"),
    PLUCK_PLANT("\\s*pluck\\s+plant\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    FEED_PLANT("\\s*feed\\s+plant\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    CHEAT_ADD_PLANT_FOOD("\\s*cheat\\s+add-plant-food\\s*"),
    SHOW_MAP("\\s*show\\s+map\\s*"),
    SHOW_PLANTS_STATUS("\\s*show\\s+plants\\s+status\\s*"),
    SHOW_TILE_STATUS("\\s*show\\s+tile\\s+status\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    ZOMBIE_INFO("\\s*zombies\\s+info\\s*"),
    CHEAT_SPAWN_ZOMBIE("\\s*cheat\\s+spawn-zombie\\s+-t\\s+(?<type>.+?)\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    START_ZOMBIE_WAVES("\\s*start\\s+zombie\\s+waves\\s*"),

    BREAK_VASE("\\s*break\\s+vase\\s+-l\\s+\\(\\s*(?<row>\\d+)\\s*,\\s*(?<col>\\d+)\\s*\\)\\s*"),
    PICK_UP_SEED("\\s*pick\\s+up\\s+seed\\s+-l\\s+\\(\\s*(?<row>\\d+)\\s*,\\s*(?<col>\\d+)\\s*\\)\\s*"),
    PLANT_HELD_SEED("\\s*plant\\s+held\\s+seed\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    SWAP_PLANTS("\\s*swap\\s+plants\\s+-l1\\s+\\(\\s*(?<row1>\\d+)\\s*,\\s*(?<col1>\\d+)\\s*\\)\\s+-l2\\s+\\(\\s*(?<row2>\\d+)\\s*,\\s*(?<col2>\\d+)\\s*\\)\\s*"),
    UPGRADE_PLANT("\\s*upgrade\\s+plant\\s+-t\\s+(?<type>\\S+)\\s*"),
    PLACE_ZOMBIE("\\s*place\\s+zombie\\s+-t\\s+(?<type>\\S+)\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*"),
    THROW_BOWLING_BALL("\\s*throw\\s+bowling-ball\\s+-t\\s+(?<type>\\S+)\\s+-l\\s+\\(\\s*(?<x>\\d+)\\s*,\\s*(?<y>\\d+)\\s*\\)\\s*");



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
