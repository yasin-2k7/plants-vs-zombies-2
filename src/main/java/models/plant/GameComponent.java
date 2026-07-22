package models.plant;

import models.zombie.Zombie;

public interface GameComponent {
    void update(Plant owner);
    void activatePlantFood(Plant owner);
    default int onTakeDamage(Plant owner, int damageAmount, Zombie attacker) {
        return damageAmount;
    }
    default void onDeath(Plant owner){}
}