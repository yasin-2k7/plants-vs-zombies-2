package com.pvz2.models.network.onlineIZombie.messages;

/** Pushed (no requestId) to a single player, e.g. after handlePlaceZombie — the server's
 *  equivalent of what GameMenuView.showResult(...) did locally. */
public class ActionResult {
    public boolean success;
    public String message;

    public ActionResult() {}
    public ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
