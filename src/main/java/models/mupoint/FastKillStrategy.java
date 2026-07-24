package models.mupoint;

public class FastKillStrategy implements ScoreStrategy {

    @Override
    public int calculatePoints(KillEvent event) {
        long secondsAlive = event.getSurvivalTicks() / 20;

        if (secondsAlive < 10) {
            return (int) ((10 - secondsAlive) * 15);
        }
        return 0;
    }
}
