package controller;

import models.core.App;
import models.core.User;
import models.enums.PlantType;
import models.shop.ShopList;

import java.util.ArrayList;
import java.util.List;

public class ShopMenuController implements MenuController {
    private ShopList shopList = new ShopList();

    @Override
    public void changeMenu() {
    }

    public List<String> showShopList() {
        List<String> output = new ArrayList<>();
        output.add("--- Permanent Items ---");
        shopList.getPermanentItems().forEach(i ->
                output.add(i.getName() + ": " + i.getCoinCost() + " coins / " + i.getDiamondCost() + " gems")
        );
        output.add("--- Daily Offer ---");
        if (shopList.getDailyOffer().isAvailableToday()) {
            output.add(shopList.getDailyOffer().getName() + " costs " +
                    shopList.getDailyOffer().getCoinCost() + " coins");
        } else {
            output.add("No daily offer available today.");
        }
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
}