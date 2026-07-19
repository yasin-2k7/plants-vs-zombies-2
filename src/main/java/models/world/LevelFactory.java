package models.world;

import models.enums.Chapter;
import models.enums.LevelType;
import models.enums.PlantType;
import models.plant.card.PlantCard;
import models.world.ChapterWorld.AncientEgyptWorld;
import models.world.ChapterWorld.BigWaveBeachWorld;
import models.world.ChapterWorld.DarkAgesWorld;
import models.world.ChapterWorld.FrostbiteCavesWorld;
import models.world.levelSetup.*;
import models.world.loseCondition.*;
import models.world.mechanics.*;
import models.world.winCondition.NormalWin;
import models.world.winCondition.TimedWarWin;
import models.world.winCondition.WinCondition;
import models.zombie.wave.Wave;
import models.zombie.wave.WaveManager;
import models.zombie.wave.WaveSpawnEntry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelFactory {

    public static GameWorld createLevel(Chapter chapter, int level){
        return switch (chapter) {
            case EGYPT -> switch (level) {
                case 1 -> createAncientEgyptLevel1();
                case 2 -> createAncientEgyptLevel2();
                case 3 -> createAncientEgyptLevel3();
                default -> throw new IllegalArgumentException("invalid level for egypt chapter");
            };
            case BIG_WAVE_BEACH -> switch (level) {
                case 1 -> createBigWaveBeachLevel1();
                case 2 -> createBigWaveBeachLevel2();
                case 3 -> createBigWaveBeachLevel3();
                default -> throw new IllegalArgumentException("invalid level for big wave beach chapter");
            };
            case DARK_AGES -> switch (level) {
                case 1 -> createDarkAgesLevel1();
                case 2 -> createDarkAgesLevel2();
                case 3 -> createDarkAgesLevel3();
                default -> throw new IllegalArgumentException("invalid level for dark ages");
            };
            case FROSTBITE_CAVES -> switch (level) {
                case 1 -> createFrostbiteCavesLevel1();
                case 2 -> createFrostbiteCavesLevel2();
                case 3 -> createFrostbiteCavesLevel3();
                default -> throw new IllegalArgumentException("invalid level for frostbite caves");
            };
        };
    }

    private static GameWorld createAncientEgyptLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)   // کاراکتر اضافی حذف شد
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        AncientEgyptWorld world = new AncientEgyptWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.EGYPT);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createAncientEgyptLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new DeadLineLevelSetup(rows, cols, 4, waves);
        LoseCondition loseCondition = new DeadLineLose(4);
        WinCondition winCondition = new NormalWin();

        AncientEgyptWorld world = new AncientEgyptWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.EGYPT);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createAncientEgyptLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        List<PlantCard> plantCards = List.of(
                new PlantCard(PlantType.PEASHOOTER, 0, 5)
        );

        LevelSetup levelSetup = new ConveyorLevelSetup(rows, cols, waves, plantCards);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        AncientEgyptWorld world = new AncientEgyptWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.EGYPT);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createAncientEgyptLevel4(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        AncientEgyptWorld world = new AncientEgyptWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.EGYPT);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createBigWaveBeachLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        BigWaveBeachWorld world = new BigWaveBeachWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.BIG_WAVE_BEACH);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createBigWaveBeachLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        Map<Point, PlantType> protectedPlants = Map.of(
                new Point(2, 1), PlantType.WALL_NUT,
                new Point(4, 3), PlantType.SUNFLOWER,
                new Point(6, 2), PlantType.WALL_NUT
        );

        LevelSetup levelSetup = new SaveOurSeedsLevelSetup(rows, cols, waves, protectedPlants);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        BigWaveBeachWorld world = new BigWaveBeachWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.BIG_WAVE_BEACH);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createBigWaveBeachLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        TimedWarLose loseCondition = new TimedWarLose(5000, 12);
        WinCondition winCondition = new TimedWarWin(loseCondition);

        BigWaveBeachWorld world = new BigWaveBeachWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.registerZombieKillListener(loseCondition::onZombieKilled);
        world.setCurrentChapter(Chapter.BIG_WAVE_BEACH);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createBigWaveBeachLevel4(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        AncientEgyptWorld world = new AncientEgyptWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.BIG_WAVE_BEACH);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createDarkAgesLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        ArrayList<Mechanic> mechanics = new ArrayList<>();
        mechanics.add(new DarkAgesMechanic());

        DarkAgesWorld world = new DarkAgesWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                mechanics
        );
        world.setCurrentChapter(Chapter.DARK_AGES);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createDarkAgesLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NightOpsLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        ArrayList<Mechanic> mechanics = new ArrayList<>();
        mechanics.add(new DarkAgesMechanic());

        DarkAgesWorld world = new DarkAgesWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                mechanics
        );
        world.setCurrentChapter(Chapter.DARK_AGES);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createDarkAgesLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new LoveYourPlantsLose(5);
        WinCondition winCondition = new NormalWin();

        ArrayList<Mechanic> mechanics = new ArrayList<>();
        mechanics.add(new DarkAgesMechanic());

        DarkAgesWorld world = new DarkAgesWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                mechanics
        );
        world.setCurrentChapter(Chapter.DARK_AGES);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createDarkAgesLevel4(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        ArrayList<Mechanic> mechanics = new ArrayList<>();
        mechanics.add(new DarkAgesMechanic());

        DarkAgesWorld world = new DarkAgesWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                mechanics
        );
        world.setCurrentChapter(Chapter.DARK_AGES);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        FrostbiteCavesWorld world = new FrostbiteCavesWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.FROSTBITE_CAVES);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        List<PlantCard> availablePlants = List.of(
                new PlantCard(PlantType.PEASHOOTER, 100, 5)
        );

        LevelSetup levelSetup = new PlantWhatYouGetLevelSetup(rows, cols, waves, 500, availablePlants);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        FrostbiteCavesWorld world = new FrostbiteCavesWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.FROSTBITE_CAVES);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        FrostbiteCavesWorld world = new FrostbiteCavesWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.FROSTBITE_CAVES);
        world.setWillUnlockLevel(true);
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel4(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();

        AncientEgyptWorld world = new AncientEgyptWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.FROSTBITE_CAVES);
        return world;
    }
}