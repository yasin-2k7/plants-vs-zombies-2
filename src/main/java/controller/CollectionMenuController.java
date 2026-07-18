package controller;

import models.core.App;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.PlantFactory;
import models.plant.card.PlantCard;
import models.plant.card.PlantCardFactory;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;
import view.terminalView.AppView;
import view.terminalView.ChapterMenuView;
import view.terminalView.CollectionMenuView;
import view.terminalView.GameMenuView;

public class CollectionMenuController implements MenuController{

    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        AppView.setCurrentScreen(ChapterMenuView.getInstance());
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: collection menu");
    }

    public void showPlants(){
        for (PlantType type : App.getCurrentUser().getUnlockedPlantsLevels().keySet()){
            CollectionMenuView.getInstance().showResult(type.name());
        }
    }

    public void showAllPlants(){
        for (PlantType type : PlantType.values()){
            CollectionMenuView.getInstance().showResult(type.name());
        }
    }

    public void showZombies(){
        for (String name : App.getCurrentUser().getShowedZombies().keySet()){
            if (App.getCurrentUser().getShowedZombies().get(name)) CollectionMenuView.getInstance().showResult(name);
        }
    }

    public void showAllZombies(){
        for (String name : App.getCurrentUser().getShowedZombies().keySet()){
            CollectionMenuView.getInstance().showResult(name);
        }
    }

    public void showPlant(PlantType type){
        Plant plant = PlantFactory.createPlant(type, 0, 0, null);
        PlantCard card = PlantCardFactory.createCard(type, App.getCurrentUser().getUserLevel());
        CollectionMenuView.getInstance().showResult(type.name() + "\n"
        + "Health: " + plant.getHealth() + "\n"
        + "Damage: " + plant.getDamage() + "\n"
        + "Sun Cost: " + card.getSunCost());
    }

    public void showZombie(String name){
        String zombieName = App.getZombieId(name);
        Zombie zombie;
        try{
            zombie = new ZombieFactory().createZombie(zombieName);
        } catch (Exception e) {
            CollectionMenuView.getInstance().showResult("Zombie doesn't exist.");
            return;
        }

        if (!App.getCurrentUser().getShowedZombies().containsKey(name)){
            CollectionMenuView.getInstance().showResult("Zombie doesn't exist.");
            return;
        }

        if (!App.getCurrentUser().getShowedZombies().get(name)){
            CollectionMenuView.getInstance().showResult("Zombie is locked.");
            return;
        }

        CollectionMenuView.getInstance().showResult(name + "\n"
                + "Health: " + zombie.getHealth() + "\n"
                + "Damage: " + zombie.getDamage()/10 + "\n"
                + "Speed: " + (int) (zombie.getSpeed()*15));

    }


    public void upgradePlant(PlantType type){
        if (!App.getCurrentUser().getUnlockedPlantsLevels().containsKey(type)){
            CollectionMenuView.getInstance().showResult("you have not this plant.");
            return;
        }
        int plantLevel = App.getCurrentUser().getUnlockedPlantsLevels().get(type);
        if (plantLevel == 4){
            CollectionMenuView.getInstance().showResult("this plant has max level.");
            return;
        }
        int currentSeedPacket = App.getCurrentUser().getSeedPacketsCount(type);
        int neededSeedPacket = plantLevel*10;
        int currentCoin = App.getCurrentUser().getCoins();
        int neededCoin = plantLevel*100;
        if (currentSeedPacket < neededSeedPacket){
            CollectionMenuView.getInstance().showResult("you need " + neededSeedPacket + " seed packets.");
            CollectionMenuView.getInstance().showResult("current seed packets: ." + currentSeedPacket);
            return;
        }
        if (currentCoin < neededCoin){
            CollectionMenuView.getInstance().showResult("you need " + neededCoin + " coins.");
            CollectionMenuView.getInstance().showResult("current coins: ." + currentCoin);
            return;
        }
        App.getCurrentUser().getUnlockedPlantsLevels().put(type, plantLevel+1);
        App.getCurrentUser().spendCoins(neededCoin);
        App.getCurrentUser().getSeedPackets().put(type, currentSeedPacket-neededSeedPacket);
        CollectionMenuView.getInstance().showResult("plant " + type + " upgraded.");
    }

    public PlantType getPlantType(String type){
        for (PlantType plantType : PlantType.values()){
            if (plantType.name().equalsIgnoreCase(type)){
                return plantType;
            }
        }
        CollectionMenuView.getInstance().showResult("Plant doesn't exist.");
        return null;
    }

    public void purchasePlant(PlantType type){
        if (App.getCurrentUser().getUnlockedPlantsLevels().containsKey(type)){
            CollectionMenuView.getInstance().showResult("you already have this plant.");
            return;
        }
        if (App.getCurrentUser().getCoins() < 2000){
            CollectionMenuView.getInstance().showResult("you need 2000 coins.");
            CollectionMenuView.getInstance().showResult("current coins: ." + App.getCurrentUser().getCoins());
            return;
        }
        App.getCurrentUser().spendCoins(2000);
        App.getCurrentUser().getUnlockedPlantsLevels().put(type, 1);
        CollectionMenuView.getInstance().showResult("plant " + type + " purchased.");
        App.getCurrentUser().notifyPlantUnlock(type.name());
    }
}
