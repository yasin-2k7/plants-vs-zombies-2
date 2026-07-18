package view.terminalView;

import controller.CollectionMenuController;
import controller.LevelMenuController;
import controller.LoginMenuController;
import models.enums.PlantType;
import models.enums.commands.CollectionMenuCommands;
import models.enums.commands.GameMenuCommands;
import models.enums.commands.PlantMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class CollectionMenuView implements View {
    private static CollectionMenuView instance;

    public static CollectionMenuView getInstance(){
        if (instance == null){
            instance = new CollectionMenuView(new CollectionMenuController());
            return instance;
        }
        return instance;
    }
    public CollectionMenuView(CollectionMenuController controller) {
        this.controller = controller;
    }

    private CollectionMenuController controller;

    @Override
    public void processCommand(String command) {
        for (CollectionMenuCommands collectionMenuCommands : CollectionMenuCommands.values()) {
            Matcher matcher = collectionMenuCommands.matcher(command);
            if (matcher.matches()) {
                String name;
                PlantType type;
                switch (collectionMenuCommands) {
                    case MENU_ENTER:
                        controller.changeMenu();
                        return;
                    case MENU_EXIT:
                        controller.exitMenu();
                        return;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        return;
                    case MENU_COLLECTION_UPGRADE:
                        name = matcher.group("plant");
                        type = controller.getPlantType(name);
                        if (type != null) controller.upgradePlant(type);
                        return;
                    case MENU_COLLECTION_PURCHASE:
                        name = matcher.group("plant");
                        type = controller.getPlantType(name);
                        if (type != null) controller.purchasePlant(type);
                        return;
                    case MENU_COLLECTION_SHOW_PLANTS:
                        controller.showPlants();
                        return;
                    case MENU_COLLECTION_SHOW_ZOMBIES:
                        controller.showZombies();
                        return;
                    case MENU_COLLECTION_SHOW_ONE_PLANT:
                        name = matcher.group("plant");
                        type = controller.getPlantType(name);
                        if (type != null) controller.showPlant(type);
                        return;
                    case MENU_COLLECTION_SHOW_ALL_PLANTS:
                        controller.showAllPlants();
                        return;
                    case MENU_COLLECTION_SHOW_ONE_ZOMBIE:
                        String zombieName = matcher.group("zombie");
                        controller.showZombie(zombieName);
                        return;
                    case MENU_COLLECTION_SHOW_ALL_ZOMBIES:
                        controller.showAllZombies();
                        return;
                    default:
                        break;
                }
            }
        }
        System.out.println("invalid command!");
    }


    public void showResult(String message){
        System.out.println(message);
    }


}
