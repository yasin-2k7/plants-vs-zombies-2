package models.world.levelSetup;

import models.enums.PlantType;
import models.world.Cell;
import models.world.GameWorld;
import models.world.levelsSpecial.LockedPlantLevel;
import models.world.mechanics.NormalMechanic;
import models.world.mechanics.SunSpawnMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

import java.util.List;

public class LockedPlantsLevelSetup implements LevelSetup{
    private int rows;
    private int cols;
    private List<Wave> waves;
    private List<PlantType> lockedPlants;
    private List<PlantType> forcedPlants;

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);
        buildGrid(world, rows, cols);

        ((LockedPlantLevel) world).setLockedPlants(lockedPlants);
        ((LockedPlantLevel) world).setForcedPlants(forcedPlants);

        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
        world.addMechanic(new SunSpawnMechanic());
    }

    @Override
    public boolean requirePlantSelection() {
        return true;
    }
}
