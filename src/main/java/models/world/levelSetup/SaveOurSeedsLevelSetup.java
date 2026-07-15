package models.world.levelSetup;

import models.enums.PlantType;
import models.plant.Plant;
import models.plant.PlantFactory;
import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.LandTerrain;
import models.world.mechanics.NormalMechanic;
import models.world.mechanics.SunSpawnMechanic;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class SaveOurSeedsLevelSetup implements LevelSetup{
    private int rows;
    private int cols;
    private List<Wave> waves;
    private Map<Point, PlantType> protectedPlants;

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);
        world.setRows(rows);
        world.setCols(cols);

        Cell[][] grid = new Cell[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Cell(r, c, new LandTerrain());
        world.setGrid(grid);

        for(Map.Entry<Point, PlantType> entry : protectedPlants.entrySet()){
            int row = (int) entry.getKey().getY();
            int col = (int) entry.getKey().getX();

            Plant plant = PlantFactory.createPlant(entry.getValue(), row, 0);
            grid[row][col].handlePlanting(entry.getValue());

            world.getActivePlants().add(plant);
        }

        WaveManager waveManager = new WaveManager(waves);
        world.addMechanic(new NormalMechanic(waveManager));
        world.addMechanic(new SunSpawnMechanic());
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
