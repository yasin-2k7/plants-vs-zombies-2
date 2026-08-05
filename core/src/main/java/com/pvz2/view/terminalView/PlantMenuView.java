package com.pvz2.view.terminalView;

import com.pvz2.controller.PlantMenuController;
import com.pvz2.models.enums.commands.PlantMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class PlantMenuView implements View {
    private static PlantMenuView instance;
    private PlantMenuController controller;

    private PlantMenuView(PlantMenuController controller) {
        this.controller = controller;
    }

    public static PlantMenuView getInstance() {
        if (instance == null) {
            instance = new PlantMenuView(new PlantMenuController());
        }
        return instance;
    }

    public PlantMenuController getController() {
        return controller;
    }

    @Override
    public void processCommand(String command) {
        command = command.trim();

        for (PlantMenuCommands cmd : PlantMenuCommands.values()) {
            Matcher matcher = cmd.matcher(command);
            if (matcher.matches()) {
                switch (cmd) {
                    case SHOW_ALL_PLANTS:
                        System.out.println(controller.showAllPlants());
                        return;
                    case SHOW_AVAILABLE_PLANTS:
                        System.out.println(controller.showAvailablePlants());
                        return;
                    case ADD_PLANT:
                        String targetType = matcher.group(2);
                        if (targetType != null){
                            System.out.println(controller.addPlant(matcher.group(1),
                                    matcher.group(2)));
                        } else {
                            System.out.println(controller.addPlant(matcher.group(1)));
                        }
                        return;
                    case REMOVE_PLANT:
                        System.out.println(controller.removePlant(matcher.group(1)));
                        return;
                    case BOOST_PLANT:
                        System.out.println(controller.boostPlant(matcher.group(1)));
                        return;
                    case START_GAME:
                        System.out.println(controller.startGame());
                        return;
                    case MENU_EXIT:
                        System.out.println("Exiting level selection. Returning to Game menu.");
                        AppView.setCurrentScreen(GameMenuView.getInstance());
                        return;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        return;
                    case MENU_ENTER:
                        System.out.println("Already in Level Selection menu.");
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("Unknown command in Level Selection menu.");
    }


}
