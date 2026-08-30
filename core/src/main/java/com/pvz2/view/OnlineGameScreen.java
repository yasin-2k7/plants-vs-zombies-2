package com.pvz2.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pvz2.Main;
import com.pvz2.network.onlineIZombie.ClientGameController;
import com.pvz2.network.onlineIZombie.messages.ReactionCategory;

public class OnlineGameScreen extends MenuScreen {
    ClientGameController controller;

    private ReactionPanel reactionPanel;
    private Container<ReactionPanel> reactionPanelContainer;
    private Table opponentBubbleLayer;

    public OnlineGameScreen(Main game, ClientGameController controller) {
        super(game);
        this.controller = controller;
        controller.setActionResultListener((success, message) ->
            addToast(success ? "Info" : "Error", message, !success));
        controller.setMatchOverListener((winner, msg) -> showEndScreen(winner, msg));
        controller.setReactionListener(r -> showOpponentReaction(r.category, r.index));
    }

    @Override
    protected void buildUI() {
        buildReactionUI();
    }

    private void buildReactionUI() {
        reactionPanel = new ReactionPanel(game, skin, this::sendReaction);
        reactionPanelContainer = new Container<>(reactionPanel);
        reactionPanelContainer.size(300, 260);
        reactionPanelContainer.setVisible(false);

        ImageButton toggleBtn = MainMenuScreen.createImageButton(
            "IMAGE_UI_MAINMENU_EDIT_BTN_PRESSED",
            "IMAGE_UI_MAINMENU_EDIT_BTN_NORMAL",
            game.textureBank
        );
        toggleBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleReactionPanel();
            }
        });

        Table dock = new Table();
        dock.setFillParent(true);
        dock.bottom().right().pad(20);
        dock.add(reactionPanelContainer).padBottom(10).row();
        dock.add(toggleBtn);
        mainStack.add(dock);

        opponentBubbleLayer = new Table();
        opponentBubbleLayer.setFillParent(true);
        opponentBubbleLayer.top().left().pad(20);
        mainStack.add(opponentBubbleLayer);
    }

    private void toggleReactionPanel() {
        if (reactionPanelContainer.isVisible()) {
            reactionPanelContainer.addAction(Actions.sequence(
                Actions.fadeOut(0.25f),
                Actions.visible(false)
            ));
        } else {
            reactionPanelContainer.getColor().a = 0f;
            reactionPanelContainer.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.fadeIn(0.25f)
            ));
        }
    }

    private void sendReaction(ReactionCategory category, int index) {
        controller.sendReaction(category, index);
    }

    private void showOpponentReaction(ReactionCategory category, int index) {
        Actor bubble = ReactionBubbleFactory.build(game, skin, category, index);
        opponentBubbleLayer.clearChildren();
        opponentBubbleLayer.add(bubble);
        bubble.getColor().a = 0f;
        bubble.addAction(Actions.sequence(
            Actions.fadeIn(0.2f),
            Actions.delay(2.5f),
            Actions.fadeOut(0.4f)
        ));
    }

    private void showEndScreen(ClientGameController.Side winner, String msg) {
    }
}
