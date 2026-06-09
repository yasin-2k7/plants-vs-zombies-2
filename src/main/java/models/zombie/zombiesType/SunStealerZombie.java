package models.zombie.zombiesType;

import models.zombie.Zombie;

public class SunStealerZombie extends Zombie {
    private int stolenSun = 0;

    public SunStealerZombie(int health, int speed, int damage) {
        super("Sun Stealer Zombie", health, speed, damage);
    }

    // متد دزدیدن خورشید از بازیکن
    public void stealSun() {
        int amount = 25; // مثلا ۲۵ واحد در ثانیه
        // player.decreaseSun(amount);
        this.stolenSun += amount;
    }

    @Override
    public void die() {
        int returnedSun = this.stolenSun / 2;
        // player.addSun(returnedSun);
        super.die();
    }
}
