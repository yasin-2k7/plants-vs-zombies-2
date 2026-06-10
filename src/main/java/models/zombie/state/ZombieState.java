package models.zombie.state;

import models.zombie.Zombie;

public interface ZombieState {
    void handleAction(Zombie zombie);
}
