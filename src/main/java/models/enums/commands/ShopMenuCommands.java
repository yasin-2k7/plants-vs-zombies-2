package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ShopMenuCommands {
    MENU_ENTER("menu enter shop"),
    MENU_SHOW_CURRENT("menu show current"),
    MENU_EXIT("menu exit"),
    SHOP_LIST("shop list"),
    SHOP_DAILY("shop daily"),
    SHOP_BUY("shop buy -i (\\w+) -n (\\d+)(?: -t (\\w+))?");

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