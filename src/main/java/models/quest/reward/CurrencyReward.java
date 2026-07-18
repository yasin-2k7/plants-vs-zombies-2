package models.quest.reward;

import models.core.User;

public class CurrencyReward implements Reward {
    private int coins;
    private int gems;

    public CurrencyReward(int coins, int gems) {
        this.coins = coins;
        this.gems = gems;
    }

    @Override
    public void apply(User user) {
        if (coins > 0) {
            user.addCoins(coins);
        }
        if (gems > 0) {
            user.addGems(gems);
        }
    }
}