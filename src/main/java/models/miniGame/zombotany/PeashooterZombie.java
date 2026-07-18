package models.miniGame.zombotany;

import models.core.App;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.GameWorld;
import models.zombie.Zombie;

public class PeashooterZombie extends Zombie {
    private int shootCooldown = 0;
    private static final int COOLDOWN_TICKS = 15;

    public PeashooterZombie(Zombies name, int health, double speed, int damage) {
        super(name, health, speed, damage);
    }

    private void shoot(){
        GameWorld world = App.getCurrentGame();
        if (world == null) return;

        int row = (int) (this.y / App.getCellHeight());
        Plant target = world.getNearestPlantInRow(row, this.x);
        if (target == null) return;

        target.takeDamage(damage);
    }

    @Override
    public void update() {
        if(isDead) return;
        super.update();
        if(isDead) return;

        if(shootCooldown <= 0){
            shoot();
            shootCooldown = COOLDOWN_TICKS;
        } else {
            shootCooldown--;
        }
    }
}
