package com.pvz2.network.onlineIZombie.messages;

public class RandomMatchResponse {
    public boolean success;
    public boolean waiting;     // true: no opponent yet, you're queued — wait for a MatchFound push
                                 // false (with success true): you were paired immediately with whoever was waiting
    public String errorMessage; // e.g. "You're already queued" or "You're already in a match"

    public RandomMatchResponse() {}
}
