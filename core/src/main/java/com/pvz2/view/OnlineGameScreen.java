package com.pvz2.view;

import com.pvz2.Main;
import com.pvz2.network.onlineIZombie.ClientGameController;
import com.pvz2.network.onlineIZombie.messages.ReactionCategory;
import com.pvz2.network.onlineIZombie.messages.ReactionReceived;

import java.util.function.Consumer;

public class OnlineGameScreen extends MenuScreen{
    ClientGameController controller;
    private ZombiePlacementManager zombiePlacementManager = new ZombiePlacementManager();

    public OnlineGameScreen(Main game, ClientGameController controller) {
        super(game);
        this.controller = controller;
        controller.setActionResultListener((success, message) ->
            addToast(success ? "Info" : "Error", message, !success));
        controller.setMatchOverListener((winner, msg) -> showEndScreen(winner, msg));
        controller.setReactionListener(r -> showReactionBubble(r.fromUsername, r.category, r.index));
        controller.setReactionListener(new Consumer<ReactionReceived>() {
            @Override
            public void accept(ReactionReceived reactionReceived) {
                showOpponentReaction(reactionReceived.category, reactionReceived.index);
            }
        });
    }

    private void showReactionBubble(String fromUsername, ReactionCategory category, int index) {
    }

    private void showEndScreen(ClientGameController.Side winner, String msg) {
    }

    private void sendReaction(ReactionCategory category, int index){
        controller.sendReaction(category, index);
    }

    private void showOpponentReaction(ReactionCategory category, int index){

    }

    @Override
    protected void buildUI() {

    }

    public ZombiePlacementManager getZombiePlacementManager() {
        return zombiePlacementManager;
    }
}
