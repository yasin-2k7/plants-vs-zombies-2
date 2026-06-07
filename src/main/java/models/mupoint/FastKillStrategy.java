package models.mupoint;

public class FastKillStrategy implements ScoreStrategy{

    @Override
    public int calculatePoints(KillEvent event) {
        return (event.killDuration < 5) ? 50 : 0;  // فعلا همینطوری الکی
    }
}
