package com.pvz2.models.network.onlineIZombie.messages;

/** Pushed (no requestId) to the target player so their client can show an accept/reject pop-up. */
public class ChallengeInvite {
    public String inviteId;
    public String fromUsername;

    public ChallengeInvite() {}
    public ChallengeInvite(String inviteId, String fromUsername) {
        this.inviteId = inviteId;
        this.fromUsername = fromUsername;
    }
}
