package com.pvz2.network.onlineIZombie;

import com.badlogic.gdx.Gdx;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.miniGame.IZombie.OnlineIZombieLevel;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.network.NetworkClient;
import com.pvz2.network.NetworkMessage;
import com.pvz2.network.onlineIZombie.messages.*;
import com.pvz2.view.MenuScreen;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Client-side controller for a live "I, Zombie" match. One instance is created when
 * the MATCH_FOUND push arrives (see the matchId/side/opponentUsername it carries).
 *
 * Local-only actions (select/unselect shovel, select/unselect plant, select/unselect
 * zombie) touch only this client's own UI state — kept as plain fields here, NOT inside
 * `world`, because `world` is wholesale-replaced every time a GAME_STATE push arrives
 * (including pushes triggered purely by the opponent's actions). If selection lived
 * inside `world`, it would get silently cleared any time the opponent did anything.
 *
 * Gameplay actions (collect sun, plant plant, pluck plant, place zombie) are sent to
 * the server as fire-and-forget intents. The server is the single source of truth;
 * a rejected action just never shows up in the next snapshot — no need to block
 * waiting for a yes/no reply on every click.
 */
public class ClientGameController {
    public enum Side { PLANTS, ZOMBIES }

    private final String matchId;
    private final Side side;
    private final String opponentUsername;

    private OnlineIZombieLevel world; // render-only copy; always overwritten by sync(), never mutated locally

    // local-only UI selection state — see class comment for why this is NOT on `world`
    private boolean shovelSelected = false;
    private boolean plantSelected = false;
    private PlantType selectedPlantType = null;
    private boolean zombieSelected = false;
    private String selectedZombieType = null;
    private BiConsumer<Boolean, String> actionResultListener;
    private BiConsumer<Side, String> matchOverListener;
    private Consumer<ReactionReceived> reactionListener;
    private Runnable onWorldReadyListener;
    private boolean worldReadyFired = false;



    public ClientGameController(MatchFound info) {
        this.matchId = info.matchId;
        this.side = Side.valueOf(info.side);
        this.opponentUsername = info.opponentUsername;
        NetworkClient.get().onPush("GAME_STATE", this::onGameStatePush);
        NetworkClient.get().onPush("ACTION_RESULT", this::onActionResult);
        NetworkClient.get().onPush("MATCH_OVER", this::onMatchOver);
        NetworkClient.get().onPush("REACTION", this::onReaction);
    }

    /** Whoever owns the game screen should set this once (e.g. to screen::addToast) to
     *  see server-side rejections like "invalid zombie" or "not enough brains". */
    public void setActionResultListener(BiConsumer<Boolean, String> listener) {
        this.actionResultListener = listener;
    }

    /** Set once by the game screen to react when the match ends — winning side + message. */
    public void setMatchOverListener(BiConsumer<Side, String> listener) {
        this.matchOverListener = listener;
    }

    /** Set once by the game screen to show the opponent's text/emoji/sticker — draw it
     *  from your own local preset lists using received.category + received.index. */
    public void setReactionListener(Consumer<ReactionReceived> listener) {
        this.reactionListener = listener;
    }

    public Side getSide() { return side; }
    public String getOpponentUsername() { return opponentUsername; }
    public OnlineIZombieLevel getWorld() { return world; }

    private void onGameStatePush(NetworkMessage msg) {
        GameStateUpdate update = NetworkClient.get().parsePayload(msg, GameStateUpdate.class);
        if (!matchId.equals(update.matchId)) return; // stale push from a different/older match
        boolean wasNull = (world == null);
        world = update.world;
        if (wasNull && onWorldReadyListener != null && !worldReadyFired) {
            worldReadyFired = true;
            onWorldReadyListener.run();
        }

    }

    private void onActionResult(NetworkMessage msg) {
        ActionResult result = NetworkClient.get().parsePayload(msg, ActionResult.class);
        Gdx.app.postRunnable(() -> {
            if (actionResultListener != null) actionResultListener.accept(result.success, result.message);
        });
    }

    private void onMatchOver(NetworkMessage msg) {
        MatchOver update = NetworkClient.get().parsePayload(msg, MatchOver.class);
        if (!matchId.equals(update.matchId)) return;
        Gdx.app.postRunnable(() -> {
            if (matchOverListener != null) matchOverListener.accept(Side.valueOf(update.winnerSide), update.message);
        });

    }

    private void onReaction(NetworkMessage msg) {
        ReactionReceived received = NetworkClient.get().parsePayload(msg, ReactionReceived.class);
        if (!matchId.equals(received.matchId)) return;
        Gdx.app.postRunnable(() -> {
            if (reactionListener != null) reactionListener.accept(received);
        });

    }

    // ---------------- local-only UI state: plants side ----------------

    public boolean isShovelSelected() { return shovelSelected; }
    public boolean isPlantSelected() { return plantSelected; }
    public PlantType getSelectedPlantType() { return selectedPlantType; }

    public void unselectShovel() {
        shovelSelected = false;
    }

    public boolean selectAndUnselectShovel() {
        unselectPlant();
        shovelSelected = !shovelSelected;
        return true;
    }

    public boolean selectAndUnselectPlant(PlantType type, MenuScreen screen) {
        if (world == null) return false;
        if (plantSelected) {
            unselectShovel();
            if (selectedPlantType == type) {
                unselectPlant();
                return false;
            }
            unselectPlant();
        }
        PlantCard selectedCard = null;
        for (PlantCard card : world.getPlantLists()) {
            if (card.getType().equals(type)) { selectedCard = card; break; }
        }
        if (selectedCard == null) return false;
        if (!selectedCard.isReady()) {
            screen.addToast("Error", "This plant isn't ready!");
            return false;
        }
        if (selectedCard.getSunCost() > world.getSun()) {
            screen.addToast("Error", "You don't have enough suns!");
            return false;
        }
        plantSelected = true;
        selectedPlantType = type;
        return true;
    }

    public void unselectPlant() {
        plantSelected = false;
        selectedPlantType = null;
    }

    public boolean selectAndUnselectZombie(String type, MenuScreen screen) {
        if (world == null) return false;
        if (zombieSelected) {
            if (selectedZombieType.equals(type)) {
                unselectZombie();
                return false;
            }
            unselectZombie();
        }
        ZombieCard selectedCard = null;
        for (ZombieCard card : world.getZombieCards()) {
            if (card.getType().equals(type)) { selectedCard = card; break; }
        }
        if (selectedCard == null) return false;
        if (!selectedCard.isReady()) {
            screen.addToast("Error", "This zombie isn't ready!");
            return false;
        }
        if (selectedCard.getBrainCost() > world.getZombieBrains()) {
            screen.addToast("Error", "You don't have enough brains!");
            return false;
        }
        zombieSelected = true;
        selectedZombieType = type;
        return true;
    }

    public void unselectZombie() {
        zombieSelected = false;
        selectedZombieType = null;
    }

    public void setOnWorldReadyListener(Runnable listener) {
        this.onWorldReadyListener = listener;
        if (world != null && !worldReadyFired) {
            worldReadyFired = true;
            listener.run(); // covers the race where the first snapshot already arrived before this was registered
        }
    }

    // ---------------- network gameplay actions ----------------

    public void plantPlant(float x, float y) {
        if (side != Side.PLANTS || !plantSelected) return;
        NetworkClient.get().sendMessage("PLANT_PLANT", new PlantPlantRequest(matchId, selectedPlantType, x, y));
        unselectPlant();
    }

    public void pluckPlant(float x, float y) {
        if (side != Side.PLANTS || !shovelSelected) return;
        NetworkClient.get().sendMessage("PLUCK_PLANT", new PluckPlantRequest(matchId, x, y));
    }

    public void collectSun(float touchX, float touchY) {
        if (side != Side.PLANTS) return;
        NetworkClient.get().sendMessage("COLLECT_SUN", new CollectSunRequest(matchId, touchX, touchY));
    }

    public void collectBrain(float touchX, float touchY) {
        if (side != Side.ZOMBIES) return;
        NetworkClient.get().sendMessage("COLLECT_BRAIN", new CollectBrainRequest(matchId, touchX, touchY));
    }

    public void placeZombie(String zombieType, float x, float y) {
        if (side != Side.ZOMBIES) return;
        NetworkClient.get().sendMessage("PLACE_ZOMBIE", new PlaceZombieRequest(matchId, zombieType, x, y));
    }

    public void sendReaction(ReactionCategory category, int index){
        NetworkClient.get().sendMessage("SEND_REACTION", new SendReactionRequest(matchId, category, index));
    }
}
