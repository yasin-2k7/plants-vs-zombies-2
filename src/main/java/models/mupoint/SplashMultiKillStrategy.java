package models.mupoint;

public class SplashMultiKillStrategy implements ScoreStrategy {
    @Override
    public int calculatePoints(KillEvent event) {
        if (event.isBySplashDamage() && event.getSimultaneousKills() > 1) {
            return event.getSimultaneousKills() * 50;
        }
        return 0;
    }
}