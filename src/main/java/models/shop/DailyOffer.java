package models.shop;

import models.plant.Plant;

import java.time.LocalDate;

public class DailyOffer extends ShopItem{
    private Class<? extends Plant> plantType;
    private LocalDate offerDate;
    private boolean isPurchased;

    public boolean isAvailableToday() {
        return false;
    }

    public void refresh() {

    }

    public boolean canPurchase() {
        return false;
    }
}
