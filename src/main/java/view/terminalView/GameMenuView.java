package view.terminalView;

import controller.GameMenuController;
import controller.SignupMenuController;
import models.enums.PlantType;
import models.enums.commands.GameMenuCommands;
import models.enums.commands.SignupMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class GameMenuView implements View{
    private static GameMenuView instance;
    public static GameMenuView getInstance(){
        if (instance == null){
            instance = new GameMenuView();
            instance.controller = new GameMenuController();
            return instance;
        }
        return instance;
    }
    private GameMenuController controller;
    @Override
    public void processCommand(String command) {
        for (GameMenuCommands gameMenuCommands : GameMenuCommands.values()) {
            Matcher matcher = gameMenuCommands.matcher(command);
            if (matcher.matches()) {
                switch (gameMenuCommands) {
                    case ADVANCE_TIME:
                        int count = Integer.parseInt(matcher.group("count"));
                        if (count <= 0){
                            System.out.println("Count must be an integer bigger than 0!");
                            return;
                        }
                        controller.advanceTime(count);
                        return;
                    case MENU_EXIT:
                        controller.exitMenu();
                        return;
                    case MENU_ENTER:
                        controller.enterMenu(matcher.group("name"));
                        return;
                    case SHOW_MAP:
                        controller.showMap();
                        return;
                    case FEED_PLANT:
                        float x = Float.parseFloat(matcher.group("x"));
                        float y = Float.parseFloat(matcher.group("y"));
                        controller.feedPlant(x, y);
                        return;
                    case COLLECT_SUN:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.collectSun(x, y);
                        return;
                    case PLANT_PLANT:
                        String type = matcher.group("type");
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        PlantType selectedType = null;
                        for (PlantType plantType : PlantType.values()){
                            if (plantType.name().equalsIgnoreCase(type)){
                                selectedType = plantType;
                                break;
                            }
                        }
                        if (selectedType == null){
                            System.out.println("please select a valid plant");
                            return;
                        }
                        controller.plantPlant(selectedType, x, y);
                        return;
                    case PLUCK_PLANT:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.pluckPlant(x, y);
                        return;
                    case ZOMBIE_INFO:
                        controller.zombieInfo();
                        return;
                    case CHEAT_ADD_SUNS:
                        int amount = Integer.parseInt(matcher.group("count"));
                        controller.cheatAddSun(amount);
                        return;
                    case SHOW_SUN_AMOUNT:
                        controller.showSunAmount();
                        return;
                    case RELEASE_THE_NUKE:
                        controller.releaseTheNuke();
                        return;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        return;
                    case SHOW_TILE_STATUS:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.showTileStatus(x, y);
                        return;
                    case CHEAT_SPAWN_ZOMBIE:
                        type = matcher.group("type");
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.cheatSpawnZombie(type, x, y);
                        return;
                    case SHOW_PLANTS_STATUS:
                        controller.showPlantsStatus();
                        return;
                    case CHEAT_REMOVE_COOLDOWN:
                        controller.removeCooldown();
                        return;
                    case CHEAT_ADD_PLANT_FOOD:
                        controller.cheatAddPlantFood();
                        return;
                    case START_ZOMBIE_WAVES:
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
