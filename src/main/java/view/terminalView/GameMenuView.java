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
                        break;
                    case MENU_EXIT:
                        controller.exitMenu();
                        break;
                    case MENU_ENTER:
                        controller.enterMenu(matcher.group("name"));
                        break;
                    case SHOW_MAP:
                        controller.showMap();
                        break;
                    case FEED_PLANT:
                        float x = Float.parseFloat(matcher.group("x"));
                        float y = Float.parseFloat(matcher.group("y"));
                        controller.feedPlant(x, y);
                        break;
                    case COLLECT_SUN:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.collectSun(x, y);
                        break;
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
                        break;
                    case PLUCK_PLANT:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.pluckPlant(x, y);
                        break;
                    case ZOMBIE_INFO:
                        controller.zombieInfo();
                        break;
                    case CHEAT_ADD_SUNS:
                        int amount = Integer.parseInt(matcher.group("count"));
                        controller.cheatAddSun(amount);
                        break;
                    case SHOW_SUN_AMOUNT:
                        controller.showSunAmount();
                        break;
                    case RELEASE_THE_NUKE:
                        controller.releaseTheNuke();
                        break;
                    case MENU_SHOW_CURRENT:
                        controller.showCurrentMenu();
                        break;
                    case SHOW_TILE_STATUS:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.showTileStatus(x, y);
                        break;
                    case CHEAT_SPAWN_ZOMBIE:
                        type = matcher.group("type");
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.cheatSpawnZombie(type, x, y);
                        break;
                    case SHOW_PLANTS_STATUS:
                        controller.showPlantsStatus();
                        break;
                    case CHEAT_REMOVE_COOLDOWN:
                        controller.removeCooldown();
                        break;
                    case CHEAT_ADD_PLANT_FOOD:
                        controller.cheatAddPlantFood();
                        break;
                    case START_ZOMBIE_WAVES:
                        break;
                    default:
                        System.out.println("invalid command!");
                }
            }
        }
    }

    public void showResult(String message){
        System.out.println(message);
    }

}
