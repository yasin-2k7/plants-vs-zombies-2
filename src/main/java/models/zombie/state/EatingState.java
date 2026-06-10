package models.zombie.state;

import models.zombie.Zombie;

public class EatingState implements ZombieState {
    private Object targetPlant; // گیاهی که زامبی در حال خوردن آن است

    public EatingState(Object targetPlant) {
        this.targetPlant = targetPlant;
    }

    @Override
    public void handleAction(Zombie zombie) {
        // TODO: منطق آسیب زدن به گیاه (مثلاً کاهش جان گیاه بر اساس zombie.getDamage())

        // TODO: اگر جان گیاه صفر شد، زامبی باید دوباره راه بیفتد:
        // zombie.setState(new WalkingState());
    }
}
