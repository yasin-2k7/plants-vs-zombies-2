package com.pvz2.models.world;

import com.pvz2.models.core.App;
import com.pvz2.models.zombie.Zombie;

public class Sandstorm {
    public enum State { INTRO, LOOP, OUTRO, FINISHED }

    private State state = State.INTRO;
    private float x, y;
    private final int lane;
    private final int targetCol;
    private final float targetX;
    private final Zombie zombie;

    private float stateTime = 0f;
    private final float speed = 280f;

    private static final float INTRO_DURATION = 0.4f;
    private static final float OUTRO_DURATION = 0.5f;

    public Sandstorm(Zombie zombie, int lane, int targetCol, int totalCols) {
        this.zombie = zombie;
        this.lane = lane;
        this.targetCol = targetCol;

        // شروع از سمت راست بیرون صفحه
        this.x = App.getFirstCellX() + totalCols * App.getCellWidth() + 100f;
        this.y = App.getCellCenterY(lane);

        // مقصد زامبی روی چمن
        this.targetX = App.getFirstCellX() + targetCol * App.getCellWidth();

        if (zombie != null) {
            zombie.setX(this.x);
            zombie.setY(this.y);
        }
    }

    public void update(float delta) {
        stateTime += delta;

        switch (state) {
            case INTRO:
                if (stateTime >= INTRO_DURATION) {
                    state = State.LOOP;
                    stateTime = 0f;
                }
                break;

            case LOOP:
                x -= speed * delta;
                if (zombie != null) zombie.setX(x);

                if (x <= targetX) {
                    x = targetX;
                    state = State.OUTRO;
                    stateTime = 0f;
                }
                break;

            case OUTRO:
                if (stateTime >= OUTRO_DURATION) {
                    if (zombie != null) {
                        zombie.setX(targetX);
                    }
                    state = State.FINISHED;
                }
                break;

            case FINISHED:
                break;
        }
    }

    public State getState() { return state; }
    public float getX() { return x; }
    public float getY() { return y; }
    public Zombie getZombie() { return zombie; }
    public boolean isFinished() { return state == State.FINISHED; }
    public float getStateTime() { return stateTime; }
}
