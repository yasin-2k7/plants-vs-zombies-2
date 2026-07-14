package view.terminalView;

import controller.GreenhouseMenuController;
import models.enums.commands.GreenhouseMenuCommands;
import view.View;

import java.util.List;
import java.util.regex.Matcher;

public class GreenhouseMenuView implements View {
    private static GreenhouseMenuView instance;
    private GreenhouseMenuController controller;

    public GreenhouseMenuView(GreenhouseMenuController controller) {
        this.controller = controller;
    }

    public static GreenhouseMenuView getInstance() {
        if (instance == null) {
            instance = new GreenhouseMenuView(new GreenhouseMenuController());
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {
        command = command.trim();

        for (GreenhouseMenuCommands ghMenuCommand : GreenhouseMenuCommands.values()) {
            Matcher matcher = ghMenuCommand.matcher(command);
            if (matcher.matches()) {
                switch (ghMenuCommand) {
                    case SHOW_GREENHOUSE:
                        List<String> status = controller.showGreenhouse();
                        for (String s : status) {
                            System.out.println(s);
                        }
                        return;
                    case PLANT_POT:
                        int px = Integer.parseInt(matcher.group(1));
                        int py = Integer.parseInt(matcher.group(2));
                        System.out.println(controller.plantPot(px, py));
                        return;
                    case COLLECT:
                        int cx = Integer.parseInt(matcher.group(1));
                        int cy = Integer.parseInt(matcher.group(2));
                        System.out.println(controller.collect(cx, cy));
                        return;
                    case GROW:
                        int gx = Integer.parseInt(matcher.group(1));
                        int gy = Integer.parseInt(matcher.group(2));
                        System.out.println(controller.grow(gx, gy));
                        return;
                    case ENTER_SHOP:
                        System.out.println("Entering shop...");
                        return;
                    case MENU_EXIT:
                        System.out.println("Exiting greenhouse. Returning to main menu.");
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("Unknown command in Greenhouse menu.");
    }
}