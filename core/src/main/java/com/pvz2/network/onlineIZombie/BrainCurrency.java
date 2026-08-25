package com.pvz2.network.onlineIZombie;

/**
 * The zombies' equivalent of a Sun pickup — click-to-collect currency that funds zombie
 * placement. Deliberately NOT the same class as Brain.java (the goal objects zombies eat
 * at the red line to win) — same word, unrelated concept, kept separate on purpose.
 *
 * NOTE: this currency is tracked separately from GameWorld's `sun` field. See the class
 * comment on ServerGameController for why — sun and brains can't share one counter once
 * both sides are live on the same world at once.
 */
public class BrainCurrency {
    private final float x, y;
    private boolean collected = false;

    public BrainCurrency(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isCollected() { return collected; }
    public void collect() { collected = true; }

    /** Simple radius hit-test for a click at (px, py). Swap for your Sun class's exact
     *  bounds convention (e.g. a Rectangle) if you want the two to feel identical to click. */
    public boolean contains(float px, float py, float radius) {
        float dx = px - x, dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }
}
