package models.world.levelSetup;

import models.plant.card.PlantCard;
import models.world.GameWorld;
import models.world.mechanics.ConveyorMechanic;
import models.world.mechanics.NormalMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

import java.util.List;

public class ConveyorLevelSetup implements LevelSetup {
    private int rows;
    private int cols;
    private List<Wave> waves;
    private List<PlantCard> plantCards;


    public ConveyorLevelSetup(int rows, int cols, List<Wave> waves, List<PlantCard> plantCards) {
        this.rows = rows;
        this.cols = cols;
        this.waves = waves;
        this.plantCards = plantCards;
    }


    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(true);
        buildGrid(world, rows, cols);


        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
        world.addMechanic(new ConveyorMechanic(plantCards));
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
