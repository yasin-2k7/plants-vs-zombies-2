package models.world.levelSetup;

import models.plant.card.PlantCard;
import models.world.Cell;
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

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(true);
        buildGrid(world, rows, cols);

        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
        world.addMechanic(new ConveyorMechanic(availablePlants));
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
