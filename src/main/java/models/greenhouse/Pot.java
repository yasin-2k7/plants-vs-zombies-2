package models.greenhouse;

import models.plant.Plant;

public class Pot {
    private int x, y;
    private boolean isLocked;
    private Plant plant;
    private long plantTime;
    private boolean isReady;

    public boolean isEmpty() {
        return false;
    }

    public void plant(Plant p) {

    }

    public void clear() {

    }

    public long getRemainingHours() {
        return 0;
    }
}
