package models.miniGame.IZombie;

import models.enums.PlantLayer;
import models.enums.PlantType;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.world.cellTerrains.LandTerrain;
import models.world.levelSetup.LevelSetup;
import models.zombie.Zombie;

import java.util.List;
import java.util.Random;

public class IZombieSetup implements LevelSetup {
    private int rows;
    private int cols;
    private List<Zombie> stageZombies;


    public IZombieSetup(int rows, int cols, List<Zombie> stageZombies) {
        this.rows = rows;
        this.cols = cols;
        this.stageZombies = stageZombies;
    }

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);

        world.setRows(rows);
        world.setCols(cols);

        Cell[][] grid = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c, new LandTerrain());
            }
        }
        world.setGrid(grid);

        if (world instanceof IZombieLevel level) {
            Random random = new Random();
            level.setAvailableZombies(stageZombies);


            for (int r = 0; r < rows; r++) {
                Brain brain = new Brain(r, 10, r * 100 + 50);
                level.getBrains().add(brain);
            }


            PlantType[] possiblePlants = {PlantType.PEASHOOTER, PlantType.SNOW_PEA, PlantType.WALL_NUT, PlantType.SUNFLOWER};
            for (int r = 0; r < rows; r++) {
                int plantCount = 2 + random.nextInt(2);
                for (int i = 0; i < plantCount; i++) {
                    int col = 1 + random.nextInt(4);
                    if (grid[r][col].isEmpty()) {
                        PlantType type = possiblePlants[random.nextInt(possiblePlants.length)];
                        Plant plant = createPlantInstance(type, r, col);
                        if (plant != null) {
                            grid[r][col].setPlant(plant, PlantLayer.MAIN);
                            level.getActivePlants().add(plant);
                        }
                    }
                }
            }

            for (int r = 0; r < rows; r++) {
                SunProducer sp = new SunProducer(Zombies.ARMORED, 1100, 0.4, 20);
                sp.setX(8 * 100 + 50);
                sp.setY(r * 100 + 50);
                sp.initSpawnTick(level.getCurrentTick());

                level.getSunProducers().add(sp);
                level.addZombie(sp);
            }

        }

    }

    private Plant createPlantInstance(PlantType type, int row, int col) {
        return switch (type) {
            case PEASHOOTER -> new Plant(PlantType.PEASHOOTER, 300, 100);
            case SNOW_PEA -> new Plant(PlantType.SNOW_PEA, 300, 80);
            case WALL_NUT -> new Plant(PlantType.WALL_NUT, 4000, 0);
            case SUNFLOWER -> new Plant(PlantType.SUNFLOWER, 300, 0);
            default -> new Plant(type, 300, 100);
        };
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }
}
