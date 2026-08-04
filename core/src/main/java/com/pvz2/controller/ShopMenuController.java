package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.shop.ShopList;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.GreenhouseMenuView;

import java.util.ArrayList;
import java.util.List;

public class ShopMenuController implements MenuController {
    private ShopList shopList = new ShopList();

    @Override
    public void changeMenu() {
    }

    @Override
    public void exitMenu() {
        AppView.currentScreen = GreenhouseMenuView.getInstance();
    }

    public List<String> showShopList() {
        List<String> output = new ArrayList<>();
        output.add("--- Permanent Items ---");
        shopList.getPermanentItems().forEach(i ->
                output.add(i.getName() + ": " + i.getCoinCost() + " coins / " + i.getDiamondCost() + " gems")
        );
        return output;
    }

    public String showDailyOffer() {
        if (shopList.getDailyOffer().isAvailableToday()) {
            return "Daily Offer: " + shopList.getDailyOffer().getName() +
                    " - " + shopList.getDailyOffer().getCoinCost() + " coins";
        } else {
            return "No daily offer available today.";
        }
    }

    public String buyItem(String itemId, int count, String plantTypeName) {
        User user = App.getCurrentUser();
        if (user == null) {
            return "Error: No user logged in.";
        }

        PlantType type = null;
        if (plantTypeName != null && !plantTypeName.isEmpty()) {
            try {
                type = PlantType.valueOf(plantTypeName.toUpperCase());
            } catch (IllegalArgumentException e) {
                return "Error: Invalid plant type.";
            }
        }

        return shopList.buy(itemId, type, count);
    }

    public void showCurrentMenu() {
        GameMenuView.getInstance().showResult("Current menu: shop menu");
    }
}
