package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TravelLogCommands {
    MENU_ENTER("^menu\\s+enter\\s+(.+)$"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("^menu\\s+exit$"),
    TRAVEL_LOG_PAGE("^travel\\s+log\\s+page\\s+(\\S+)$"),  // page name: daily, main, epic, minigame
    PLAY_MINIGAME("\\s*play\\s+(\\S+)\\s+([1-3])\\s*");

    private final String pattern;
    private final Pattern compiledPattern;

    TravelLogCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

}
