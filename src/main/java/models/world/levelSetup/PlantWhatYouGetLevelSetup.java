package models.world.levelSetup;

import models.plant.card.PlantCard;
import models.world.GameWorld;
import models.world.mechanics.ConveyorMechanic;
import models.world.mechanics.NormalMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

import java.util.List;

public class PlantWhatYouGetLevelSetup implements LevelSetup{
    private int rows;
    private int cols;
    private List<Wave> waves;
    private int initialSun;
    private List<PlantCard> availablePlants;

    public PlantWhatYouGetLevelSetup(int rows, int cols, List<Wave> waves, int initialSun, List<PlantCard> availablePlants) {
        this.rows = rows;
        this.cols = cols;
        this.waves = waves;
        this.initialSun = initialSun;
        this.availablePlants = availablePlants;
    }

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(true);
        buildGrid(world, rows, cols);

        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
        world.addMechanic(new ConveyorMechanic(availablePlants));
        world.setPlantingPhase(true);
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
