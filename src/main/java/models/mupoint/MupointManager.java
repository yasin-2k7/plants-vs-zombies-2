package models.mupoint;
import java.util.ArrayList;
import java.util.List;

public class MupointManager {
    private int totalMupoints = 0;
    private List<ScoreStrategy> strategies = new ArrayList<>();

    public MupointManager() {
        // ثبت الگوهای ۵ گانه در منیجر
        strategies.add(new FastKillStrategy());
        strategies.add(new SimultaneousKillStrategy());
        //TODO ... اضافه کردن سایر استراتژی‌ها
    }

    // متدی که در زمان مرگ زامبی صدا زده می‌شود (الگوی Observer)
    public void onZombieDeath(KillEvent event) {
        for (ScoreStrategy strategy : strategies) {
            totalMupoints += strategy.calculatePoints(event);
        }
        System.out.println("Current Mupoints: " + totalMupoints);
    }

}
