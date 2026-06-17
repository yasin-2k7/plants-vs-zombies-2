package models.shop;

import models.plant.Plant;

import java.util.List;

public class ShopList {
    private List<ShopItem> permanentItems;
    private DailyOffer dailyOffer;

    public List<ShopItem> getPermanentItems() {
        return null;
    }

    public DailyOffer getDailyOffer() {
        return null;
    }



    public void buy(int itemId, int count,
                    Class<? extends Plant> plantType) {

    }
}
