package models.miniGame.vaseBreaker;

import models.enums.PlantType;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.zombie.Zombie;

import java.util.Random;

public class VaseBreakerSetup implements LevelSetup {
    private int rows;
    private int cols;

    public VaseBreakerSetup(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void groundSetup(GameWorld world) {
        world.setConveyorMode(false);
        buildGrid(world, rows, cols);

        Random random = new Random();

        for(int r = 0; r < rows; r++){
            for(int c = 4; c < cols; c++){
                VaseType type = getRandomVaseType(random);
                Zombie hiddenZombie = null;
                SeedPacket hiddenSeed = null;

                float spawnX = c * 100 + 50;
                float spawnY = r * 100 + 50;

                if(type == VaseType.PLANT){
                    PlantType randomPlant = getRandomPlantType(random);
                    hiddenSeed = new SeedPacket(spawnX, spawnY, randomPlant);
                } else {
                    hiddenZombie = createZombieForVase(type);
                }
            }
        }
    }

    @Override
    public boolean requirePlantSelection() {
        return false;
    }

    private VaseType getRandomVaseType(Random random) {
        int r = random.nextInt(10);
        if (r < 4) return VaseType.PLANT;
        if (r < 9) return VaseType.NORMAL;
        return VaseType.GIANT;
    }

    private PlantType getRandomPlantType(Random random) {
        PlantType[] plants = {PlantType.PEASHOOTER, PlantType.SNOW_PEA, PlantType.WALL_NUT};
        return plants[random.nextInt(plants.length)];
    }

    private Zombie createZombieForVase(VaseType type) {


        return null;
    }
}
