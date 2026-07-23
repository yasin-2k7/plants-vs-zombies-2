package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ShopMenuCommands {
    MENU_ENTER("^menu\\s+enter\\s+shop$"),
    MENU_SHOW_CURRENT("^menu\\s+show\\s+current$"),
    MENU_EXIT("^menu\\s+exit$"),
    SHOP_LIST("^shop\\s+list$"),
    SHOP_DAILY("^shop\\s+daily$"),
    SHOP_BUY("^shop\\s+buy\\s+-i\\s+(\\w+)\\s+-n\\s+(\\d+)(?:\\s+-t\\s+(\\w+))?$");

    private final Pattern compiledPattern;

    ShopMenuCommands(String pattern) {
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }
}