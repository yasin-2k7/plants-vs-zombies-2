package models.zombie;

import models.core.App;
import models.core.DifficultyCalculator;
import models.enums.Zombies;
import models.miniGame.zombotany.JalapenoZombie;
import models.miniGame.zombotany.PeashooterZombie;
import models.miniGame.zombotany.SquashZombie;
import models.miniGame.zombotany.WallnutZombie;
import models.zombie.data.ArmorData;
import models.zombie.data.ArmorProperties;
import models.zombie.data.ZombieData;
import models.zombie.data.ZombieProperties;
import models.zombie.zombiesType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ZombieFactory {
    private static final Logger logger = Logger.getLogger(ZombieFactory.class.getName());
    private static final Map<Zombies, String> enumToAlias = new HashMap<>();
    static {
        enumToAlias.put(Zombies.ZOMBIE, "ZombieDefault");
        enumToAlias.put(Zombies.ARMORED, "ZombieArmor1");
        enumToAlias.put(Zombies.WIZARD, "ZombieWizard");
        enumToAlias.put(Zombies.SUN_STEALER, "ZombieRa");
        enumToAlias.put(Zombies.SPAWNER, "ZombieGargantuar");
        enumToAlias.put(Zombies.SNORKEL, "ZombieBeachSnorkel");
        enumToAlias.put(Zombies.RANGED, "ZombieIceAgeHunter");
        enumToAlias.put(Zombies.PUSHER, "ZombieArcade");
        enumToAlias.put(Zombies.PHASING, "ZombieNewspaper");
        enumToAlias.put(Zombies.IMP, "ZombieImp");
        enumToAlias.put(Zombies.FISHERMAN, "ZombieBeachFisherman");
        enumToAlias.put(Zombies.ELEMENTAL, "ZombieExplorer");
        enumToAlias.put(Zombies.DODO_RIDER, "ZombieIceAgeDodo");
        enumToAlias.put(Zombies.DEFLECTOR, "ZombieDarkJuggler");
        enumToAlias.put(Zombies.PEASHOOTER_ZOMBIE, "ZombiePeashooter");
        enumToAlias.put(Zombies.JALAPENO_ZOMBIE, "ZombieJalapeno");
        enumToAlias.put(Zombies.WALLNUT_ZOMBIE, "ZombieWallnut");
        enumToAlias.put(Zombies.SQUASH_ZOMBIE, "ZombieSquash");
    }

    public Zombie createZombie(String alias) {
        Zombie zombotanyZombie = createZombotanyZombie(alias);
        if (zombotanyZombie != null) {
            return zombotanyZombie;
        }

        ZombieProperties props = ZombieRegistry.getZombieProperties(alias);
        if (props == null) {
            logger.warning("Unknown zombie alias: " + alias);
            return null;
        }
        String objclass = props.getObjclass();
        ZombieData data = props.getObjdata();

        int difficulty = App.getCurrentUser().getGameDifficulty();
        double increaseFactor = DifficultyCalculator.increaseFactor(difficulty);

        int health = (int) Math.round(data.getHitpoints() * increaseFactor);
        int eatDPS = (int) Math.round(data.getEatDPS() * increaseFactor);
        double speed = data.getSpeed();

        Zombie createdZombie = null;

        if ("ZombieDarkImpDragon".equalsIgnoreCase(alias)) {
            createdZombie = new ImpZombie(health, speed, eatDPS, true);
        } else if ("ZombieImp".equalsIgnoreCase(alias)) {
            createdZombie = new ImpZombie(health, speed, eatDPS, false);
        } else {

            switch (objclass) {
                case "ZombiePropertySheet":
                    createdZombie = buildBasicZombie(health, eatDPS, speed, data);
                    break;
                case "ZombieGargantuarProps":
                    createdZombie = new SpawnerZombie(health, speed, eatDPS, true);
                    break;
                case "ZombieRaProps":
                    createdZombie = new SunStealerZombie(health, speed, eatDPS, true);
                    break;
                case "ZombieCrystalSkullProps":
                    createdZombie = new SunStealerZombie(health, speed, eatDPS, false);
                    break;
                case "ZombieExplorerProps":
                    createdZombie = new ElementalZombie(health, speed, eatDPS, true);
                    break;
                case "ZombieProspectorProps":
                    createdZombie = new ElementalZombie(health, speed, eatDPS, false); // false = Prospector
                    break;
                case "ZombieIceAgeHunterProps":
                    createdZombie = new RangedZombie(health, speed, eatDPS, "SNOWBALL");
                    break;
                case "ZombieBeachOctopusProps":
                    createdZombie = new RangedZombie(health, speed, eatDPS, "OCTOPUS");
                    break;
                case "ZombieTombRaiserProps":
                    createdZombie = new RangedZombie(health, speed, eatDPS, "BONE");
                    break;
                case "ZombieDarkJugglerProps":
                    createdZombie = new DeflectorZombie(health, speed, eatDPS, true);
                    break;
                case "ZombieLostCityJaneProps":
                    createdZombie = new DeflectorZombie(health, speed, eatDPS, false);
                    break;
                case "ZombieDarkWizardProps":
                    createdZombie = new WizardZombie(health, speed, eatDPS);
                    break;
                case "ZombieDarkKingProps":
                    createdZombie = new SpawnerZombie(health, speed, eatDPS, false);
                    break;
                case "ZombieBeachFishermanProps":
                    createdZombie = new FishermanZombie(health, eatDPS);
                    break;
                case "ZombieIceAgeDodoProps":
                    createdZombie = new DodoRiderZombie(health, speed, eatDPS);
                    break;
                case "ZombieModernAllStarProps":
                    createdZombie = new PhasingZombie(health, speed, eatDPS, 0, false);
                    break;
                case "ZombieNewspaperProps":
                    createdZombie = new PhasingZombie(health, speed, eatDPS, 800, true);
                    break;
                case "ZombiePianoProps":
                    createdZombie = new PusherZombie(health, speed, eatDPS, "PIANO", 1100);
                    break;
                case "ZombieArcadeProps":
                    createdZombie = new PusherZombie(health, speed, eatDPS, "ARCADE", 1100);
                    break;
                case "ZombieIceAgeTroglobiteProps":
                    createdZombie = new PusherZombie(health, speed, eatDPS, "ICEBLOCK", 600);
                    break;
                case "ZombieBeachSnorkelProps":
                    createdZombie = new SnorkelZombie(health, speed, eatDPS);
                    break;
                case "ZombieImpProps":
                    createdZombie = new ImpZombie(health, speed, eatDPS, false);
                    break;
                case "ZombieDarkImpDragonProps":
                    createdZombie = new ImpZombie(health, speed, eatDPS, true);
                    break;
                case "ZombieBarrelRollerProps": // توی جیسون نبود
                    createdZombie = new BarrelRollerZombie(health, speed, eatDPS, 600);
                    break;
                default:
                    createdZombie = buildBasicZombie(health, eatDPS, speed, data);
                    break;
            }
        }
        if (createdZombie != null) {
            createdZombie.setSpecificName(alias);
            if (Math.random() < 0.05) {
                createdZombie.setGlowing(true);
            }
        }

        return createdZombie;
    }

    private Zombie buildBasicZombie(int health, int eatDPS, double speed, ZombieData data) {
        List<String> armorRefs = data.getZombieArmorProps();
        if (armorRefs != null && !armorRefs.isEmpty()) {
            int totalArmorHealth = 0;
            boolean magnetic = false;
            List<String> currentArmorTypes = new ArrayList<>();
            for (String ref : armorRefs) {
                String alias = extractAlias(ref);
                ArmorProperties armor = ZombieRegistry.getArmorProperties(alias);
                if (armor != null) {
                    ArmorData ad = armor.getObjdata();
                    totalArmorHealth += ad.getBaseHealth();
                    currentArmorTypes.add(ad.getArmorType());
                    if (ad.getArmorFlags().contains("metallic")) {
                        magnetic = true;
                    }
                } else {
                    logger.warning("Armor properties not found for alias: " + alias + " (used in " + data.getClass().getName() + ")");
                }
            }
            ArmoredZombie zombie = new ArmoredZombie(health, speed, eatDPS, totalArmorHealth, magnetic);
            zombie.getArmorTypes().addAll(currentArmorTypes);
//            logger.info("Created armored zombie with total armor health: " + totalArmorHealth);
            return zombie;
        }
        // زامبی معمولی بدون زره
        return new Zombie(Zombies.ZOMBIE, health, speed, eatDPS) {};
    }

    private String extractAlias(String ref) {
        if (ref.startsWith("RTID(") && ref.endsWith(")")) {
            String inner = ref.substring(5, ref.length() - 1);
            int atIndex = inner.indexOf('@');
            if (atIndex != -1) {
                return inner.substring(0, atIndex);
            }
            return inner;
        }
        return ref;
    }

    private Zombie createZombotanyZombie(String alias) {
        return switch (alias) {
            case "ZombiePeashooter" -> new PeashooterZombie(Zombies.PEASHOOTER_ZOMBIE, 200, 1.0, 20);
            case "ZombieWallnut" -> new WallnutZombie(Zombies.WALLNUT_ZOMBIE, 4000, 0.3, 100);
            case "ZombieJalapeno" -> new JalapenoZombie(Zombies.JALAPENO_ZOMBIE, 200, 1.0, 100);
            case "ZombieSquash" -> new SquashZombie(Zombies.SQUASH_ZOMBIE, 200, 3.0, 500);
            default -> null;
        };
    }
}