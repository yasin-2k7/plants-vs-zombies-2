package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ShopMenuCommands {
    MENU_ENTER(""),
    MENU_SHOW_CURRENT(""),
    MENU_EXIT(""),
    SHOP_LIST(""),
    SHOP_DAILY(""),
    SHOP_BUY("");

    private final String pattern;
    private final Pattern compiledPattern;

    ShopMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

}
