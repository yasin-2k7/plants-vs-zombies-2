package models.zombie.state;

import models.zombie.Zombie;

public class WalkingState implements ZombieState {
    @Override
    public void handleAction(Zombie zombie) {
        // تا زمانی که در این وضعیت است، زامبی حرکت می‌کند
        zombie.move();

        // TODO: اگر زامبی به گیاه رسید، باید وضعیتش تغییر کند:
        // zombie.setState(new EatingState(targetPlant));
    }
}
