package models.lawnMower;

import models.core.App;
import models.world.GameState;
import models.zombie.Zombie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LawnMowerManager {
    private static final int TOTAL_ROWS = 5;
    private List<LawnMower> mowers;
    private boolean enabled = true;

    public LawnMowerManager() {
        this.mowers = new ArrayList<>();
        for (int i = 0; i < TOTAL_ROWS; i++) {
            mowers.add(new LawnMower(i));
        }
    }

    public void updateMowers(List<Zombie> allZombies) {
        if (!enabled) {
            return;
        }

        checkActivations(allZombies);

        for (LawnMower mower : mowers) {
            List<Zombie> zombies = getZombiesInRow(allZombies, mower.getRow());
            zombies.stream()
                    .min(Comparator.comparingDouble(Zombie::getX)).ifPresent(mower::checkCollision);
            if (mower.isActive()) {
                mower.mowZombies(getZombiesInRow(allZombies, mower.getRow()));
                mower.move();
            }
        }
    }

    private void checkActivations(List<Zombie> allZombies) {
        for (Zombie z : allZombies) {
            if (!z.isDead() && z.getX() <= 0) {
                int row = getRowFromY(z.getY());
                LawnMower mower = getMowerByRow(row);

                if (mower != null) {
                    if (!mower.isSpent() && !mower.isActive()) {
                        mower.activate();
                    } else if (mower.isSpent()) {

                        App.getCurrentGame().setState(GameState.LOST);
                        z.setSpeed(0);
                    }
                }
            }
        }
    }

    private List<Zombie> getZombiesInRow(List<Zombie> allZombies, int row) {
        List<Zombie> zombiesInRow = new ArrayList<>();
        for (Zombie z : allZombies) {
            if (getRowFromY(z.getY()) == row) {
                zombiesInRow.add(z);
            }
        }
        return zombiesInRow;
    }

    private LawnMower getMowerByRow(int row) {
        for (LawnMower mower : mowers) {
            if (mower.getRow() == row) {
                return mower;
            }
        }
        return null;
    }

    private int getRowFromY(float y) {
        int row = (int) (y / 100);
        return Math.max(0, Math.min(row, TOTAL_ROWS - 1));
    }

    public List<LawnMower> getMowers() {
        return mowers;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            this.mowers.clear();
        }
    }
}
