package controller;

import models.core.App;
import models.core.User;
import models.enums.CollectableType;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.miniGame.IZombie.IZombieLevel;
import models.miniGame.beghouled.BeghouledMechanics;
import models.miniGame.bowling.BowlingBallType;
import models.miniGame.bowling.BowlingMechanics;
import models.miniGame.vaseBreaker.VaseBreakerLevel;
import models.plant.Plant;
import models.plant.card.ImitatorCard;
import models.plant.card.PlantCard;
import models.world.*;
import models.world.mechanics.NormalMechanic;
import models.world.obstacles.Grave;
import models.world.obstacles.IceBlock;
import models.world.obstacles.Obstacle;
import models.world.obstacles.OctopusObstacle;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;
import models.zombie.wave.WaveManager;
import models.zombie.zombiesType.ArmoredZombie;
import view.terminalView.*;

import java.util.List;

public class GameMenuController implements MenuController {
    public static void handleWinning(GameWorld gameWorld) {
        GameMenuView.getInstance().showResult("Dear humanz, zis is not done yet; we will come back to eat your brainz, humanz.");
        if (gameWorld.isWillUnlockLevel()){
            App.getCurrentUser().unlockLevel();
        }
        AppView.setCurrentScreen(MainMenuView.getInstance());
        App.setCurrentGame(null);
        App.getCurrentUser().getPlantBoosts().clear();
    }

    public static void handleLosing(GameWorld gameWorld) {
        GameMenuView.getInstance().showResult(("The zombie ate your brain; LOSER!!!"));
        AppView.setCurrentScreen(MainMenuView.getInstance());
        App.setCurrentGame(null);
        App.getCurrentUser().getPlantBoosts().clear();
    }

    @Override
    public void changeMenu() {

    }

    public static void updateState(String state){
        GameMenuView.getInstance().showResult(state);
    }

    public String enterMenu(String menuName) {
//        if (menuName.equalsIgnoreCase("collection")) {
//            AppView.setCurrentScreen(CollectionMenuView.getInstance());
//            return "Entering collection menu...";
//        } else if (menuName.equalsIgnoreCase("travel log")) {
//            AppView.setCurrentScreen(TravelLogMenuView.getInstance());
//            TravelLogMenuView.getInstance().showCurrentPage();
//            return "Entering Travel Log...";
//        }
//        return "Invalid menu name!";
        return "";
    }

    @Override
    public void exitMenu() {
        App.setCurrentGame(null);
        App.getCurrentUser().getPlantBoosts().clear();
        AppView.setCurrentScreen(MainMenuView.getInstance());
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: game menu");
    }

    public void advanceTime(int count){
        System.out.println("okay");
        GameWorld game = App.getCurrentGame();
        for (int i = 0; i < count; i++){
            game.tick();
        }
    }

    public void collectSun(float x, float y){
        for (Sun sun : App.getCurrentGame().getActiveSuns()){
            if (sun.isCollected()) continue;
            if (Math.abs(sun.getX() - x) < 2 && Math.abs(sun.getY() - y) < 2){
                if (sun.getType() == SunType.RADIOACTIVE){
                    Cell[][] grid = App.getCurrentGame().getGrid();
                    sun.collect();
                    Cell sunCell = Cell.findCell(sun.getX(), sun.getY(), grid);
                    if (sunCell != null){
                        List<Cell> zombieCells = Cell.getNeighborCells(sunCell, grid, 2);
                        List<Zombie> zombies = Cell.getZombiesInCells(zombieCells);
                        for (Zombie zombie : zombies){
                            zombie.takeDamage(150, "NORMAL");
                        }
                        List<Cell> plantCells = Cell.getNeighborCells(sunCell, grid, 1);
                        for (Cell cell : plantCells){
                            if (cell.getPlant(PlantLayer.BASE) != null) cell.getPlant(PlantLayer.BASE).takeDamage(80);
                            if (cell.getPlant(PlantLayer.MAIN) != null) cell.getPlant(PlantLayer.MAIN).takeDamage(80);
                            if (cell.getPlant(PlantLayer.SHIELD) != null) cell.getPlant(PlantLayer.SHIELD).takeDamage(80);
                        }
                    }

                }
                else{
                    GameWorld game = App.getCurrentGame();
                    sun.collect();
                    game.setSun(game.getSun() + sun.getSize());
                    if (sun.getProducer() != null) sun.getProducer().getComponentSuns().remove(sun);
                    User user = App.getCurrentUser();
                    if (user != null) {
                        user.getQuestStats().addSunsCollectedToday(sun.getSize());
                        user.getQuestManager().checkAllQuests(user);
                    }
                }
                return;
            }
        }
        GameMenuView.getInstance().showResult("there is no sun in that place!");
    }

    public void collectCollectable(float x, float y, String type){
        CollectableType selectedType = null;
        for (CollectableType collectableType : CollectableType.values()) {
            if (collectableType.name().equalsIgnoreCase(type)) {
                selectedType = collectableType;
                break;
            }
        }
        if (selectedType == null){
            GameMenuView.getInstance().showResult("invalid collectable type");
            return;
        }


        for (Collectable collectable : App.getCurrentGame().getActiveCollectables()){
            if (collectable.isDead()) continue;
            if (Math.abs(collectable.getX() - x) < 2 && Math.abs(collectable.getY() - y) < 2){
                switch (collectable.getType()){
                    case POT:
                        App.getCurrentUser().setPot(App.getCurrentUser().getPot()+1);
                        GameMenuView.getInstance().showResult("pot collected. now you have " + App.getCurrentUser().getPot() + " pots.");
                        break;
                    case COIN:
                        App.getCurrentUser().setCoins(App.getCurrentUser().getCoins()+10);
                        GameMenuView.getInstance().showResult("coin collected. now you have " + App.getCurrentUser().getCoins() + " coins.");
                        break;
                    case DIAMOND:
                        App.getCurrentUser().setGems(App.getCurrentUser().getGems()+1);
                        GameMenuView.getInstance().showResult("gem collected. now you have " + App.getCurrentUser().getGems() + " gems.");
                        break;
                    case PLANT_FOOD:
                        if (App.getCurrentGame().getPlantFoods() < 3){
                            App.getCurrentGame().setPlantFoods(App.getCurrentGame().getPlantFoods()+1);
                            GameMenuView.getInstance().showResult("plant food collected. now you have " + App.getCurrentGame().getPlantFoods() + " plant foods.");
                        }
                        else {
                            GameMenuView.getInstance().showResult("not enough space!");
                        }
                        break;
                }
                collectable.collect();
                return;
            }
        }
        GameMenuView.getInstance().showResult("there is no collectable in that place!");
    }

    public void showSunAmount(){
        GameMenuView.getInstance().showResult("current sun amount: " + App.getCurrentGame().getSun());
    }

    public void cheatAddSun(int count){
        App.getCurrentGame().setSun(25*count+App.getCurrentGame().getSun());
        GameMenuView.getInstance().showResult("Cheat activated! Added " + count + " suns. ☀️");
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
//            game.getActiveZombies().clear();
            System.out.println("All zombies eliminated by nuke (fallback)!");
        }
    }

    public void plantPlant(PlantType type, float x, float y){
        PlantCard selectedCard = null;
        for (PlantCard card : App.getCurrentGame().getPlantLists()){
            if (card.getType().equals(type)){
                selectedCard = card;
                break;
            }
        }
        if (selectedCard == null){
            GameMenuView.getInstance().showResult(type + " is not in your plants!");
            return;
        }
        plantPlant(selectedCard, x, y);
    }

    private Cell findCellAt(GameWorld game, float x, float y) {
        for (Cell[] cells : game.getGrid()) {
            if (!(cells[0].getY() + App.getCellHeight() / 2 > y && cells[0].getY() - App.getCellHeight() / 2 < y)) continue;
            for (Cell cell : cells) {
                if (cell.getX() + App.getCellWidth() / 2 > x && cell.getX() - App.getCellWidth() / 2 < x) {
                    return cell;
                }
            }
        }
        return null;
    }

    public void plantPlant(PlantCard card, float x, float y){

        if (App.getCurrentGame().getSun() < card.getSunCost()){
            GameMenuView.getInstance().showResult("you haven't enough suns!");
            return;
        }
        if (!card.isReady()){
            GameMenuView.getInstance().showResult("this plant isn't ready!");
            return;
        }
        Cell selectedCell = findCellAt(App.getCurrentGame(), x, y);
        if (selectedCell == null){
            GameMenuView.getInstance().showResult("you cannot plant in that place!");
            return;
        }
        String error;
        PlantType type;
        if (card instanceof ImitatorCard imitatorCard){
            type = imitatorCard.getTargetType();
            error = selectedCell.handlePlanting(type, true);
        }
        else{
            type = card.getType();
            error = selectedCell.handlePlanting(type, App.getCurrentUser().hasBoost(type));
        }


        if (error != null) GameMenuView.getInstance().showResult(error);
        else {
            if (App.getCurrentGame().isConveyorMode()){
                App.getCurrentGame().getConveyorBelt().remove(card);
            }
            else{
                card.setReady(false);
                App.getCurrentGame().setSun(App.getCurrentGame().getSun() - card.getSunCost());
            }
        }
    }

    public void removeCooldown(){
        if (App.getCurrentGame().isConveyorMode()) return;
        for (PlantCard card : App.getCurrentGame().getPlantLists()){
            card.deactivateCooldown();
        }
    }

    public void activateCooldown(){
        if (App.getCurrentGame().isConveyorMode()) return;
        for (PlantCard card : App.getCurrentGame().getPlantLists()){
            card.setActiveCooldown();
        }
    }

    public void pluckPlant(float x, float y){
        Cell selectedCell = findCellAt(App.getCurrentGame(), x, y);
        if (selectedCell == null || !selectedCell.findAndRemovePlant()) {
            GameMenuView.getInstance().showResult("there is no plant in that place!");
        }
    }

    public void showPlantFoodsCount(){
        GameMenuView.getInstance().showResult("plant foods count: " + App.getCurrentUser().getPlantFoods());
    }

    public void feedPlant(float x, float y){
        if (App.getCurrentGame().getPlantFoods() <= 0){
            GameMenuView.getInstance().showResult("you have not any plant foods!");
            return;
        }
        Cell selectedCell = null;
        for (Cell[] cells : App.getCurrentGame().getGrid()){
            if (!(cells[0].getY() + App.getCellHeight()/2 > y && cells[0].getY() - App.getCellHeight()/2 < y)) continue;
            for (Cell cell : cells){
                if ((cell.getX() + App.getCellWidth()/2 > x && cell.getX() - App.getCellWidth()/2 < x)){
                    selectedCell = cell;
                    break;
                }
            }
        }
        if (selectedCell == null || selectedCell.findPlant() == null) {
            GameMenuView.getInstance().showResult("there is no plant in that place!");
            return;
        }
        selectedCell.findPlant().activatePlantFood();
        App.getCurrentGame().setPlantFoods(App.getCurrentGame().getPlantFoods()-1);

    }

    public void cheatAddPlantFood(){
        if (App.getCurrentGame().getPlantFoods() < 3){
            App.getCurrentGame().setPlantFoods(App.getCurrentGame().getPlantFoods()+1);
        }
    }

    public void showMap(){
        int currentWaveNum = 1;
        int totalWaves = 1;
        NormalMechanic normal = App.getCurrentGame().getMechanic(NormalMechanic.class);
        if (normal != null && normal.getWaveManager() != null) {
            WaveManager wm = normal.getWaveManager();
            if (wm.getCurrentWave() != null) {
                currentWaveNum = wm.getCurrentWave().getWaveNumber();
            } else {
                currentWaveNum = wm.getCurrentWaveIndex() + 1;
            }
            totalWaves = wm.getTotalWavesCount();
        }

        System.out.println("==================================================================================================");
        System.out.printf(" WAVE: %d/%d  |  SUN: %d ☀️  |  PLANT FOOD: %d ⚡  |  STATUS: %s 🎮%n",
                currentWaveNum, totalWaves, App.getCurrentGame().getSun(), App.getCurrentGame().getPlantFoods(), App.getCurrentGame().getState());
        System.out.println("==================================================================================================");
        for (int y = 0; y < App.getCurrentGame().getGrid().length; y++) {
            String mowerSymbol = App.getCurrentGame().getLawnMowerManager().getMowers().get(y).isAlive() ? "[🚜]" : "[❌]";
            System.out.printf("Row %d %s | ", y+1, mowerSymbol);

            for (int x = 0; x < App.getCurrentGame().getGrid()[0].length; x++) {
                Cell cell = App.getCurrentGame().getGrid()[y][x];

                String terrainSymbol = cell.getTerrain().getTerminalSymbol(); // مقدار پیش‌فرض

                if (cell.hasObstacle()) {
                    Obstacle obs = cell.getObstacle();
                    if (obs instanceof Grave) {
                        Grave grave = (Grave) obs;
                        switch (grave.getType()) {
                            case SUN:
                                terrainSymbol = "🪦☀";
                                break;
                            case PLANT_FOOD:
                                terrainSymbol = "🪦⚡";
                                break;
                            default:
                                terrainSymbol = "🪦";
                                break;
                        }
                    } else if (obs instanceof OctopusObstacle) {
                        terrainSymbol = "🐙";
                    } else if (obs instanceof IceBlock) {
                        terrainSymbol = "🧊";
                    } else {
                        terrainSymbol = "🪨";
                    }
                }

                String plantSymbol = "    ";
                if (!cell.isEmpty()) {
                    Plant plant = cell.getPlant();
                    if (plant.isCat()) {
                        plantSymbol = "🐱 ";
                    } else {
                        plantSymbol = plant.getType().getSymbol();
                    }
                }


                String zombieString = "       ";
                List<Zombie> zombiesInCell = Cell.getZombiesInCell(cell);
                if (!zombiesInCell.isEmpty()) {
                    Zombie firstZombie = zombiesInCell.getFirst();
                    zombieString = String.format("Z(%.1f)", firstZombie.getX());
                }

                System.out.printf("[ %s | %-4s | %-7s ] | ", terrainSymbol, plantSymbol.trim(), zombieString.trim());
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("==================================================================================================");
    }

    public void showPlantsStatus(){
        if (App.getCurrentGame().isConveyorMode()) return;
        for (PlantCard card : App.getCurrentGame().getPlantLists()){
            String ticksRemaining = card.isReady() ? "" : " | ticks remaining: " + (card.getMaxCooldownTicks() - card.getCurrentCooldownTicks());
            GameMenuView.getInstance().showResult(card.getType().name() + " | Cost: " + card.getSunCost() + " | is ready: " + card.isReady() + ticksRemaining);
        }
    }

    public void showTileStatus(float x, float y){
        Cell selectedCell = null;
        for (Cell[] cells : App.getCurrentGame().getGrid()){
            if (!(cells[0].getY() + App.getCellHeight()/2 > y && cells[0].getY() - App.getCellHeight()/2 < y)) continue;
            for (Cell cell : cells){
                if ((cell.getX() + App.getCellWidth()/2 > x && cell.getX() - App.getCellWidth()/2 < x)){
                    selectedCell = cell;
                    break;
                }
            }
        }
        if (selectedCell == null) {
            GameMenuView.getInstance().showResult("there is no tile in that place!");
            return;
        }
        GameMenuView.getInstance().showResult("plants in this tile:");
        for (PlantLayer layer : PlantLayer.values()){
            Plant p = selectedCell.getPlant(layer);
            if (p != null){
                GameMenuView.getInstance().showResult(p.getType().name() + " | health: " + p.getHealth() + " | damage: " + p.getDamage());
            }
        }
        GameMenuView.getInstance().showResult("zombies in this tile:");
        for (Zombie zombie : Cell.getZombiesInCells(List.of(selectedCell))){
            GameMenuView.getInstance().showResult(App.getArmoredZombieName(zombie.getSpecificName()) + " | health: " + zombie.getHealth() + " | damage: "+ zombie.getDamage());
        }
    }

    public void zombieInfo() {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()) {
            GameMenuView.getInstance().showResult(App.getArmoredZombieName(zombie.getSpecificName()) + ":");
            GameMenuView.getInstance().showResult("    position: (" + zombie.getX() + ", " + zombie.getY() + ")");
            GameMenuView.getInstance().showResult("    health: " + zombie.getHealth());
            if (zombie instanceof ArmoredZombie armoredZombie) {
                GameMenuView.getInstance().showResult("    armor health: " + handleArmor(armoredZombie));
            } else {
                GameMenuView.getInstance().showResult("    armor health: none");
            }
            GameMenuView.getInstance().showResult("    effects:");
            if (zombie.getDisabledTicksRemaining() > 0)
                GameMenuView.getInstance().showResult("        stunned " + zombie.getDisabledTicksRemaining());
            if (zombie.getFreezedTicksRemaining() > 0)
                GameMenuView.getInstance().showResult("        frozen " + zombie.getFreezedTicksRemaining());
            if (zombie.getSlowTicksRemaining() > 0)
                GameMenuView.getInstance().showResult("        slowed " + zombie.getSlowTicksRemaining());
            GameMenuView.getInstance().showResult("");
        }
    }

    private String handleArmor(ArmoredZombie armoredZombie) {
        if (armoredZombie.getArmorHealth() <= 0) {
            return "0 (broken)";
        }
        if (armoredZombie.getSpecificName().equalsIgnoreCase("ZombieDarkArmor3")) {
            if (armoredZombie.getArmorHealth() > 1600) {
                return "crown: " + (armoredZombie.getArmorHealth() - 1600) + ", shoulderArmor: 1600";
            } else {
                return "shoulderArmor: " + armoredZombie.getArmorHealth();
            }
        }
        // برای سایر زامبی‌های زره‌دار
        String type = armoredZombie.getArmorTypes().isEmpty() ? "unknown" : armoredZombie.getArmorTypes().get(0);
        return type + ": " + armoredZombie.getArmorHealth();
    }

    public void cheatSpawnZombie(String type, float x, float y){
        Zombie zombie;
        try{
            zombie = new ZombieFactory().createZombie(type);
        }
        catch (Exception e){
            GameMenuView.getInstance().showResult("invalid zombie type!");
            return;
        }

        float newY = -1;
        for (Cell[] cells : App.getCurrentGame().getGrid()){
            if (cells[0].getY() + App.getCellHeight()/2 >= y && cells[0].getY() - App.getCellHeight()/2 <= y){
                newY = cells[0].getY();
            }
        }
        if (x < 0 || x > App.getCellWidth()*9 || newY == -1){
            GameMenuView.getInstance().showResult("you cannot spawn zombie in that place!");
            return;
        }

        // تنظیم موقعیت زامبی
        zombie.setX(x);
        zombie.setY(newY);

        App.getCurrentGame().getActiveZombies().add(zombie);
        GameMenuView.getInstance().showResult("Spawned " + type + " at (" + x + ", " + newY + ")");
    }

    public void startZombieWaves(){

    }

    //miniGames
    public void breakVase(int row, int col){
        GameWorld game = App.getCurrentGame();
        if (!(game instanceof VaseBreakerLevel level)){
            GameMenuView.getInstance().showResult("this command is only available in Vase Breaker!");
            return;
        }
        GameMenuView.getInstance().showResult(level.breakVaseAt(row, col));
    }

    public void pickUpSeed(int row, int col) {
        GameWorld game = App.getCurrentGame();
        if (!(game instanceof VaseBreakerLevel level)) {
            GameMenuView.getInstance().showResult("this command is only available in Vase Breaker!");
            return;
        }
        GameMenuView.getInstance().showResult(level.pickUpSeedAt(row, col));
    }

    public void plantHeldSeed(float x, float y) {
        GameWorld game = App.getCurrentGame();
        if (!(game instanceof VaseBreakerLevel level)) {
            GameMenuView.getInstance().showResult("this command is only available in Vase Breaker!");
            return;
        }

        PlantType heldType = level.getHeldSeed();
        if (heldType == null) {
            GameMenuView.getInstance().showResult("You are not holding any seed!");
            return;
        }

        Cell selectedCell = findCellAt(game, x, y);
        if (selectedCell == null) {
            GameMenuView.getInstance().showResult("you cannot plant in that place!");
            return;
        }

        String error = selectedCell.handlePlanting(heldType);
        if (error != null) {
            GameMenuView.getInstance().showResult(error);
        } else {
            level.clearHeldSeed();
            GameMenuView.getInstance().showResult("Planted " + heldType.name() + "!");
        }
    }

    public void placeZombie(String type, float x, float y){
        GameWorld world = App.getCurrentGame();
        if(!(world instanceof IZombieLevel level)){
            GameMenuView.getInstance().showResult("this command is only available in I Zombie!");
            return;
        }
        Zombie zombie;
        try {
            zombie = new ZombieFactory().createZombie(type);
        } catch (Exception e) {
            GameMenuView.getInstance().showResult("invalid zombie");
            return;
        }
        boolean allowed = level.getAvailableZombies().stream()
                .anyMatch(z -> z.getName().name().equalsIgnoreCase(type));
        if (!allowed) {
            GameMenuView.getInstance().showResult("u dont have this zombie");
            return;
        }
        GameMenuView.getInstance().showResult(level.placeZombie(zombie, x, y));
    }

    public void swapPlants(int row1, int col1, int row2, int col2){
        GameWorld game = App.getCurrentGame();
        BeghouledMechanics mechanics = game.getMechanic(BeghouledMechanics.class);
        if (mechanics == null){
            GameMenuView.getInstance().showResult("this command is only available in Beghouled!");
            return;
        }

        GridPosition a = new GridPosition(row1, col1);
        GridPosition b = new GridPosition(row2, col2);

        String error = mechanics.trySwap(game, a, b);
        if (error != null){
            GameMenuView.getInstance().showResult(error);
        } else {
            GameMenuView.getInstance().showResult("Swap successful! Score: " + mechanics.getScore() + "/" + mechanics.getTargetScore());
        }
    }

    public void upgradePlant(PlantType type){
        GameWorld game = App.getCurrentGame();
        BeghouledMechanics mechanics = game.getMechanic(BeghouledMechanics.class);
        if (mechanics == null){
            GameMenuView.getInstance().showResult("this command is only available in Beghouled!");
            return;
        }

        String error = mechanics.upgradePlant(game, type);
        if (error != null){
            GameMenuView.getInstance().showResult(error);
        } else {
            GameMenuView.getInstance().showResult("Upgrade successful! Remaining sun: " + game.getSun());
        }
    }

    public void throwBowlingBall(BowlingBallType type, float x, float y) {
        GameWorld game = App.getCurrentGame();
        BowlingMechanics mechanics = game.getMechanic(BowlingMechanics.class);
        if (mechanics == null) {
            GameMenuView.getInstance().showResult("this command is only available in Bowling!");
            return;
        }
        GameMenuView.getInstance().showResult(mechanics.throwBall(game, type, x, y));
    }


}
