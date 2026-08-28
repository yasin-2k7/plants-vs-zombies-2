package com.pvz2.view;

import com.pvz2.Main;
import com.pvz2.network.onlineIZombie.ClientGameController;
import com.pvz2.network.onlineIZombie.messages.ReactionCategory;

public class OnlineGameScreen extends MenuScreen{
    ClientGameController controller;

    public OnlineGameScreen(Main game, ClientGameController controller) {
        super(game);
        this.controller = controller;
        controller.setActionResultListener((success, message) ->
            addToast(success ? "Info" : "Error", message, !success));
        controller.setMatchOverListener((winner, msg) -> showEndScreen(winner, msg));
        controller.setReactionListener(r -> showReactionBubble(r.fromUsername, r.category, r.index));
    }

    private void showReactionBubble(String fromUsername, ReactionCategory category, int index) {
    }

    private void showEndScreen(ClientGameController.Side winner, String msg) {
    }

    @Override
    protected void buildUI() {

    }
}
