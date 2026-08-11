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
                default -> throw new IllegalArgumentException("invalid level for egypt chapter");
            };
            case BIG_WAVE_BEACH -> switch (level) {
                case 1 -> createBigWaveBeachLevel1();
                case 2 -> createBigWaveBeachLevel2();
                case 3 -> createBigWaveBeachLevel3();
                default ->
                        throw new IllegalArgumentException("invalid level for big wave beach chapter");
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

    private static GameWorld createAncientEgyptLevel1() {
        int rows = 5;
        int cols = 9;
        List<WaveSpawnEntry> availableZombies = List.of(
                new WaveSpawnEntry("ZombieDefault", 100),
                new WaveSpawnEntry("ZombieArmor1", 200),
                new WaveSpawnEntry("ZombieArmor2", 300),
                new WaveSpawnEntry("ZombieArmor4", 400),
                new WaveSpawnEntry("ZombieTombRaiser", 300),
                new WaveSpawnEntry("ZombieRa", 100),
                new WaveSpawnEntry("ZombieNewspaper", 450),
                new WaveSpawnEntry("ZombieExplorer", 250)
        );
        List<Wave> waves = Wave.generateWaves(3, 500, availableZombies, 20);
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
            "Greetings, neighbor! I'm Crazy Dave!",
            "Why do they call me Crazy Dave? Because I put a pot on my head!",
            "Look! Mummies are coming to eat your hot sauce!",
            "Plant these Peashooters or they will chew on your toes!",
            "WABBA WABBA RAAGH!"
        ));
        world.setWinningDialogs(List.of(
            "YEEHAW! We saved my taco! Er... I mean, your brains!",
            "Those zombies didn't stand a chance against my favorite pot!",
            "Good job neighbor! Let's eat some victory tacos!"
        ));

        world.setLosingDialogs(List.of(
            "NOOO! They ate your brains!",
            "And worse... they didn't leave any hot sauce for my taco!",
            "Looks like you need more Peashooters... and a time machine!",
            "WAAABBAAA!"
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
            "Whoa! See that red flower line on the sand?",
            "If a zombie steps over that line, my taco will fall on the ground!",
            "DON'T LET THEM CROSS IT!"
        ));
        world.setWinningDialogs(List.of(
            "Phew! The red line is safe!",
            "My taco didn't get sandy! You're the best neighbor!"
        ));
        world.setLosingDialogs(List.of(
            "Oh no! They crossed the line!",
            "My taco is sandy... and so are my brains!",
            "WAAAAAH!"
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
            "Conveyor Belt action! Free plants incoming!",
            "It's like a drive-thru, but green and angry!",
            "Grab 'em fast and place 'em CRAAAZY!"
        ));
        world.setWinningDialogs(List.of(
            "That conveyor belt was faster than my grandma on a scooter!",
            "Great job catching all those plants!"
        ));
        world.setLosingDialogs(List.of(
            "Too many zombies, not enough belt!",
            "We should have ordered the extra-large Peashooter!"
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
            "One last push, neighbor!",
            "The mummies brought their big sandy friends!",
            "Let's show them the power of green!"
        ));
        world.setWinningDialogs(List.of(
            "Take that, you dusty old bandages!",
            "Egypt is safe! Next stop: the beach!"
        ));
        world.setLosingDialogs(List.of(
            "I guess we are mummies now...",
            "Wrap me up in toilet paper and call me Dave-hotep!"
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
                new WaveSpawnEntry("ZombieBeachOctopus", 800)
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
            "Surf's up, neighbor! Welcome to Big Wave Beach!",
            "Don't forget your sunscreen... and your octo-repellent!",
            "Zombies here love seafood and BRAINS!"
        ));
        world.setWinningDialogs(List.of(
            "Tubular! We rode that wave perfectly!",
            "The octopuses are retreating back to the deep!"
        ));
        world.setLosingDialogs(List.of(
            "Wipeout!",
            "They surfed right over our defenses!",
            "My brain is completely waterlogged!"
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
            "Look at those endangered plants in the sand!",
            "They are like my pet rocks, but greener!",
            "Protect them with your life... or with Wall-nuts!"
        ));
        world.setWinningDialogs(List.of(
            "The endangered plants survived! Look at them go!",
            "Did you notice they are fully animated now?",
            "Yup, a proper live flower animation, not just a static picture!"
        ));
        world.setLosingDialogs(List.of(
            "Nooo! My pet rocks... I mean plants!",
            "They ate the endangered species!",
            "Call the plant police!"
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
            "Tick-tock! The tide is coming in FAST!",
            "Defeat those zombies before the clock runs out!",
            "GO GO GO!"
        ));
        world.setWinningDialogs(List.of(
            "Beat the clock! You're faster than a greased pig!",
            "Take that, Father Time!"
        ));
        world.setLosingDialogs(List.of(
            "Time's up! The tide washed everything away!",
            "My watch stopped... and so did my heart!"
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
            "The ultimate beach party!",
            "The zombies brought the boombox... and the teeth!",
            "Don't forget your swimming trunks!"
        ));
        world.setWinningDialogs(List.of(
            "Best beach party ever!",
            "We brought the house down! Or at least the sandcastle!"
        ));
        world.setLosingDialogs(List.of(
            "Party foul! They ate the host!",
            "I'm getting sand everywhere..."
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
            "Spooky! It's dark and scary out here!",
            "No sun drops from the sky at night!",
            "Use Mushrooms, neighbor! They thrive in the shadow!"
        ));
        world.setWinningDialogs(List.of(
            "Who's afraid of the dark? Not us!",
            "Take your magic tricks back to the circus, wizards!"
        ));
        world.setLosingDialogs(List.of(
            "It's too dark! I can't see my taco!",
            "They turned me into a toad! Wait, no, just a zombie."
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
            "Night Ops! Watch out for Wizard Zombies!",
            "They turn my favorite plants into CAT!",
            "MEAWWWW! See?!"
        ));
        world.setWinningDialogs(List.of(
            "MEOW! Oh wait, the cats turned back into plants!",
            "Good job breaking the spell, neighbor!"
        ));
        world.setLosingDialogs(List.of(
            "Meow? Meow meow meow!",
            "I guess I'm Crazy Cat Dave now!"
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
            "Love your plants, neighbor!",
            "Don't lose more than 5 plants or I'll cry into my pot!",
            "BE CAREFUL!"
        ));
        world.setWinningDialogs(List.of(
            "You really DO love your plants!",
            "Not a single leaf was harmed! Okay, maybe a few."
        ));
        world.setLosingDialogs(List.of(
            "You broke my heart!",
            "And my pot!",
            "Too many plant casualties! Retreat!"
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
            "The darkest night of all!",
            "I hear clanking armor!",
            "Did somebody order a dragon?"
        ));
        world.setWinningDialogs(List.of(
            "The sun is rising! We survived the night!",
            "Time for breakfast tacos!"
        ));
        world.setLosingDialogs(List.of(
            "Goodnight, neighbor...",
            "The dark ages just got a lot darker."
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
            "BRRRR! My pot is frozen to my head!",
            "The icy wind will freeze your plants solid!",
            "Use warm plants to melt the ice!"
        ));
        world.setWinningDialogs(List.of(
            "We melted their icy hearts!",
            "My pot is finally unfrozen!"
        ));
        world.setLosingDialogs(List.of(
            "Brrr! I'm shivering!",
            "They turned us into brain-flavored popsicles!"
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
            "You get what you get and you don't get upset!",
            "Use the provided seed cards carefully!"
        ));
        world.setWinningDialogs(List.of(
            "You made a gourmet meal out of leftovers!",
            "Who knew Peashooters were so versatile?"
        ));
        world.setLosingDialogs(List.of(
            "I guess we needed better seeds...",
            "Don't blame me, blame the RNG!"
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
            "Locked plants challenge!",
            "No changing your lineup! Show those cave zombies who's boss!"
        ));
        world.setWinningDialogs(List.of(
            "A solid lineup for a solid victory!",
            "You cracked the code, neighbor!"
        ));
        world.setLosingDialogs(List.of(
            "Should have picked a different strategy!",
            "Back to the drawing board... if it wasn't frozen!"
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
            "The final freeze!",
            "The Yeti is coming... maybe? I don't know!",
            "Stay warm, neighbor!"
        ));
        world.setWinningDialogs(List.of(
            "We conquered the ice age!",
            "Let's go home and turn up the thermostat!"
        ));
        world.setLosingDialogs(List.of(
            "We are officially fossils now.",
            "Tell future archeologists I loved tacos."
        ));
        return world;
    }
}
