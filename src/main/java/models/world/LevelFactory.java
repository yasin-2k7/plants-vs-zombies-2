package models.world;

import models.enums.Chapter;
import models.enums.LevelType;
import models.world.ChapterWorld.AncientEgyptWorld;
import models.world.levelSetup.LevelSetup;
import models.world.levelSetup.NormalLevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.loseCondition.NormalLose;
import models.world.mechanics.ConveyorMechanic;
import models.world.mechanics.Mechanic;
import models.world.mechanics.NormalMechanic;
import models.world.mechanics.SunSpawnMechanic;
import models.world.winCondition.NormalWin;
import models.world.winCondition.WinCondition;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;
import models.zombie.wave.WaveSpawnEntry;

import java.util.ArrayList;
import java.util.List;

public class LevelFactory {

    public static GameWorld createLevel(Chapter chapter, LevelType levelType){
        return switch (chapter) {
            case EGYPT -> switch (levelType) {
                case NORMAL -> createAncientEgyptLevel1();
                default -> throw new IllegalArgumentException("invalid level");
            };
            case BIG_WAVE_BEACH -> null;
            case DARK_AGES -> null;
            case FROSTBITE_CAVES -> null;
        };


    }

    private static GameWorld createAncientEgyptLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new AncientEgyptWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }
}
