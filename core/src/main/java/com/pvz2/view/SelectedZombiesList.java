package com.pvz2.view;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pvz2.network.onlineIZombie.ZombieCard;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectedZombiesList extends Table {
    private final int cardWidth, cardHeight;
    private final Consumer<ZombieCardView> cardClickMethod;
    private final List<ZombieCardView> zombieCardViewList = new ArrayList<>();

    public SelectedZombiesList(int cardWidth, int cardHeight, Consumer<ZombieCardView> cardClickMethod) {
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.cardClickMethod = cardClickMethod;
    }

    public void build(List<ZombieCard> zombieCards) {
        this.clear();
        zombieCardViewList.clear();

        for (ZombieCard card : zombieCards) {
            ZombieCardView view = new ZombieCardView(card.isReady(), false, card.getBrainCost(), card.getType());
            view.setCard(card);
            view.setClickMethod(cardClickMethod);
            zombieCardViewList.add(view);
            this.add(view).size(cardWidth, cardHeight);
        }
    }

    public void updateCards() {
        for (ZombieCardView view : zombieCardViewList) {
            view.update();
        }
    }

    public boolean hasZombie(String zombieName) {
        return zombieCardViewList.stream().anyMatch(v -> v.getZombieName().equals(zombieName));
    }

    public List<ZombieCardView> getZombieCardViewList() {
        return zombieCardViewList;
    }
}
