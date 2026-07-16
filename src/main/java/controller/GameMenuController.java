package controller;

import models.core.App;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.enums.Zombies;
import models.plant.Plant;
import models.plant.card.PlantCard;
import models.world.Cell;
import models.world.GameWorld;
import models.world.Sun;
import models.world.SunType;
import models.world.mechanics.NormalMechanic;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;
import models.zombie.zombiesType.ArmoredZombie;
import view.terminalView.*;

import java.util.List;

public class GameMenuController implements MenuController {
    @Override
    public void changeMenu() {

    }

    public static void updateState(String state){
        GameMenuView.getInstance().showResult(state);
    }

    public String enterMenu(String menuName) {
        if (menuName.equalsIgnoreCase("collection")) {
            AppView.setCurrentScreen(CollectionMenuView.getInstance());
            return "Entering collection menu...";
        }
        return "Invalid menu name!";
    }

    @Override
    public void exitMenu() {
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
                }
                return;
            }
        }
        GameMenuView.getInstance().showResult("there is no sun in that place!");
    }

    public void showSunAmount(){
        GameMenuView.getInstance().showResult("current sun amount: " + App.getCurrentGame().getSun());
    }

    public void cheatAddSun(int count){
        App.getCurrentGame().setSun(25*count+App.getCurrentGame().getSun());
    }

    public void releaseTheNuke(){
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            zombie.die();
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

    public void plantPlant(PlantCard card, float x, float y){
        if (App.getCurrentGame().getSun() < card.getSunCost()){
            GameMenuView.getInstance().showResult("you haven't enough suns!");
            return;
        }
        if (!card.isReady()){
            GameMenuView.getInstance().showResult("this plant isn't ready!");
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
        if (selectedCell == null){
            GameMenuView.getInstance().showResult("you cannot plant in that place!");
            return;
        }

        String error = selectedCell.handlePlanting(card.getType());
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
        if (selectedCell == null || !selectedCell.findAndRemovePlant()) {
            GameMenuView.getInstance().showResult("there is no plant in that place!");
        }
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

    // how to get waves?
    public void showMap(){
        System.out.println("==================================================================================================");
        System.out.printf(" WAVE: %d/%d  |  SUN: %d ☀️  |  PLANT FOOD: %d ⚡  |  STATUS: %s 🎮%n",
                5, 7, App.getCurrentGame().getSun(), App.getCurrentGame().getPlantFoods(), App.getCurrentGame().getState());
        System.out.println("==================================================================================================");
        for (int y = 0; y < App.getCurrentGame().getGrid().length; y++) {
            String mowerSymbol = App.getCurrentGame().getLawnMowerManager().getMowers().get(y).isAlive() ? "[🚜]" : "[❌]";
            System.out.printf("Row %d %s | ", y+1, mowerSymbol);

            for (int x = 0; x < App.getCurrentGame().getGrid()[0].length; x++) {
                Cell cell = App.getCurrentGame().getGrid()[y][x];

                String terrainSymbol = cell.getTerrain().getTerminalSymbol(); // '.' , '~' , 'I' , 'O'
                if (cell.hasObstacle()) {
                    terrainSymbol = "O";
                }

                String plantSymbol = "    ";
                if (!cell.isEmpty()) {
                    plantSymbol = (cell.getPlant().getType().getSymbol());
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
            GameMenuView.getInstance().showResult(zombie.getName().name() + " | health: " + zombie.getHealth() + " | damage: "+ zombie.getDamage());
        }
    }

    public void zombieInfo(){
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()){
            GameMenuView.getInstance().showResult(zombie.getName().name() + ":");
            GameMenuView.getInstance().showResult("position: (" + zombie.getX() + ", " + zombie.getY() + ")");
            GameMenuView.getInstance().showResult("health: " + zombie.getHealth());
            GameMenuView.getInstance().showResult("armor health: " + (zombie instanceof ArmoredZombie? ((ArmoredZombie)zombie).getArmorHealth() : ""));
            GameMenuView.getInstance().showResult("effects:");
            if (zombie.getDisabledTicksRemaining() > 0) GameMenuView.getInstance().showResult("stunned " + zombie.getDisabledTicksRemaining());
            if (zombie.getFreezedTicksRemaining() > 0) GameMenuView.getInstance().showResult("frozen " + zombie.getFreezedTicksRemaining());
            if (zombie.getSlowTicksRemaining() > 0) GameMenuView.getInstance().showResult("slowed " + zombie.getSlowTicksRemaining());
            GameMenuView.getInstance().showResult("");
        }
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

        App.getCurrentGame().getActiveZombies().add(zombie);
    }

    public void startZombieWaves(){

    }


}
