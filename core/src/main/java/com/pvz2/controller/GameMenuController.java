package com.pvz2.controller;


import com.badlogic.gdx.Screen;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.core.UserDataManager;
import com.pvz2.models.enums.CollectableType;
import com.pvz2.models.enums.PlantLayer;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.greenhouse.GreenHouse;
import com.pvz2.models.miniGame.IZombie.IZombieLevel;
import com.pvz2.models.miniGame.beghouled.BeghouledMechanics;
import com.pvz2.models.miniGame.beghouled.GridPosition;
import com.pvz2.models.miniGame.bowling.BowlingMechanics;
import com.pvz2.models.miniGame.vaseBreaker.VaseBreakerLevel;
import com.pvz2.models.mupoint.MupointManager;
import com.pvz2.models.plant.card.ImitatorCard;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.world.*;
import com.pvz2.models.world.levelSetup.DeadLineLevelSetup;
import com.pvz2.models.world.levelSetup.PlantWhatYouGetLevelSetup;
import com.pvz2.models.world.mechanics.NormalMechanic;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.ZombieFactory;
import com.pvz2.models.zombie.wave.WaveManager;
import com.pvz2.view.GameScreen;
import com.pvz2.view.MenuScreen;

import java.util.List;

public class GameMenuController implements MenuController {
    public static void handleWinning(GameWorld gameWorld) {
        if (gameWorld instanceof IZombieLevel) {
//            GameMenuView.getInstance().showResult("Delicious! You ate all the brains and WON the level! 🧠😋");
        } else {
//            GameMenuView.getInstance().showResult(
//                    "Dear humanz, zis is not done yet; we will come back to eat your brainz, humanz.");
        }
        if (gameWorld.isWillUnlockLevel()) {
            App.getCurrentUser().unlockLevel();
        }
        User user = App.getCurrentUser();
        MupointManager mupointManager = gameWorld.getMupointManager();
        if (user != null && mupointManager != null) {
            int currentLevelPoints = mupointManager.getTotalMupoints();
//            GameMenuView.getInstance().showResult("Your Mupoint in this level: " + currentLevelPoints);

            if (currentLevelPoints > user.getMaxMupoint()) {
                user.updateMupointRecord(currentLevelPoints);
                UserDataManager.saveUser(user);
//                GameMenuView.getInstance().showResult("New High Score! " +
//                        "Updated Mupoint record to: " + currentLevelPoints);
            }
        }
//        AppView.setCurrentScreen(MainMenuView.getInstance());
        App.setCurrentGame(null);
        if (!gameWorld.isConveyorMode()){
            for (PlantCard plantCard : gameWorld.getPlantLists()){
                App.getCurrentUser().getPlantBoosts().remove(plantCard.getType());
            }
        }
    }

    public static void handleLosing(GameWorld gameWorld) {
        if (gameWorld instanceof IZombieLevel) {
//            GameMenuView.getInstance().showResult("You ran out of zombies and failed to eat all the brains! LOSER!!!");
        } else if (gameWorld.getLevelSetup() instanceof DeadLineLevelSetup) {
//            GameMenuView.getInstance().showResult("Zombie passed deadLine; Loser!!!");
        } else {
//            GameMenuView.getInstance().showResult("LOSER!!!");
        }
        User user = App.getCurrentUser();
        MupointManager mupointManager = gameWorld.getMupointManager();
        if (user != null && mupointManager != null) {
            int currentLevelPoints = mupointManager.getTotalMupoints();
//            GameMenuView.getInstance().showResult("Your Mupoint in this level: " + currentLevelPoints);

            if (currentLevelPoints > user.getMaxMupoint()) {
                user.updateMupointRecord(currentLevelPoints);
                UserDataManager.saveUser(user);
//                GameMenuView.getInstance().showResult("New High Score! " +
//                        "Updated Mupoint record to: " + currentLevelPoints);
            }
        }
//        AppView.setCurrentScreen(MainMenuView.getInstance());
        App.setCurrentGame(null);
        App.getCurrentUser().getPlantBoosts().clear();
    }
    public static void updateState(String state) {
//        GameMenuView.getInstance().showResult(state);
    }
    @Override
    public void changeMenu() {}
    @Override
    public void exitMenu() {
        App.setCurrentGame(null);
        App.getCurrentUser().getPlantBoosts().clear();
//        AppView.setCurrentScreen(MainMenuView.getInstance());
    }
//    public void showCurrentMenu() {GameMenuView.getInstance().showResult("Current menu: game menu");}

    public void advanceTime(int count) {
        if (count <= 0) {
//            GameMenuView.getInstance().showResult("Count must be an integer bigger than 0!");
            return;
        }
//        GameMenuView.getInstance().showResult(count + " ticks later...");
        GameWorld game = App.getCurrentGame();
        for (int i = 0; i < count; i++) {
//            if (game != null && AppView.currentScreen instanceof GameMenuView && game.getState() == GameState.PLAYING) {
                game.tick(App.getCurrentGame().getElapsedTime());
//            } else return;
        }
    }

    public void collectSun(float x, float y) {
        for (Sun sun : App.getCurrentGame().getActiveSuns()) {
            if (sun.isCollected()) continue;
            if (Math.abs(sun.getX() - x) < 2 && Math.abs(sun.getY() - y) < 2) {
                if (sun.getType() == SunType.RADIOACTIVE) {
                    Cell[][] grid = App.getCurrentGame().getGrid();
                    sun.collect();
                    Cell sunCell = Cell.findCell(sun.getX(), sun.getY(), grid);
                    if (sunCell != null) {
                        List<Cell> zombieCells = Cell.getNeighborCells(sunCell, grid, 2);
                        List<Zombie> zombies = Cell.getZombiesInCells(zombieCells);
                        for (Zombie zombie : zombies) {
                            zombie.takeDamage(150, "NORMAL");
                        }
                        List<Cell> plantCells = Cell.getNeighborCells(sunCell, grid, 1);
                        for (Cell cell : plantCells) {
                            if (cell.getPlant(PlantLayer.BASE) != null)
                                cell.getPlant(PlantLayer.BASE).takeDamage(80);
                            if (cell.getPlant(PlantLayer.MAIN) != null)
                                cell.getPlant(PlantLayer.MAIN).takeDamage(80);
                            if (cell.getPlant(PlantLayer.SHIELD) != null)
                                cell.getPlant(PlantLayer.SHIELD).takeDamage(80);
                        }
                    }
                } else {
                    GameWorld game = App.getCurrentGame();
                    sun.collect();
                    game.setSun(game.getSun() + sun.getSize());
                    if (sun.getProducer() != null) sun.getProducer().getComponentSuns().remove(sun);
                    User user = App.getCurrentUser();
                    if (user != null) {
                        user.getQuestStats().addSunsCollectedToday(sun.getSize());
                        user.getQuestManager().checkAllQuests(user, false);
                    }
                }
                return;
            }
        }
//        GameMenuView.getInstance().showResult("there is no sun in that place!");
    }

    public void collectCollectable(float x, float y, String type) {
        CollectableType selectedType = null;
        for (CollectableType collectableType : CollectableType.values()) {
            if (collectableType.name().equalsIgnoreCase(type)) {
                selectedType = collectableType;
                break;
            }
        }
        if (selectedType == null) {
//            GameMenuView.getInstance().showResult("invalid collectable type");
            return;
        }
        for (Collectable collectable : App.getCurrentGame().getActiveCollectables()) {
            if (collectable.isDead()) continue;
            if (Math.abs(collectable.getX() - x) < 2 && Math.abs(collectable.getY() - y) < 2) {
                switch (collectable.getType()) {
                    case POT:
                        GreenHouse greenhouse = App.getCurrentUser().getGreenhouse();
                        String result = greenhouse.unlockFirstLockedPot();
//                        GameMenuView.getInstance().showResult(result);
                        break;
                    case COIN:
                        App.getCurrentUser().setCoins(App.getCurrentUser().getCoins() + 10);
//                        GameMenuView.getInstance().showResult(
//                                "coin collected. now you have " + App.getCurrentUser().getCoins() + " coins.");
                        break;
                    case DIAMOND:
                        App.getCurrentUser().setGems(App.getCurrentUser().getGems() + 1);
//                        GameMenuView.getInstance().showResult(
//                                "gem collected. now you have " + App.getCurrentUser().getGems() + " gems.");
                        break;
                    case PLANT_FOOD:
                        if (App.getCurrentGame().getPlantFoods() < 3) {
                            App.getCurrentGame().setPlantFoods(App.getCurrentGame().getPlantFoods() + 1);
//                            GameMenuView.getInstance().showResult("plant food collected. now you have " +
//                                    App.getCurrentGame().getPlantFoods() + " plant foods.");
                        } else {
//                            GameMenuView.getInstance().showResult("not enough space!");
                        }
                        break;
                }
                collectable.collect();
                return;
            }
        }
//        GameMenuView.getInstance().showResult("there is no collectable in that place!");
    }


    public void cheatAddSun(int count) {
        App.getCurrentGame().setSun(25 * count + App.getCurrentGame().getSun());
//        GameMenuView.getInstance().showResult("Cheat activated! Added " + count + " suns. ☀️");
    }

    public void releaseTheNuke() {
        GameWorld game = App.getCurrentGame();
        NormalMechanic normal = game.getMechanic(NormalMechanic.class);
        if (normal != null && normal.getWaveManager() != null) {
            normal.getWaveManager().releaseTheNuke(game);
        } else {
            for (Zombie zombie : game.getActiveZombies()) {
                zombie.die();
            }
//            GameMenuView.getInstance().showResult("All zombies eliminated by nuke (fallback)!");
        }
    }

    public static boolean selectAndUnselectPlant(PlantType type, MenuScreen screen) {
        if (App.getCurrentGame().isPlantSelected()){
            if (App.getCurrentGame().getSelectedPlant() == type){
                unselectPlant();
                return false;
            }
            unselectPlant();
        }
        List<PlantCard> gamePlants = App.getCurrentGame().getPlantLists();
        PlantCard selectedCard = null;
        for (PlantCard card : gamePlants) {
            if (card.getType().equals(type)) {
                selectedCard = card;
                break;
            }
        }
        if (selectedCard == null) {
            return false;
        }
        if (!selectedCard.isReady()){
            screen.addToast("Error", "This plant isn't ready!");
            return false;
        }
        if (!(selectedCard.getSunCost() <= App.getCurrentGame().getSun())){
            screen.addToast("Error", "You don't have enough suns!");
            return false;
        }
        App.getCurrentGame().setPlantSelected(true);
        App.getCurrentGame().setSelectedPlant(selectedCard.getType());
        return true;
    }
    public static void unselectPlant() {
        App.getCurrentGame().setSelectedPlant(null);
        App.getCurrentGame().setPlantSelected(false);
    }
    public static boolean plantSelectedPlant(float x, float y, MenuScreen screen) {
        if (App.getCurrentGame().getSelectedPlant() == null) {
            return false;
        }
        if (plantPlant(App.getCurrentGame().getSelectedPlant(), x, y, screen)){
            unselectPlant();
            return true;
        }
        return false;
    }

    public static boolean plantPlant(PlantType type, float x, float y, MenuScreen screen) {
        PlantCard selectedCard = null;
        List<PlantCard> gamePlants = App.getCurrentGame().isConveyorMode()?
                App.getCurrentGame().getConveyorBelt() : App.getCurrentGame().getPlantLists();
        for (PlantCard card : gamePlants) {
            if (card.getType().equals(type)) {
                selectedCard = card;
                break;
            }
        }
        if (selectedCard == null) {
            return false;
        }
        return plantPlant(selectedCard, x, y, screen);
    }

    private static Cell findCellAt(GameWorld game, float x, float y) {return Cell.findCell(x, y,
        game.getGrid());}

    public static boolean plantPlant(PlantCard card, float x, float y, MenuScreen screen) {
        Cell selectedCell = findCellAt(App.getCurrentGame(), x, y);
        if (selectedCell == null) {
            return false;
        }
        String error;
        PlantType type;
        GameScreen gameScreen = screen instanceof GameScreen gameScreen1 ? gameScreen1 : null;
        if (card instanceof ImitatorCard imitatorCard) {
            type = imitatorCard.getTargetType();
            error = selectedCell.handlePlanting(type,
                    App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.IMITATER)>=4, gameScreen);
        } else {
            type = card.getType();
            error = selectedCell.handlePlanting(type, App.getCurrentUser().hasBoost(type), gameScreen);
        }
        if (error != null){
            screen.addToast("Error", error);
            return false;
        }
        else {
            card.setReady(false);
            App.getCurrentGame().setSun(App.getCurrentGame().getSun() - card.getSunCost());
            return true;
            }
    }

    public void removeCooldown() {
        if (App.getCurrentGame().isConveyorMode()) return;
        for (PlantCard card : App.getCurrentGame().getPlantLists()) {
            card.deactivateCooldown();
        }
    }

    public void pluckPlant(float x, float y) {
        Cell selectedCell = findCellAt(App.getCurrentGame(), x, y);
        if (selectedCell == null || !selectedCell.findAndRemovePlant()) {
//            GameMenuView.getInstance().showResult("there is no plant in that place!");
        }
    }

    public void feedPlant(float x, float y) {
        if (App.getCurrentGame().getPlantFoods() <= 0) {
//            GameMenuView.getInstance().showResult("you have not any plant foods!");
            return;
        }
        Cell selectedCell = null;
        for (Cell[] cells : App.getCurrentGame().getGrid()) {
            if (!(cells[0].getY() + App.getCellHeight() / 2 > y && cells[0].getY() - App.getCellHeight() / 2 < y))
                continue;
            for (Cell cell : cells) {
                if ((cell.getX() + App.getCellWidth() / 2 > x && cell.getX() - App.getCellWidth() / 2 < x)) {
                    selectedCell = cell;
                    break;
                }
            }
        }
        if (selectedCell == null || selectedCell.findPlant() == null) {
//            GameMenuView.getInstance().showResult("there is no plant in that place!");
            return;
        }
        selectedCell.findPlant().activatePlantFood();
        App.getCurrentGame().setPlantFoods(App.getCurrentGame().getPlantFoods() - 1);
    }

    public void cheatAddPlantFood() {
        if (App.getCurrentGame().getPlantFoods() < 3) {
            App.getCurrentGame().setPlantFoods(App.getCurrentGame().getPlantFoods() + 1);
        }
    }


    public void cheatSpawnZombie(String type, float x, float y) {
        Zombie zombie = new ZombieFactory().createZombie(type);
        if (zombie == null) {
//            GameMenuView.getInstance().showResult("❌invalid zombie type!: " + type);
            return;
        }
        float newY = -1;
        for (Cell[] cells : App.getCurrentGame().getGrid()) {
            if (cells[0].getY() + App.getCellHeight() / 2 >= y && cells[0].getY() - App.getCellHeight() / 2 <= y) {
                newY = cells[0].getY();
            }
        }
        if (x < 0 || x > App.getCellWidth() * 9 || newY == -1) {
//            GameMenuView.getInstance().showResult("you cannot spawn zombie in that place!");
            return;
        }
        zombie.setX(x);
        zombie.setY(newY);
        App.getCurrentGame().getActiveZombies().add(zombie);
//        GameMenuView.getInstance().showResult("Spawned " + type + " at (" + x + ", " + newY + ")");
    }
    //miniGames
    public void breakVase(int row, int col) {
        GameWorld game = App.getCurrentGame();
        if (!(game instanceof VaseBreakerLevel level)) {
//            GameMenuView.getInstance().showResult("this command is only available in Vase Breaker!");
            return;
        }
//        GameMenuView.getInstance().showResult(level.breakVaseAt(row, col));
    }

    public void pickUpSeed(int row, int col) {
        GameWorld game = App.getCurrentGame();
        if (!(game instanceof VaseBreakerLevel level)) {
//            GameMenuView.getInstance().showResult("this command is only available in Vase Breaker!");
            return;
        }
//        GameMenuView.getInstance().showResult(level.pickUpSeedAt(row, col));
    }

    public void plantHeldSeed(float x, float y) {
        GameWorld game = App.getCurrentGame();
        if (!(game instanceof VaseBreakerLevel level)) {
//            GameMenuView.getInstance().showResult("this command is only available in Vase Breaker!");
            return;
        }

        PlantType heldType = level.getHeldSeed();
        if (heldType == null) {
//            GameMenuView.getInstance().showResult("You are not holding any seed!");
            return;
        }

        Cell selectedCell = findCellAt(game, x, y);
        if (selectedCell == null) {
//            GameMenuView.getInstance().showResult("you cannot plant in that place!");
            return;
        }

        String error = selectedCell.handlePlanting(heldType);
        if (error != null) {
//            GameMenuView.getInstance().showResult(error);
        } else {
            level.clearHeldSeed();
//            GameMenuView.getInstance().showResult("Planted " + heldType.name() + "!");
        }
    }

    public void placeZombie(String type, float x, float y) {
        GameWorld world = App.getCurrentGame();
        if (!(world instanceof IZombieLevel level)) {
//            GameMenuView.getInstance().showResult("this command is only available in I Zombie!");
            return;
        }
        Zombie zombie;
        try {
            zombie = new ZombieFactory().createZombie(type);
        } catch (Exception e) {
//            GameMenuView.getInstance().showResult("invalid zombie");
            return;
        }
        boolean allowed = level.getAvailableZombies().stream()
                .anyMatch(z -> z.getName() == zombie.getName()
                        || (z.getSpecificName() != null && z.getSpecificName().equalsIgnoreCase(type)));
        if (!allowed) {
//            GameMenuView.getInstance().showResult("you dont have this zombie");
            return;
        }
//        GameMenuView.getInstance().showResult(level.placeZombie(zombie, x, y));
    }

    public void swapPlants(int row1, int col1, int row2, int col2) {
        GameWorld game = App.getCurrentGame();
        BeghouledMechanics mechanics = game.getMechanic(BeghouledMechanics.class);
        if (mechanics == null) {
//            GameMenuView.getInstance().showResult("this command is only available in Beghouled!");
            return;
        }

        GridPosition a = new GridPosition(row1, col1);
        GridPosition b = new GridPosition(row2, col2);

        String error = mechanics.trySwap(game, a, b);
        if (error != null) {
//            GameMenuView.getInstance().showResult(error);
        } else {
//            GameMenuView.getInstance().showResult(
//                    "Swap successful! Score: " + mechanics.getScore() + "/" + mechanics.getTargetScore());
        }
    }

    public void upgradePlant(PlantType type) {
        GameWorld game = App.getCurrentGame();
        BeghouledMechanics mechanics = game.getMechanic(BeghouledMechanics.class);
        if (mechanics == null) {
//            GameMenuView.getInstance().showResult("this command is only available in Beghouled!");
            return;
        }

        String error = mechanics.upgradePlant(game, type);
        if (error != null) {
//            GameMenuView.getInstance().showResult(error);
        } else {
//            GameMenuView.getInstance().showResult("Upgrade successful! Remaining sun: " + game.getSun());
        }
    }

    public void resetMap() {
        GameWorld world = App.getCurrentGame();
        BeghouledMechanics mechanics = world.getMechanic(BeghouledMechanics.class);
        if (mechanics == null) {
//            GameMenuView.getInstance().showResult("this command is only available in Beghouled!");
            return;
        }
        mechanics.resetBoard(world);
    }

    public void throwBowlingBall(PlantType plantType, float x, float y) {
        GameWorld game = App.getCurrentGame();
        BowlingMechanics mechanics = game.getMechanic(BowlingMechanics.class);
        if (mechanics == null) {
//            GameMenuView.getInstance().showResult("this command is only available in Bowling!");
            return;
        }
//        GameMenuView.getInstance().showResult(mechanics.throwBall(game, plantType, x, y));
    }

    public String startWaves(){
        if(!(App.getCurrentGame().getLevelSetup() instanceof PlantWhatYouGetLevelSetup)){
            return "this command is for Plant What You Get Level";
        }
        WaveManager waveManager = App.getCurrentGame().getWaveManager();
        if (waveManager.isWavesStarted()) {
            return "Error: Zombie waves have already started!";
        }
        App.getCurrentGame().setPlantingPhase(false);
        waveManager.startWaves();
        return "Zombie waves started!";
    }

    public static void showAnnouncement(String message) {
        GameScreen.announce(message);
    }
}
