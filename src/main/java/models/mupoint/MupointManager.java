package models.mupoint;

import controller.GameMenuController;
import models.core.App;
import models.core.User;
import models.core.UserDataManager;

import java.util.ArrayList;
import java.util.List;

public class MupointManager {
    private int totalMupoints = 0;
    private List<ScoreStrategy> strategies = new ArrayList<>();

    public MupointManager() {
        strategies.add(new FastKillStrategy());
        strategies.add(new SunMilestoneStrategy());
        strategies.add(new CleanKillStrategy());
        strategies.add(new ToughZombieStrategy());
        strategies.add(new ComboKillStrategy());
    }

    public void onZombieDeath(KillEvent event) {
        int pointsGained = 0;
        for (ScoreStrategy strategy : strategies) {
            int pts = strategy.calculatePoints(event);
            if (pts > 0) {
                String strategyName = strategy.getClass().getSimpleName();
                GameMenuController.updateState(strategyName + ": +" + pts + " point");
                pointsGained += pts;
            }
        }
        applyPoints(pointsGained);
    }

    public void checkSunMilestones() {
        for (ScoreStrategy strategy : strategies) {
            if (strategy instanceof SunMilestoneStrategy) {
                int pts = strategy.calculatePoints(null);
                if (pts > 0) {
                    GameMenuController.updateState("SunMilestoneStrategy: +" + pts + " point");
                    applyPoints(pts);
                }
            }
        }
    }

    private void applyPoints(int pointsGained) {
        if (pointsGained <= 0) return;

        totalMupoints += pointsGained;

        User user = App.getCurrentUser();
        if (user != null) {
            user.updateMupointRecord(totalMupoints);
            UserDataManager.saveUser(user);
        }

        GameMenuController.updateState("Current Mupoints: " + totalMupoints);
    }

    public int getTotalMupoints() {
        return totalMupoints;
    }
}
