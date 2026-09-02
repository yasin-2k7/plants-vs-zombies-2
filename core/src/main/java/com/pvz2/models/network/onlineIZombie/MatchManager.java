package com.pvz2.models.network.onlineIZombie;

import com.pvz2.models.network.ClientHandler;
import com.pvz2.models.network.onlineIZombie.messages.*;
import com.pvz2.models.network.onlineIZombie.messages.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Owns everything about "getting two players into a Match": specific-user challenges,
 * the random-opponent queue, and the resulting active matches.
 * One instance lives on GameServer and is shared by every ClientHandler thread, so
 * every method that touches shared state is synchronized — fine at course-project scale.
 */
public class MatchManager {

    private static class PendingInvite {
        final ClientHandler challenger;
        final ClientHandler target;
        PendingInvite(ClientHandler challenger, ClientHandler target) {
            this.challenger = challenger;
            this.target = target;
        }
    }

    private final Map<String, PendingInvite> pendingInvites = new ConcurrentHashMap<>(); // inviteId -> invite
    private final Map<String, Match> matchesById = new ConcurrentHashMap<>();
    private final Map<ClientHandler, Match> matchByPlayer = new ConcurrentHashMap<>();
    private final Consumer<Match> onMatchCreated;

    private ClientHandler waitingForRandom; // single-slot "queue" — null when empty

    /** onMatchCreated fires right after MATCH_FOUND is pushed to both players, from whichever
     *  flow created the match — GameServer uses it to eagerly build and start that match's
     *  ServerGameController, instead of waiting for a player to send the first gameplay action
     *  (nobody can, until the world exists — that's the deadlock this closes). */
    public MatchManager(Consumer<Match> onMatchCreated) {
        this.onMatchCreated = onMatchCreated;
    }

    // ---------- specific-user challenge ----------

    public synchronized ChallengeResponse challenge(ClientHandler challenger, ClientHandler target) {
        ChallengeResponse res = new ChallengeResponse();

        if (target == null) {
            res.success = false;
            res.errorMessage = "That user is invalid or offline.";
            return res;
        }
        if (target == challenger) {
            res.success = false;
            res.errorMessage = "You can't challenge yourself.";
            return res;
        }
        if (isBusy(challenger)) {
            res.success = false;
            res.errorMessage = "You're already queued or in a match.";
            return res;
        }
        if (isBusy(target)) {
            res.success = false;
            res.errorMessage = "That player is already queued or in a match.";
            return res;
        }

        String inviteId = UUID.randomUUID().toString();
        pendingInvites.put(inviteId, new PendingInvite(challenger, target));

        target.send("CHALLENGE_INVITE", null, new ChallengeInvite(inviteId, challenger.getUsername()));

        res.success = true;
        res.inviteId = inviteId;
        return res;
    }

    public synchronized ChallengeAnswerResponse answerChallenge(ClientHandler responder,
                                                                String inviteId, boolean accept) {
        PendingInvite invite = pendingInvites.get(inviteId);

        if (invite == null || invite.target != responder) {
            return new ChallengeAnswerResponse(false, "This invite is no longer valid.");
        }
        pendingInvites.remove(inviteId);

        if (!accept) {
            invite.challenger.send("CHALLENGE_DECLINED", null, new ChallengeDeclined(responder.getUsername()));
            return new ChallengeAnswerResponse(true, null);
        }

        if (isBusy(invite.challenger)) {
            return new ChallengeAnswerResponse(false, "The challenger is no longer available.");
        }

        createMatch(invite.challenger, invite.target);
        return new ChallengeAnswerResponse(true, null);
    }

    // ---------- random matchmaking ----------

    public synchronized RandomMatchResponse joinRandomQueue(ClientHandler player) {
        RandomMatchResponse res = new RandomMatchResponse();

        if (isBusy(player)) {
            res.success = false;
            res.errorMessage = "You're already queued or in a match.";
            return res;
        }

        if (waitingForRandom == null) {
            waitingForRandom = player;
            res.success = true;
            res.waiting = true;
            return res;
        }

        ClientHandler opponent = waitingForRandom;
        waitingForRandom = null;

        createMatch(player, opponent);
        res.success = true;
        res.waiting = false;
        return res;
    }

    public synchronized void cancelRandomQueue(ClientHandler player) {
        if (waitingForRandom == player) {
            waitingForRandom = null;
        }
    }

    // ---------- shared ----------

    private void createMatch(ClientHandler a, ClientHandler b) {
        Match match = new Match(a, b);
        matchesById.put(match.getMatchId(), match);
        matchByPlayer.put(a, match);
        matchByPlayer.put(b, match);

        a.send("MATCH_FOUND", null, new MatchFound(
            match.getMatchId(), b.getUsername(), match.getSideOf(a).name()));
        b.send("MATCH_FOUND", null, new MatchFound(
            match.getMatchId(), a.getUsername(), match.getSideOf(b).name()));

        onMatchCreated.accept(match);
    }

    private boolean isBusy(ClientHandler player) {
        return player == waitingForRandom || matchByPlayer.containsKey(player);
    }

    public Match getMatch(String matchId) {
        return matchesById.get(matchId);
    }

    public Match getMatchOf(ClientHandler player) {
        return matchByPlayer.get(player);
    }

    /** Called by ServerGameController once a match ends naturally (win/lose), so both
     *  players are free to queue or be challenged again — mirrors handleDisconnect's
     *  cleanup but without touching the random queue or pending invites, which a normal
     *  match end has nothing to do with. */
    public synchronized void endMatch(String matchId) {
        Match match = matchesById.remove(matchId);
        if (match != null) {
            matchByPlayer.remove(match.getPlantsPlayer());
            matchByPlayer.remove(match.getZombiesPlayer());
        }
    }

    /** Called from GameServer.onDisconnect so a dropped connection doesn't leave stale state around. */
    public synchronized void handleDisconnect(ClientHandler player) {
        cancelRandomQueue(player);
        pendingInvites.entrySet().removeIf(e ->
            e.getValue().challenger == player || e.getValue().target == player);

        Match match = matchByPlayer.remove(player);
        if (match != null) {
            matchesById.remove(match.getMatchId());
            ClientHandler opponent = match.getOpponentOf(player);
            if (opponent != null) {
                matchByPlayer.remove(opponent);
                // TODO once in-match play exists: push an "OPPONENT_LEFT" notice to `opponent` here.
            }
        }
    }
}
