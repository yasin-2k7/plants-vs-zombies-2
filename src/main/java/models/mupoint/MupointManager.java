package models.mupoint;
import java.util.ArrayList;
import java.util.List;

public class MupointManager {
    private int totalMupoints = 0;
    private List<ScoreStrategy> strategies = new ArrayList<>();

    public MupointManager() {
        strategies.add(new FastKillStrategy());
        strategies.add(new SplashMultiKillStrategy());
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
                System.out.println("   🎯 استراتژی فعال شد: " + strategyName + " -> +" + pts + " امتیاز");
                pointsGained += pts;
            }
        }
        System.out.println("Current Mupoints: " + totalMupoints);
        totalMupoints += pointsGained;
    }

    public int getTotalMupoints() {
        return totalMupoints;
    }
}
