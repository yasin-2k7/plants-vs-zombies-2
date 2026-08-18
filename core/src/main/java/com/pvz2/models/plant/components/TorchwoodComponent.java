package com.pvz2.models.plant.components;

import com.pvz2.controller.LevelMenuController;
import com.pvz2.models.core.App;
import com.pvz2.models.enums.ProjectileType;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.components.explosionRanges.CircularRange;
import com.pvz2.models.plant.components.explosiveBehaviors.AreaDamageBehavior;
import com.pvz2.models.plant.components.explosiveTriggers.InstantTrigger;
import com.pvz2.models.pool.GenericObjectPool;
import com.pvz2.models.projectile.Projectile;
import com.pvz2.models.world.Cell;

import java.util.IdentityHashMap;
import java.util.Map;

public class TorchwoodComponent implements GameComponent {
    private int factor = 2;
    private boolean explodeOnDeath;

    private final Map<Projectile, Integer> convertedProjectiles = new IdentityHashMap<>();

    public TorchwoodComponent(int factor, boolean explodeOnDeath) {
        this.factor = factor;
        this.explodeOnDeath = explodeOnDeath;
    }

    @Override
    public void update(Plant owner, float delta) {
        GenericObjectPool<Projectile> pool = App.getCurrentGame().getProjectilesPool();

        for (Projectile projectile : App.getCurrentGame().getActiveProjectiles()) {
            if (Cell.findCell(projectile.getX(), projectile.getY(),
                LevelMenuController.getGameCells()) != owner.getCell()) {
                continue;
            }

            int currentGeneration = pool.getGeneration(projectile);
            Integer recordedGeneration = convertedProjectiles.get(projectile);

            if (recordedGeneration != null && recordedGeneration == currentGeneration) {
                continue;
            }

            boolean converted = false;
            if (projectile.getType().equals(ProjectileType.PEA)) {
                projectile.setType(ProjectileType.FIRE_PEA);
                projectile.getHitStrategy().setElement("FIRE");
                projectile.getHitStrategy().increaseDamage(factor);
                converted = true;
            } else if (projectile.getType().equals(ProjectileType.ICE_PEA)) {
                projectile.setType(ProjectileType.PEA);
                projectile.getHitStrategy().setElement(null);
                converted = true;
            }

            if (converted) {
                convertedProjectiles.put(projectile, currentGeneration);
            }
        }
    }

    @Override
    public void activatePlantFood(Plant owner) {
        factor = 3;
    }

    @Override
    public void onDeath(Plant owner, float delta) {
        if (explodeOnDeath) {
            ExplosivesComponent explosivesComponent = new ExplosivesComponent(InstantTrigger.INSTANCE,
                new AreaDamageBehavior(200, new CircularRange(1)), 0, 0);
            explosivesComponent.update(owner, delta);
        }
    }
}
