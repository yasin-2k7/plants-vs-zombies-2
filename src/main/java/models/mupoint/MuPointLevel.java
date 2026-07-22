package models.mupoint;

import models.enums.Chapter;
import models.miniGame.MiniGameWorld;
import models.world.ChapterWorld.AncientEgyptWorld;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.levelSetup.NormalLevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.loseCondition.NormalLose;
import models.world.winCondition.NormalWin;
import models.world.winCondition.WinCondition;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveSpawnEntry;

import java.util.ArrayList;
import java.util.List;

public class MuPointLevel {
    public static GameWorld createMuPointLevel(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        GameWorld world = new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.EGYPT);
        world.setWillUnlockLevel(true);

        MupointManager mupointManager = new MupointManager();


        world.setMupointManager(mupointManager);
        return world;
    }
}
