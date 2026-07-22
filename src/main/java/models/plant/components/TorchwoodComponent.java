package models.plant.components;

import controller.LevelMenuController;
import models.core.App;
import models.enums.ProjectileType;
import models.plant.GameComponent;
import models.plant.Plant;
import models.plant.components.explosionRanges.CircularRange;
import models.plant.components.explosiveBehaviors.AreaDamageBehavior;
import models.plant.components.explosiveTriggers.InstantTrigger;
import models.projectile.Projectile;
import models.world.Cell;

public class TorchwoodComponent implements GameComponent {
    private int factor = 2;
    private boolean explodeOnDeath;

    public TorchwoodComponent(int factor, boolean explodeOnDeath) {
        this.factor = factor;
        this.explodeOnDeath = explodeOnDeath;
    }

    @Override
    public void update(Plant owner) {
        for (Projectile projectile : App.getCurrentGame().getActiveProjectiles()){
            if (Cell.findCell(projectile.getX(), projectile.getY(), LevelMenuController.getGameCells()) == owner.getCell()){
                if (projectile.getType().equals(ProjectileType.PEA)){
                    projectile.setType(ProjectileType.FIRE_PEA);
                    projectile.getHitStrategy().setElement("FIRE");
                    projectile.getHitStrategy().increaseDamage(factor);
                }
                else if (projectile.getType().equals(ProjectileType.ICE_PEA)){
                    projectile.setType(ProjectileType.PEA);
                    projectile.getHitStrategy().setElement(null);
                }
            }
        }

    }

    @Override
    public void activatePlantFood(Plant owner) {
        factor = 3;
    }

    @Override
    public void onDeath(Plant owner) {
        if (explodeOnDeath){
            ExplosivesComponent explosivesComponent = new ExplosivesComponent(InstantTrigger.INSTANCE,
                    new AreaDamageBehavior(200, new CircularRange(1)), 0);
            explosivesComponent.setPostTriggerDelay(0);
            explosivesComponent.update(owner);
        }
    }
}
