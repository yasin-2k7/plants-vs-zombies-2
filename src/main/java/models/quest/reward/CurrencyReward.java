package models.quest.reward;

import models.core.User;

public class CurrencyReward implements Reward{
    private int coins;
    private int gems;

    public CurrencyReward(){}

    @Override
    public void apply(User user){}
}
