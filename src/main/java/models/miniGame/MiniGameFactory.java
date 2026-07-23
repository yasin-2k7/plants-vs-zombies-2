package models.miniGame;

import models.core.App;
import models.core.User;
import models.enums.PlantType;
import models.miniGame.IZombie.IZombieLevel;
import models.miniGame.IZombie.IZombieLose;
import models.miniGame.IZombie.IZombieSetup;
import models.miniGame.IZombie.IZombieWin;
import models.miniGame.beghouled.BeghouledSetup;
import models.miniGame.beghouled.BeghouledWinCondition;
import models.miniGame.beghouled.PlantUpgrade;
import models.miniGame.bowling.BowlingSetup;
import models.miniGame.vaseBreaker.VaseBreakerLevel;
import models.miniGame.vaseBreaker.VaseBreakerSetup;
import models.miniGame.vaseBreaker.VaseBreakerWinCondition;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.levelSetup.NormalLevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.loseCondition.NormalLose;
import models.world.winCondition.NormalWin;
import models.world.winCondition.WinCondition;
import models.zombie.Zombie;
import models.zombie.ZombieFactory;
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

    private static GameWorld createVaseBreakerLevel1() {
        int rows = 5, cols = 9;

        List<String> normalVaseZombies = List.of("ZombieDefault");
        List<String> giantVaseZombies = List.of("ZombieGargantuar");
        List<PlantType> possiblePlants = List.of(PlantType.PEASHOOTER, PlantType.WALL_NUT);

        LevelSetup levelSetup = new VaseBreakerSetup(rows, cols, normalVaseZombies, giantVaseZombies, possiblePlants);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new VaseBreakerWinCondition();
        winCondition.setCurrentLevel(MiniGameLevels.VASE_BREAKER_1);


        return new VaseBreakerLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createVaseBreakerLevel2() {
        int rows = 5, cols = 9;

        List<String> normalVaseZombies = List.of("ZombieDefault", "ZombieArmor1");
        List<String> giantVaseZombies = List.of("ZombieGargantuar");
        List<PlantType> possiblePlants = List.of(PlantType.PEASHOOTER, PlantType.WALL_NUT, PlantType.SNOW_PEA);

        LevelSetup levelSetup = new VaseBreakerSetup(rows, cols, normalVaseZombies, giantVaseZombies, possiblePlants);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new VaseBreakerWinCondition();
        winCondition.setCurrentLevel(MiniGameLevels.VASE_BREAKER_2);


        return new VaseBreakerLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createVaseBreakerLevel3() {
        int rows = 5, cols = 9;

        List<String> normalVaseZombies = List.of("ZombieArmor1", "ZombieWizard");
        List<String> giantVaseZombies = List.of("ZombieGargantuar", "ZombieDarkKing");
        List<PlantType> possiblePlants = List.of(PlantType.PEASHOOTER, PlantType.WALL_NUT, PlantType.SNOW_PEA, PlantType.CHOMPER);

        LevelSetup levelSetup = new VaseBreakerSetup(rows, cols, normalVaseZombies, giantVaseZombies, possiblePlants);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new VaseBreakerWinCondition();
        winCondition.setCurrentLevel(MiniGameLevels.VASE_BREAKER_3);


        return new VaseBreakerLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBowlingLevel1() {
        int rows = 5, cols = 9, redLineCol = 3;

        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)

        );

        LevelSetup levelSetup = new BowlingSetup(rows, cols, redLineCol, zombies, 1, 500);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();
        winCondition.setCurrentLevel(MiniGameLevels.BOWLING_1);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBowlingLevel2() {
        int rows = 5, cols = 9, redLineCol = 3;

        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100),
                new WaveSpawnEntry("ZombieArmor1", 150)
        );

        LevelSetup levelSetup = new BowlingSetup(rows, cols, redLineCol, zombies, 4, 250);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();
        winCondition.setCurrentLevel(MiniGameLevels.BOWLING_2);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBowlingLevel3() {
        int rows = 5, cols = 9, redLineCol = 3;

        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombieArmor1", 150),
                new WaveSpawnEntry("ZombieGargantuar", 400)
        );

        LevelSetup levelSetup = new BowlingSetup(rows, cols, redLineCol, zombies, 5, 350);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();
        winCondition.setCurrentLevel(MiniGameLevels.BOWLING_3);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createIZombieLevel1() {
        List<Zombie> availableZombies = List.of(
                new ZombieFactory().createZombie("ZombieDefault")
        );

        LevelSetup levelSetup = new IZombieSetup(5, 9, availableZombies);
        LoseCondition loseCondition = new IZombieLose();
        WinCondition winCondition = new IZombieWin();
        winCondition.setCurrentLevel(MiniGameLevels.I_ZOMBIE_1);

        return new IZombieLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createIZombieLevel2() {
        List<Zombie> availableZombies = List.of(
                new ZombieFactory().createZombie("ZombieDefault"),
                new ZombieFactory().createZombie("ZombieArmor1")
        );

        LevelSetup levelSetup = new IZombieSetup(5, 9, availableZombies);
        LoseCondition loseCondition = new IZombieLose();
        WinCondition winCondition = new IZombieWin();
        winCondition.setCurrentLevel(MiniGameLevels.I_ZOMBIE_2);

        return new IZombieLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createIZombieLevel3() {
        List<Zombie> availableZombies = List.of(
                new ZombieFactory().createZombie("ZombieArmor1"),
                new ZombieFactory().createZombie("ZombieGargantuar")
        );

        LevelSetup levelSetup = new IZombieSetup(5, 9, availableZombies);
        LoseCondition loseCondition = new IZombieLose();
        WinCondition winCondition = new IZombieWin();
        winCondition.setCurrentLevel(MiniGameLevels.I_ZOMBIE_3);

        return new IZombieLevel(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createBeghouledLevel1() {
        List<PlantType> plants = List.of(
                PlantType.PEASHOOTER, PlantType.SUNFLOWER, PlantType.WALL_NUT,
                PlantType.CABBAGE_PULT, PlantType.MELON_PULT
        );


        List<PlantUpgrade> upgrades = List.of(
                new PlantUpgrade(PlantType.PEASHOOTER, PlantType.REPEATER, 500),
                new PlantUpgrade(PlantType.WALL_NUT, PlantType.TALL_NUT, 500)
        );

        ensurePlantsUnlocked(plants);
        ensurePlantsUnlocked(upgrades.stream().map(PlantUpgrade::getTo).toList());

        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100)
        );

        LevelSetup levelSetup = new BeghouledSetup(5, 9, plants, upgrades, 5, zombies);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new BeghouledWinCondition();
        winCondition.setCurrentLevel(MiniGameLevels.BEGHOULED_1);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createBeghouledLevel2() {
        List<PlantType> plants = List.of(
                PlantType.PEASHOOTER, PlantType.SUNFLOWER, PlantType.WALL_NUT,
                PlantType.CABBAGE_PULT, PlantType.MELON_PULT
        );
        List<PlantUpgrade> upgrades = List.of(
                new PlantUpgrade(PlantType.PEASHOOTER, PlantType.REPEATER, 500),
                new PlantUpgrade(PlantType.WALL_NUT, PlantType.TALL_NUT, 500),
                new PlantUpgrade(PlantType.CABBAGE_PULT, PlantType.MELON_PULT, 1000)
        );
        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100),
                new WaveSpawnEntry("ZombieArmor1", 150)
        );

        LevelSetup levelSetup = new BeghouledSetup(5, 9, plants, upgrades, 8, zombies);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new BeghouledWinCondition();
        winCondition.setCurrentLevel(MiniGameLevels.BEGHOULED_2);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );

    }

    private static GameWorld createBeghouledLevel3() {
        List<PlantType> plants = List.of(
                PlantType.PEASHOOTER, PlantType.SUNFLOWER, PlantType.WALL_NUT,
                PlantType.CABBAGE_PULT, PlantType.MELON_PULT
        );
        List<PlantUpgrade> upgrades = List.of(
                new PlantUpgrade(PlantType.PEASHOOTER, PlantType.REPEATER, 500),
                new PlantUpgrade(PlantType.WALL_NUT, PlantType.TALL_NUT, 500),
                new PlantUpgrade(PlantType.CABBAGE_PULT, PlantType.MELON_PULT, 1000),
                new PlantUpgrade(PlantType.MELON_PULT, PlantType.WINTER_MELON, 750)
        );
        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombieArmor1", 150),
                new WaveSpawnEntry("ZombieGargantuar", 400)
        );

        LevelSetup levelSetup = new BeghouledSetup(5, 9, plants, upgrades, 12, zombies);
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new BeghouledWinCondition();
        winCondition.setCurrentLevel(MiniGameLevels.BEGHOULED_3);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createZombotanyLevel1() {
        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombiePeashooter", 150),
                new WaveSpawnEntry("ZombieWallnut", 200)
        );

        LevelSetup levelSetup = new models.world.levelSetup.NormalLevelSetup(5, 9, models.zombie.wave.Wave.generateWaves(3, 200, zombies, 40));
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();
        winCondition.setCurrentLevel(MiniGameLevels.ZOMBOTANY_1);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createZombotanyLevel2() {
        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombiePeashooter", 150),
                new WaveSpawnEntry("ZombieWallnut", 200),
                new WaveSpawnEntry("ZombieSquash", 150)
        );

        LevelSetup levelSetup = new models.world.levelSetup.NormalLevelSetup(5, 9, models.zombie.wave.Wave.generateWaves(4, 300, zombies, 40));
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();
        winCondition.setCurrentLevel(MiniGameLevels.ZOMBOTANY_2);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static GameWorld createZombotanyLevel3() {
        List<WaveSpawnEntry> zombies = List.of(
                new WaveSpawnEntry("ZombiePeashooter", 150),
                new WaveSpawnEntry("ZombieWallnut", 200),
                new WaveSpawnEntry("ZombieJalapeno", 150),
                new WaveSpawnEntry("ZombieSquash", 150)
        );

        LevelSetup levelSetup = new models.world.levelSetup.NormalLevelSetup(5, 9, models.zombie.wave.Wave.generateWaves(5, 400, zombies, 40));
        LoseCondition loseCondition = new NormalLose();
        WinCondition winCondition = new NormalWin();
        winCondition.setCurrentLevel(MiniGameLevels.ZOMBOTANY_3);

        return new MiniGameWorld(
                levelSetup,
                new ArrayList<>(List.of(loseCondition)),
                winCondition,
                new ArrayList<>()
        );
    }

    private static void ensurePlantsUnlocked(List<PlantType> plantTypes) {
        User user = App.getCurrentUser();
        for (PlantType type : plantTypes) {
            if (!user.getUnlockedPlantsLevels().containsKey(type)) {
                user.unlockPlant(type);
            }
        }
    }

}
