package models.mupoint;

public class FastKillStrategy implements ScoreStrategy {

    @Override
    public int calculatePoints(KillEvent event) {

        if (event.getSurvivalTicks() < 50) {
            return (int) ((50 - event.getSurvivalTicks()) * 3);
        }

        return 0;
    }
}
