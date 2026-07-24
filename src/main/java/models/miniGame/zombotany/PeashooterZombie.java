package models.miniGame.zombotany;

import models.core.App;
import models.enums.ProjectileType;
import models.enums.Zombies;
import models.plant.Plant;
import models.projectile.Projectile;
import models.projectile.hitStrategies.CombinedDamageStrategy;
import models.projectile.hitStrategies.PlantDamageStrategy;
import models.projectile.movementStrategies.StraightMovementStrategy;
import models.projectile.strikeStrategies.CheckPlantStrike;
import models.projectile.strikeStrategies.CheckStraightStrike;
import models.projectile.strikeStrategies.CheckStrike;
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
        System.out.println("🎯 PeashooterZombie saw " + target.getClass().getSimpleName() +
                " at row " + row + "! Shooting...");

        Projectile pea = world.getProjectilesPool().acquire();

        StraightMovementStrategy movement = new StraightMovementStrategy(-5f, 0, 0);

        CheckStrike checkStrike = new CheckPlantStrike();
        pea.reset(this.x - 20,
                this.y,
                new PlantDamageStrategy(20, "NORMAL"),
                movement,
                checkStrike,
                ProjectileType.PEA);

        world.addProjectile(pea);
        System.out.println("🚀 Zombie Pea spawned at X: " + (this.x - 20) + ", Y: " + this.y);
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
