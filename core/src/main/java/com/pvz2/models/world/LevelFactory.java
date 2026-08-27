package com.pvz2.models.world;

import com.pvz2.models.enums.Chapter;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.world.ChapterWorld.AncientEgyptWorld;
import com.pvz2.models.world.ChapterWorld.BigWaveBeachWorld;
import com.pvz2.models.world.ChapterWorld.DarkAgesWorld;
import com.pvz2.models.world.ChapterWorld.FrostbiteCavesWorld;
import com.pvz2.models.world.levelSetup.*;
import com.pvz2.models.world.loseCondition.*;
import com.pvz2.models.world.mechanics.DarkAgesMechanic;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.winCondition.NormalWin;
import com.pvz2.models.world.winCondition.TimedWarWin;
import com.pvz2.models.world.winCondition.WinCondition;
import com.pvz2.models.zombie.wave.Wave;
import com.pvz2.models.zombie.wave.WaveSpawnEntry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelFactory {
    public static GameWorld createLevel(Chapter chapter, int level) {
        return switch (chapter) {
            case EGYPT -> switch (level) {
                case 1 -> createAncientEgyptLevel1();
                case 2 -> createAncientEgyptLevel2();
                case 3 -> createAncientEgyptLevel3();
                case 4 -> createAncientEgyptLevel4();
                default -> throw new IllegalArgumentException("invalid level for egypt chapter");
            };
            case BIG_WAVE_BEACH -> switch (level) {
                case 1 -> createBigWaveBeachLevel1();
                case 2 -> createBigWaveBeachLevel2();
                case 3 -> createBigWaveBeachLevel3();
                case 4 -> createBigWaveBeachLevel4();
                default ->
                    throw new IllegalArgumentException("invalid level for big wave beach chapter");
            };
            case DARK_AGES -> switch (level) {
                case 1 -> createDarkAgesLevel1();
                case 2 -> createDarkAgesLevel2();
                case 3 -> createDarkAgesLevel3();
                case 4 -> createDarkAgesLevel4();
                default -> throw new IllegalArgumentException("invalid level for dark ages");
            };
            case FROSTBITE_CAVES -> switch (level) {
                case 1 -> createFrostbiteCavesLevel1();
                case 2 -> createFrostbiteCavesLevel2();
                case 3 -> createFrostbiteCavesLevel3();
                case 4 -> createFrostbiteCavesLevel4();
                default -> throw new IllegalArgumentException("invalid level for frostbite caves");
            };
        };
    }

    private static GameWorld createAncientEgyptLevel1() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 250),
            new WaveSpawnEntry("ZombieArmor1", 500),
            new WaveSpawnEntry("ZombieArmor2", 630),
            new WaveSpawnEntry("ZombieArmor4", 650),
            new WaveSpawnEntry("ZombieTombRaiser", 510),
            new WaveSpawnEntry("ZombieRa", 505),
            new WaveSpawnEntry("ZombieNewspaper", 550),
            new WaveSpawnEntry("ZombieExplorer", 510)
        );
        List<Wave> waves = Wave.generateWaves(10, 499, availableZombies, 1);
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
        world.setStartingDialogs(List.of(
            "DAVE:Greetings, neighbor! I'm Crazy Dave!",
            "PENNY:User Dave, my scanners detect a 99.9% probability of an imminent Zombie attack.",
            "DAVE:Why do they call me Crazy Dave? Because I put a pot on my head!",
            "PENNY:That... does not logically correlate to the current threat level.",
            "DAVE:Look! Mummies are coming to eat your hot sauce! Plant these Peashooters!",
            "DAVE:WABBA WABBA RAAGH!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:YEEHAW! We saved my taco! Er... I mean, your brains!",
            "PENNY:Tactical defense successful. Brain integrity remains at 100%.",
            "DAVE:Good job neighbor! Let's eat some victory tacos!"
        ));

        world.setLosingDialogs(List.of(
            "DAVE:NOOO! They ate your brains!",
            "PENNY:Critical failure. Temporal retreat advised.",
            "DAVE:And worse... they didn't leave any hot sauce for my taco!"
        ));
        return world;
    }

    private static GameWorld createAncientEgyptLevel2() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieArmor4", 400),
            new WaveSpawnEntry("ZombieTombRaiser", 300),
            new WaveSpawnEntry("ZombieRa", 100),
            new WaveSpawnEntry("ZombieExplorer", 250),
            new WaveSpawnEntry("ZombiePiano", 450)
        );
        List<Wave> waves = Wave.generateWaves(4, 500, availableZombies, 20);
        LevelSetup levelSetup = new DeadLineLevelSetup(rows, cols, 4, waves);
        LoseCondition loseCondition = new DeadLineLose(4);
        System.out.println("if zombie pass deadLine (col = 4), you will lose");
        WinCondition winCondition = new NormalWin();
        AncientEgyptWorld world = new AncientEgyptWorld(
            levelSetup,
            new ArrayList<>(List.of(loseCondition)),
            winCondition,
            new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.EGYPT);
        world.setWillUnlockLevel(true);
        world.setStartingDialogs(List.of(
            "DAVE:Whoa! See that red flower line on the sand?",
            "DAVE:If a zombie steps over that line, my taco will fall on the ground!",
            "DAVE:DON'T LET THEM CROSS IT!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:My taco didn't get sandy! You're the best neighbor!"
        ));
        world.setLosingDialogs(List.of(
            "DAVE:Oh no! They crossed the line! My taco is sandy... and so are my brains!",
            "DAVE:WAAAAAH!"
        ));
        return world;
    }

    private static GameWorld createAncientEgyptLevel3() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieArmor4", 400),
            new WaveSpawnEntry("ZombieTombRaiser", 300),
            new WaveSpawnEntry("ZombieRa", 100),
            new WaveSpawnEntry("ZombieExplorer", 250),
            new WaveSpawnEntry("ZombieGargantuar", 700),
            new WaveSpawnEntry("ZombieBarrelRoller", 500)

        );
        List<Wave> waves = Wave.generateWaves(5, 500, availableZombies, 20);
        List<PlantCard> plantCards = List.of(
            new PlantCard(PlantType.PEASHOOTER, 0, 0),
            new PlantCard(PlantType.PEA_POD, 0, 0),
            new PlantCard(PlantType.WALL_NUT, 0, 0),
            new PlantCard(PlantType.STARFRUIT, 0, 0),
            new PlantCard(PlantType.XSHOT, 0, 0),
            new PlantCard(PlantType.CHERRY_BOMB, 0, 0),
            new PlantCard(PlantType.MELON_PULT, 0, 0),
            new PlantCard(PlantType.SQUASH, 0, 0),
            new PlantCard(PlantType.JALAPENO, 0, 0),
            new PlantCard(PlantType.SNOW_PEA, 0, 0),
            new PlantCard(PlantType.REPEATER, 0, 0)
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
        world.setStartingDialogs(List.of(
            "PENNY:Analyzing anomaly: localized temporal distortion is providing flora directly.",
            "PENNY:Deploy them rapidly to maintain defensive integrity."
        ));
        world.setWinningDialogs(List.of(
            "PENNY:Anomaly resolved. Great botanical efficiency, neighbor."
        ));
        world.setLosingDialogs(List.of(
            "PENNY:Botanical delivery system overwhelmed."
        ));
        return world;
    }

    private static GameWorld createAncientEgyptLevel4() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100)
        );
        List<Wave> waves = Wave.generateWaves(5, 500, availableZombies, 20);
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
        world.setStartingDialogs(List.of(
            "PENNY:Scanning indicates a massive wave of undead approaching.",
            "DAVE:One last push, neighbor! The mummies brought their big sandy friends!",
            "DAVE:Let's show them the power of green!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:Take that, you dusty old bandages!",
            "PENNY:Era secured. Preparing temporal shift.",
            "DAVE:Egypt is safe! Next stop: the beach!"
        ));
        world.setLosingDialogs(List.of(
            "DAVE:I guess we are mummies now...",
            "PENNY:Brain functions ceasing...",
            "DAVE:Wrap me up in toilet paper and call me Dave-hotep!"
        ));
        return world;
    }

    private static GameWorld createBigWaveBeachLevel1() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieLostCityJane", 200),
            new WaveSpawnEntry("ZombieBeachSnorkel", 200),
            new WaveSpawnEntry("ZombieBeachOctopus", 800),
            new WaveSpawnEntry("ZombieBeachFisherman", 400)
            );
        List<Wave> waves = Wave.generateWaves(3, 500, availableZombies, 10);
        LevelSetup levelSetup = new BigWaveBeachLevelSetup(6, rows, cols, waves);
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
        world.setStartingDialogs(List.of(
            "DAVE:Surf's up, neighbor! Welcome to Big Wave Beach!",
            "PENNY:Warning: Saltwater environment detected. Rust probability increasing.",
            "DAVE:Don't forget your sunscreen... and your octo-repellent!",
            "PENNY:Zombies here love seafood and BRAINS!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:Tubular! We rode that wave perfectly!",
            "PENNY:The local cephalopod threat is retreating."
        ));
        world.setLosingDialogs(List.of(
            "DAVE:Wipeout! They surfed right over our defenses!",
            "PENNY:Systems flooded. Glug glug glug...",
            "DAVE:My brain is completely waterlogged!"
        ));
        return world;
    }

    private static GameWorld createBigWaveBeachLevel2() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor4", 400),
            new WaveSpawnEntry("ZombieBeachFisherman", 400),
            new WaveSpawnEntry("ZombieBeachSnorkel", 200),
            new WaveSpawnEntry("ZombieModernAllStar", 500)
        );
        List<Wave> waves = Wave.generateWaves(4, 500, availableZombies, 10);
        Map<Point, PlantType> protectedPlants = Map.of(
            new Point(2, 1), PlantType.WALL_NUT,
            new Point(4, 3), PlantType.SUNFLOWER,
            new Point(6, 2), PlantType.WALL_NUT
        );
        LevelSetup levelSetup = new SaveOurSeedsLevelSetup(rows, cols, waves, protectedPlants);
        LoseCondition loseCondition = new SaveOurSeedsLose();
        LoseCondition loseCondition1 = new NormalLose();
        WinCondition winCondition = new NormalWin();
        BigWaveBeachWorld world = new BigWaveBeachWorld(
            levelSetup,
            new ArrayList<>(List.of(loseCondition, loseCondition1)),
            winCondition,
            new ArrayList<>()
        );
        world.setCurrentChapter(Chapter.BIG_WAVE_BEACH);
        world.setWillUnlockLevel(true);
        world.setStartingDialogs(List.of(
            "DAVE:Look at those endangered plants in the sand!",
            "DAVE:They are like my pet rocks, but greener!",
            "DAVE:Protect them with your life... or with Wall-nuts!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:The endangered plants survived! Look at them go!"
        ));
        world.setLosingDialogs(List.of(
            "DAVE:Nooo! My pet rocks... I mean plants!",
            "DAVE:Call the plant police!"
        ));
        return world;
    }

    private static GameWorld createBigWaveBeachLevel3() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieArmor4", 400),
            new WaveSpawnEntry("ZombieBeachFisherman", 400),
            new WaveSpawnEntry("ZombieBeachSnorkel", 200),
            new WaveSpawnEntry("ZombieBeachOctopus", 400),
            new WaveSpawnEntry("ZombieArcade", 400)
        );
        List<Wave> waves = Wave.generateWaves(5, 500, availableZombies, 20);
        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        TimedWarLose loseCondition = new TimedWarLose(5000, 12);
        LoseCondition loseCondition1 = new NormalLose();
        WinCondition winCondition = new TimedWarWin(loseCondition);
        BigWaveBeachWorld world = new BigWaveBeachWorld(
            levelSetup,
            new ArrayList<>(List.of(loseCondition, loseCondition1)),
            winCondition,
            new ArrayList<>()
        );
        world.registerZombieKillListener(loseCondition::onZombieKilled);
        world.setCurrentChapter(Chapter.BIG_WAVE_BEACH);
        world.setWillUnlockLevel(true);
        world.setStartingDialogs(List.of(
            "PENNY:Alert: Rising tide patterns indicate severe temporal constraints.",
            "PENNY:Eliminate hostile entities before the temporal window collapses."
        ));
        world.setWinningDialogs(List.of(
            "PENNY:Time constraints met successfully. Take that, Father Time!"
        ));
        world.setLosingDialogs(List.of(
            "PENNY:Time expired. Tides have washed everything away."
        ));
        return world;
    }

    private static GameWorld createBigWaveBeachLevel4() {
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
        world.setStartingDialogs(List.of(
            "DAVE:The ultimate beach party!",
            "PENNY:Detecting extreme hostile acoustic vibrations.",
            "DAVE:The zombies brought the boombox... and the teeth!",
            "DAVE:Don't forget your swimming trunks!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:Best beach party ever!",
            "PENNY:Hostiles eliminated. Sandcastle structural integrity intact."
        ));
        world.setLosingDialogs(List.of(
            "DAVE:Party foul! They ate the host!",
            "PENNY:Sensory overload. Shutting down.",
            "DAVE:I'm getting sand everywhere..."
        ));
        return world;
    }

    private static GameWorld createDarkAgesLevel1() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor4", 400),
            new WaveSpawnEntry("ZombieDarkJuggler", 450),
            new WaveSpawnEntry("ZombieWizard", 400),
            new WaveSpawnEntry("ZombieDarkKing", 500)
        );
        List<Wave> waves = Wave.generateWaves(3, 500, availableZombies, 20);
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
        world.setStartingDialogs(List.of(
            "DAVE:Spooky! It's dark and scary out here!",
            "PENNY:Solar energy severely depleted. No sun drops from the sky at night.",
            "DAVE:Use Mushrooms, neighbor! They thrive in the shadow!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:Who's afraid of the dark? Not us!",
            "PENNY:Magic-based hostiles successfully neutralized."
        ));
        world.setLosingDialogs(List.of(
            "DAVE:It's too dark! I can't see my taco!",
            "PENNY:Visual sensors compromised.",
            "DAVE:They turned me into a toad! Wait, no, just a zombie."
        ));
        return world;
    }

    private static GameWorld createDarkAgesLevel2() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieDarkImpDragon", 150),
            new WaveSpawnEntry("ZombieDarkJuggler", 450),
            new WaveSpawnEntry("ZombieWizard", 400),
            new WaveSpawnEntry("ZombieDarkKing", 500)
        );
        List<Wave> waves = Wave.generateWaves(4, 500, availableZombies, 20);
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
        world.setStartingDialogs(List.of(
            "DAVE:Night Ops! Watch out for Wizard Zombies!",
            "DAVE:They turn my favorite plants into SHEEP!",
            "DAVE:BAAAH! See?!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:Good job breaking the spell, neighbor!"
        ));
        world.setLosingDialogs(List.of(
            "DAVE:Baaah? Baaah baaah baaah!",
            "DAVE:I guess I'm Crazy Sheep Dave now!"
        ));
        return world;
    }

    private static GameWorld createDarkAgesLevel3() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieDarkArmor3", 450),
            new WaveSpawnEntry("ZombieDarkJuggler", 450),
            new WaveSpawnEntry("ZombieWizard", 400),
            new WaveSpawnEntry("ZombieDarkKing", 500)
        );
        List<Wave> waves = Wave.generateWaves(4, 500, availableZombies, 20);
        LevelSetup levelSetup = new NormalLevelSetup(rows, cols, waves);
        LoveYourPlantsLose loseCondition = new LoveYourPlantsLose(5);
        WinCondition winCondition = new NormalWin();
        ArrayList<Mechanic> mechanics = new ArrayList<>();
        mechanics.add(new DarkAgesMechanic());
        DarkAgesWorld world = new DarkAgesWorld(
            levelSetup,
            new ArrayList<>(List.of(loseCondition)),
            winCondition,
            mechanics
        );
        loseCondition.setGameListener(world);
        world.setCurrentChapter(Chapter.DARK_AGES);
        world.setWillUnlockLevel(true);
        world.setStartingDialogs(List.of(
            "PENNY:Restriction: Maximum acceptable botanical casualties is set to 5.",
            "PENNY:Warning: Exceeding this limit will result in catastrophic failure."
        ));
        world.setWinningDialogs(List.of(
            "PENNY:Casualty limit maintained. Not a single leaf was harmed... mostly."
        ));
        world.setLosingDialogs(List.of(
            "PENNY:Casualty limit exceeded. Retreating."
        ));
        return world;
    }

    private static GameWorld createDarkAgesLevel4() {
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
        world.setStartingDialogs(List.of(
            "PENNY:Ambient light levels reaching absolute zero.",
            "DAVE:The darkest night of all! I hear clanking armor!",
            "DAVE:Did somebody order a dragon?"
        ));
        world.setWinningDialogs(List.of(
            "PENNY:Solar radiation detected. Safe at last.",
            "DAVE:The sun is rising! We survived the night! Time for breakfast tacos!"
        ));
        world.setLosingDialogs(List.of(
            "DAVE:Goodnight, neighbor...",
            "PENNY:The dark ages just got a lot darker."
        ));
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel1() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieArmor4", 400),
            new WaveSpawnEntry("ZombieIceAgeDodo", 400),
            new WaveSpawnEntry("ZombieIceAgeHunter", 300),
            new WaveSpawnEntry("ZombieIceAgeTroglobite", 400)

        );
        List<Wave> waves = Wave.generateWaves(3, 500, availableZombies, 20);
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
        world.setStartingDialogs(List.of(
            "DAVE:BRRRR! My pot is frozen to my head!",
            "PENNY:Extreme sub-zero temperatures detected. Flora freezing probability is high.",
            "DAVE:Use warm plants to melt the ice!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:We melted their icy hearts!",
            "PENNY:Thermal equilibrium restored. My engine block is thawing."
        ));
        world.setLosingDialogs(List.of(
            "DAVE:Brrr! I'm shivering!",
            "PENNY:Core temperature critical. We are becoming popsicles."
        ));
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel2() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieCrystalSkull", 400),
            new WaveSpawnEntry("ZombieIceAgeDodo", 400),
            new WaveSpawnEntry("ZombieIceAgeHunter", 300),
            new WaveSpawnEntry("ZombieIceAgeTroglobite", 400)
        );
        List<Wave> waves = Wave.generateWaves(4, 500, availableZombies, 20);
        List<PlantCard> availablePlants = List.of(
            new PlantCard(PlantType.PEASHOOTER, 100, 5)
        );
        LevelSetup levelSetup = new PlantWhatYouGetLevelSetup(rows, cols, waves, availablePlants);
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
        world.setStartingDialogs(List.of(
            "DAVE:You get what you get and you don't get upset!",
            "DAVE:Use the provided seed cards carefully!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:You made a gourmet meal out of leftovers!"
        ));
        world.setLosingDialogs(List.of(
            "DAVE:I guess we needed better seeds..."
        ));
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel3() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
            new WaveSpawnEntry("ZombieDefault", 100),
            new WaveSpawnEntry("ZombieArmor1", 200),
            new WaveSpawnEntry("ZombieArmor2", 300),
            new WaveSpawnEntry("ZombieCrystalSkull", 400),
            new WaveSpawnEntry("ZombieIceAgeDodo", 400),
            new WaveSpawnEntry("ZombieIceAgeHunter", 300),
            new WaveSpawnEntry("ZombieProspector", 200)
        );
        List<Wave> waves = Wave.generateWaves(5, 500, availableZombies, 20);
        LevelSetup levelSetup = new LockedPlantsLevelSetup(rows, cols, waves);
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
        world.setStartingDialogs(List.of(
            "PENNY:Botanical loadout has been locked.",
            "PENNY:Please proceed with the current pre-configured strategy."
        ));
        world.setWinningDialogs(List.of(
            "PENNY:Strategy validation complete. You cracked the code, neighbor."
        ));
        world.setLosingDialogs(List.of(
            "PENNY:Back to the drawing board... if it wasn't frozen!"
        ));
        return world;
    }

    private static GameWorld createFrostbiteCavesLevel4() {
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
        world.setStartingDialogs(List.of(
            "DAVE:The final freeze!",
            "PENNY:Scanning for massive cryogenic entities...",
            "DAVE:The Yeti is coming... maybe? I don't know!",
            "DAVE:Stay warm, neighbor!"
        ));
        world.setWinningDialogs(List.of(
            "DAVE:We conquered the ice age!",
            "PENNY:Ice Age bypassed. Let's go home and turn up the thermostat!"
        ));
        world.setLosingDialogs(List.of(
            "DAVE:We are officially fossils now.",
            "PENNY:Preserved in ice for future archaeologists.",
            "DAVE:Tell them I loved tacos."
        ));
        return world;
    }
}
