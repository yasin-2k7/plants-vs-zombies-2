package com.pvz2.models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GreenhouseMenuCommands {
    MENU_ENTER("menu enter greenhouse"),
    MENU_SHOW_CURRENT("menu show current"),
    MENU_EXIT("menu exit"),
    SHOW_GREENHOUSE("show greenhouse"),
    PLANT_POT("plant pot at \\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)"),
    COLLECT("collect \\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)"),
    GROW("grow \\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)"),
    ENTER_SHOP("enter shop");

    private final String pattern;
    private final Pattern compiledPattern;

    GreenhouseMenuCommands(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String input) {
        return compiledPattern.matcher(input);
    }

    public String getPattern() {
        return pattern;
    }
}
