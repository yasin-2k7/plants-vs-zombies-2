package models.miniGame.beghouled;

import models.enums.PlantType;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.mechanics.NormalMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;
import models.zombie.wave.WaveSpawnEntry;

import java.util.List;

public class BeghouledSetup implements LevelSetup {
    private final int rows;
    private final int cols;
    private final List<PlantType> availablePlantTypes;
    private final List<PlantUpgrade> upgrades;
    private final int targetScore;
    private final List<WaveSpawnEntry> availableZombies;

    public BeghouledSetup(int rows, int cols, List<PlantType> availablePlantTypes,
                          List<PlantUpgrade> upgrades, int targetScore,
                          List<WaveSpawnEntry> availableZombies) {
        this.rows = rows;
        this.cols = cols;
        this.availablePlantTypes = availablePlantTypes;
        this.upgrades = upgrades;
        this.targetScore = targetScore;
        this.availableZombies = availableZombies;
    }


    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);
        buildGrid(world, rows, cols);

        BeghouledMechanics mechanics = new BeghouledMechanics(availablePlantTypes, upgrades, targetScore);
        world.addMechanic(mechanics);

        List<Wave> waves = Wave.generateWaves(20, 200, availableZombies, 40);
        WaveManager waveManager = new WaveManager(waves);
        waveManager.setRepeatForever(true);
        world.addMechanic(new NormalMechanic(waveManager));


        mechanics.fillRandomPlants(world);
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
