package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.plant.card.PlantCardFactory;
import com.pvz2.models.plant.factory.PlantFactory;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.ZombieFactory;
import com.pvz2.view.CollectionMenuScreen;
import com.pvz2.view.MenuScreen;
import com.pvz2.view.PlantsCollectionMenuScreen;


public class CollectionMenuController implements MenuController {
    MenuScreen lastScreen;
    CollectionMenuScreen screen;
    private PlantsCollectionMenuScreen plantsCollectionMenuScreen;

    public CollectionMenuController(MenuScreen lastScreen, CollectionMenuScreen screen) {
        this.lastScreen = lastScreen;
        this.screen = screen;
    }

    @Override
    public void changeMenu() {

    }

    @Override
    public void exitMenu() {
        screen.fadeAndSwitchScreen(lastScreen);
    }



    public int[] showPlant(PlantType type) {
        if (type == PlantType.IMITATER){
            return new int[]{0,0,0,0};
        }
        Plant plant = PlantFactory.createPlant(type, 0, 0, null);
        PlantCard card = PlantCardFactory.createCard(type,
            App.getCurrentUser().getUnlockedPlantsLevels().getOrDefault(type,1));
        int[] result = new int[4];
        result[0] = card.getSunCost();
        result[1] = card.getMaxCooldownTicks()/10;
        result[2] = plant.getHealth();
        result[3] = plant.getDamage();
        return result;
    }

    public void showZombie(String name) {
        String zombieName = App.getZombieId(name);
        Zombie zombie;
        try {
            zombie = new ZombieFactory().createZombie(zombieName);
        } catch (Exception e) {
//            CollectionMenuView.getInstance().showResult("Zombie doesn't exist.");
            //needs edit
            return;
        }

        if (!App.getCurrentUser().getShowedZombies().containsKey(name)) {
//            CollectionMenuView.getInstance().showResult("Zombie doesn't exist.");
            //needs edit
            return;
        }

        if (!App.getCurrentUser().getShowedZombies().get(name)) {
//            CollectionMenuView.getInstance().showResult("Zombie is locked.");
            //needs edit
            return;
        }

//        CollectionMenuView.getInstance().showResult(name + "\n"
//                + "Health: " + zombie.getHealth() + "\n"
//                + "Damage: " + zombie.getDamage() / 10 + "\n"
//                + "Speed: " + (int) (zombie.getSpeed() * 15));
        //needs edit

    }


    public boolean upgradePlant(PlantType type) {
        int plantLevel = App.getCurrentUser().getUnlockedPlantsLevels().get(type);
        if (plantLevel == 4) {
            if (plantsCollectionMenuScreen != null){
                plantsCollectionMenuScreen.addToast("Error", "This plant has max level.");
            }
            return false;
        }
        int currentSeedPacket = App.getCurrentUser().getSeedPacketsCount(type);
        int neededSeedPacket = plantLevel * 10;
        int currentCoin = App.getCurrentUser().getCoins();
        int neededCoin = plantLevel * 100;
        if (currentSeedPacket < neededSeedPacket) {
            if (plantsCollectionMenuScreen != null){
                plantsCollectionMenuScreen.addToast("Error", "You need " + neededSeedPacket +
                    " seed packets.");
            }
            return false;
        }
        if (currentCoin < neededCoin) {
            if (plantsCollectionMenuScreen != null){
                plantsCollectionMenuScreen.addToast("Error", "You need " + neededCoin + " coins.");
            }
            return false;
        }
        App.getCurrentUser().getUnlockedPlantsLevels().put(type, plantLevel + 1);
        App.getCurrentUser().spendCoins(neededCoin);
        App.getCurrentUser().getSeedPackets().put(type, currentSeedPacket - neededSeedPacket);
        if (plantsCollectionMenuScreen != null){
            plantsCollectionMenuScreen.addToast("Error", "plant " + type + " upgraded.");
        }
        return true;
    }

    public boolean purchasePlant(PlantType type) {
        if (App.getCurrentUser().getCoins() < 2000) {
            if (plantsCollectionMenuScreen != null){
                plantsCollectionMenuScreen.addToast("Error", "you need 2000 coins.");
            }
            return false;
        }
        App.getCurrentUser().spendCoins(2000);
        App.getCurrentUser().unlockPlant(type);
        if (plantsCollectionMenuScreen != null){
            plantsCollectionMenuScreen.addToast("Purchased successfully",
                "Now you have " + type.name() + ".");
        }
        App.getCurrentUser().notifyPlantUnlock(type.name());
        return true;
    }

    public void setPlantsCollectionMenuScreen(PlantsCollectionMenuScreen plantsCollectionMenuScreen) {
        this.plantsCollectionMenuScreen = plantsCollectionMenuScreen;
    }
}
