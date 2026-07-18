package models.miniGame;

import models.miniGame.IZombie.IZombieLevel;
import models.miniGame.vaseBreaker.VaseBreakerLevel;
import models.miniGame.vaseBreaker.VaseBreakerSetup;
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

public class MiniGameFactory {

    public static GameWorld createMiniGameLevel(MiniGames miniGame, int level){
        return switch (miniGame){
            case VASE_BREAKER -> switch (level){
                case 1 -> createVaseBreakerLevel1();
                case 2 -> createVaseBreakerLevel2();
                case 3 -> createVaseBreakerLevel3();
                default -> throw new IllegalArgumentException("invalid level for vase breaker");
            };

            case BOWLING -> switch (level){
                case 1 -> createBowlingLevel1();
                case 2 -> createBowlingLevel2();
                case 3 -> createBowlingLevel3();
                default -> throw new IllegalArgumentException("invalid level for Bowling");
            };

            case BEGHOULED -> switch (level){
                case 1 -> createBeghouledLevel1();
                case 2 -> createBeghouledLevel2();
                case 3 -> createBeghouledLevel3();
                default -> throw new IllegalArgumentException("invalid level for Beghouled");
            };

            case I_ZOMBIE -> switch (level){
                case 1 -> createIZombieLevel1();
                case 2 -> createIZombieLevel2();
                case 3 -> createIZombieLevel3();
                default -> throw new IllegalArgumentException("invalid level for I Zombie");
            };

            case ZOMBOTANY -> switch (level){
                case 1 -> createZombotanyLevel1();
                case 2 -> createZombotanyLevel2();
                case 3 -> createZombotanyLevel3();
                default -> throw new IllegalArgumentException("invalid level for egypt chapter");
            };
        };

    }

    private static GameWorld createVaseBreakerLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        LevelSetup levelSetup = new VaseBreakerSetup(rows, cols);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new VaseBreakerLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createVaseBreakerLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new VaseBreakerLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createVaseBreakerLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new VaseBreakerLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBowlingLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBowlingLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBowlingLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createIZombieLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new IZombieLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createIZombieLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new IZombieLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createIZombieLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new IZombieLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBeghouledLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBeghouledLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBeghouledLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createZombotanyLevel1(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createZombotanyLevel2(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createZombotanyLevel3(){
        int rows = 5;
        int cols = 9;

        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        List<Wave> waves = Wave.generateWaves(5, 200, availableZombies, 60);

        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();


        return new MiniGameLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

}
