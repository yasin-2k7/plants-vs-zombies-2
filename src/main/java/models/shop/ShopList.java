package models.shop;

import models.core.App;
import models.core.User;
import models.enums.PlantType;
import java.util.ArrayList;
import java.util.List;

public class ShopList {
    private List<ShopItem> permanentItems;
    private DailyOffer dailyOffer;

    public ShopList() {
        permanentItems = new ArrayList<>();
        permanentItems.add(new ShopItem("Unlock Pot", 2000, 0, 20, true));

        // نمونه پیشنهاد روزانه (در سیستم واقعی باید رندوم باشد)
        dailyOffer = new DailyOffer(PlantType.PEASHOOTER, 1000);
    }

    public void buy(String itemName, PlantType plantType) {
        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        if (itemName.equalsIgnoreCase("Unlock Pot")) {
            ShopItem potItem = permanentItems.get(0);
            if (!potItem.isAffordable(user.getCoins(), user.getGems())) {
                System.out.println("Not enough coins! Need " + potItem.getCoinCost() + " coins.");
                return;
            }
            boolean success = user.getGreenhouse().unlockFirstLockedPot();
            if (success) {
                user.spendCoins(potItem.getCoinCost());
                System.out.println("Pot unlocked successfully!");
            }
            return;
        }

        if (dailyOffer != null && dailyOffer.getName().equals(itemName) && dailyOffer.isAvailableToday()) {
            if (!dailyOffer.isAffordable(user.getCoins(), user.getGems())) {
                System.out.println("Not enough money for daily offer!");
                return;
            }
            user.spendCoins(dailyOffer.getCoinCost());
            user.unlockPlant(dailyOffer.getPlantType());
            dailyOffer.setPurchased(true);
            System.out.println(dailyOffer.getPlantType() + " unlocked permanently!");
        }
    }

    public DailyOffer getDailyOffer() { return dailyOffer; }
    public List<ShopItem> getPermanentItems() { return permanentItems; }
}
