package models.shop;

public class ShopItem {
    private String name;
    private int coinCost;
    private int diamondCost;
    private int maxCapacity;
    private boolean isPermanent;

    public boolean isAffordable(int coins, int diamonds) {
        return false;
    }

    public boolean isAtCapacity(int currentCount) {
        return false;
    }

    public boolean isPermanent() {
        return false;
    }
}
