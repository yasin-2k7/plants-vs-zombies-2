package com.pvz2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pvz2.models.core.App;
import com.pvz2.network.onlineIZombie.ZombieCard;

import java.util.function.Consumer;

public class ZombieCardView extends Stack {
    private boolean active, lock;
    private int brainCost;
    private String zombieName;
    private Consumer<ZombieCardView> onClick;
    private CooldownOverlay overlay;
    private ZombieCard card;
    private Image selectedImg;

    public ZombieCardView(boolean active, boolean lock, int brainCost, String zombieName) {
        this.active = active;
        this.lock = lock;
        this.brainCost = brainCost;
        this.zombieName = zombieName;
        this.overlay = new CooldownOverlay(PlantCardView.createSolidColor(Color.WHITE));
        this.setTouchable(Touchable.enabled);
        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClick != null) onClick.accept(ZombieCardView.this);
            }
        });
        build();
    }

    public void build() {
        add(new Image(App.getGameApp().textureBank.region(
            ZombiesTable.getZombiesPicAddress().get(zombieName))));

        Table costWrapper = new Table();
        costWrapper.bottom().right();
        costWrapper.add(new Label(Integer.toString(brainCost), App.getGameApp().skin,
            "medium_outline")).padBottom(5).padRight(10);
        this.add(costWrapper);

        overlay.setProgress(active ? 0 : 1);
        this.add(overlay);

        if (lock) {
            Table lockTable = new Table();
            lockTable.center().right();
            lockTable.add(new Image(App.getGameApp().textureBank.region(
                "IMAGE_UI_CARDS_LOCK_MEDIUM"))).size(30, 40).pad(10);
            this.add(lockTable);
        }

        selectedImg = new Image(App.getGameApp().textureBank.region("IMAGE_UI_PACKETS_SELECT"));
        this.add(selectedImg);
        selectedImg.setVisible(false);
    }

    public void update() {
        if (card == null) return;
        if (card.isReady()) {
            this.active = true;
            overlay.setProgress(0);
        } else {
            this.active = false;
            float remainingFraction = (card.getMaxCooldownTicks() - card.getCurrentCooldownTicks())
                / card.getMaxCooldownTicks();
            overlay.setProgress(remainingFraction);
        }
    }

    public String getZombieName() { return zombieName; }
    public boolean isActive() { return active; }
    public boolean isLock() { return lock; }
    public int getBrainCost() { return brainCost; }
    public void setActive(boolean active) { this.active = active; }
    public void setSelectedState(boolean state) { selectedImg.setVisible(state); }
    public void setClickMethod(Consumer<ZombieCardView> onClick) { this.onClick = onClick; }
    public void setCard(ZombieCard card) { this.card = card; }
    public ZombieCard getCard() { return card; }
}
