package view.terminalView;

import models.core.App;
import models.core.User;
import models.enums.commands.GreenhouseMenuCommands;
import models.greenhouse.GreenHouse;
import view.View;

import java.util.regex.Matcher;

public class GreenhouseMenuView implements View {

    @Override
    public void processCommand(String command) {
        command = command.trim();

        if (GreenhouseMenuCommands.SHOW_GREENHOUSE.matcher(command).matches()) {
            showGreenhouse();
        }
        else if (GreenhouseMenuCommands.PLANT_POT.matcher(command).matches()) {
            Matcher m = GreenhouseMenuCommands.PLANT_POT.matcher(command);
            if (m.find()) {
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                plantPot(x, y);
            }
        }
        else if (GreenhouseMenuCommands.COLLECT.matcher(command).matches()) {
            Matcher m = GreenhouseMenuCommands.COLLECT.matcher(command);
            if (m.find()) {
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                collect(x, y);
            }
        }
        else if (GreenhouseMenuCommands.GROW.matcher(command).matches()) {
            Matcher m = GreenhouseMenuCommands.GROW.matcher(command);
            if (m.find()) {
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                grow(x, y);
            }
        }
        else if (GreenhouseMenuCommands.ENTER_SHOP.matcher(command).matches()) {
            enterShop();
        }
        else if (GreenhouseMenuCommands.MENU_EXIT.matcher(command).matches()) {
            exitToMainMenu();
        }
        else {
            System.out.println("Unknown command in Greenhouse menu.");
        }
    }

    private void showGreenhouse() {
        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }
        GreenHouse greenhouse = user.getGreenhouse();
        greenhouse.showGreenhouse();
    }

    private void plantPot(int x, int y) {
        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }
        GreenHouse greenhouse = user.getGreenhouse();
        greenhouse.plantPot(x, y);
    }

    private void collect(int x, int y) {
        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }
        GreenHouse greenhouse = user.getGreenhouse();
        greenhouse.collect(x, y);
    }

    private void grow(int x, int y) {
        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }
        GreenHouse greenhouse = user.getGreenhouse();
        greenhouse.grow(x, y);
    }

    private void enterShop() {
        System.out.println("Entering shop...");
        // App.setCurrentMenu(new ShopMenuController().getView());
        System.out.println("Shop menu will be opened (implement menu switching).");
    }

    private void exitToMainMenu() {
        System.out.println("Exiting greenhouse. Returning to main menu.");
        // App.setCurrentMenu(new MainMenuController().getView());
    }
}