package view.terminalView;

import controller.GameMenuController;
import models.enums.PlantType;
import models.enums.commands.GameMenuCommands;
import view.View;

import java.util.regex.Matcher;

public class GameMenuView implements View {
    private static GameMenuView instance;
    private GameMenuController controller;

    public static GameMenuView getInstance() {
        if (instance == null) {
            instance = new GameMenuView();
            instance.controller = new GameMenuController();
            return instance;
        }
        return instance;
    }

    @Override
    public void processCommand(String command) {
        command = command.trim();
        for (GameMenuCommands gameMenuCommands : GameMenuCommands.values()) {
            Matcher matcher = gameMenuCommands.matcher(command);
            if (matcher.matches()) {
                switch (gameMenuCommands) {
                    case ADVANCE_TIME:
                        int count = Integer.parseInt(matcher.group("count"));
                        if (count <= 0) {
                            System.out.println("Count must be an integer bigger than 0!");
                            return;
                        }
                        controller.advanceTime(count);
                        return;
                    case MENU_EXIT:
                        controller.exitMenu();
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
                    case COLLECT_COLLECTABLE:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        String type = matcher.group("type");
                        controller.collectCollectable(x, y, type);
                        return;
                    case UNSELECT_PLANT:
                        controller.unselectPlant();
                        return;
                    case SELECT_PLANT:
                        type = matcher.group("type");
                        PlantType selectedType = null;
                        for (PlantType plantType : PlantType.values()) {
                            if (plantType.name().equalsIgnoreCase(type)) {
                                selectedType = plantType;
                                break;
                            }
                        }
                        if (selectedType == null) {
                            System.out.println("please select a valid plant");
                            return;
                        }
                        controller.selectPlant(selectedType);
                        return;
                    case PLANT_SELECTED_PLANT:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.plantSelectedPlant(x, y);
                        return;
                    case PLANT_PLANT:
                        type = matcher.group("type");
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        selectedType = null;
                        for (PlantType plantType : PlantType.values()) {
                            if (plantType.name().equalsIgnoreCase(type)) {
                                selectedType = plantType;
                                break;
                            }
                        }
                        if (selectedType == null) {
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
                    case SHOW_PLANT_FOODS:
                        controller.showPlantFoodsCount();
                        return;
                    case START_ZOMBIE_WAVES:
                        String result = controller.startWaves();
                        System.out.println(result);
                        return;

                    case BREAK_VASE:
                        int row = Integer.parseInt(matcher.group("row"));
                        int col = Integer.parseInt(matcher.group("col"));
                        controller.breakVase(row, col);
                        return;

                    case PICK_UP_SEED:
                        row = Integer.parseInt(matcher.group("row"));
                        col = Integer.parseInt(matcher.group("col"));
                        controller.pickUpSeed(row, col);
                        return;

                    case PLANT_HELD_SEED:
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.plantHeldSeed(x, y);
                        return;

                    case SWAP_PLANTS:
                        int row1 = Integer.parseInt(matcher.group("row1"));
                        int col1 = Integer.parseInt(matcher.group("col1"));
                        int row2 = Integer.parseInt(matcher.group("row2"));
                        int col2 = Integer.parseInt(matcher.group("col2"));
                        controller.swapPlants(row1, col1, row2, col2);
                        return;
                    case UPGRADE_PLANT:
                        type = matcher.group("type");
                        PlantType upgradeType = null;
                        for (PlantType plantType : PlantType.values()) {
                            if (plantType.name().equalsIgnoreCase(type)) {
                                upgradeType = plantType;
                                break;
                            }
                        }
                        if (upgradeType == null) {
                            System.out.println("please select a valid plant");
                            return;
                        }
                        controller.upgradePlant(upgradeType);
                        return;
                    case RESIT_MAP:
                        controller.resetMap();
                        System.out.println("your map reset");
                        break;

                    case PLACE_ZOMBIE:
                        type = matcher.group("type");
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        controller.placeZombie(type, x, y);
                        return;
                    case THROW_BOWLING_BALL:
                        type = matcher.group("type");
                        x = Float.parseFloat(matcher.group("x"));
                        y = Float.parseFloat(matcher.group("y"));
                        PlantType ballType = null;
                        for (PlantType bt : PlantType.values()) {
                            if (bt.name().equalsIgnoreCase(type)) {
                                ballType = bt;
                                break;
                            }
                        }
                        if (ballType == null) {
                            System.out.println("please select a valid bowling ball type");
                            return;
                        }
                        controller.throwBowlingBall(ballType, x, y);
                        return;
                    default:
                        break;

                }
            }
        }
        System.out.println("invalid command!");
    }

    public void showResult(String message) {
        System.out.println(message);
    }

}
