package models.zombie;

import models.enums.Zombies;
import models.zombie.data.ArmorData;
import models.zombie.data.ArmorProperties;
import models.zombie.data.ZombieData;
import models.zombie.data.ZombieProperties;
import models.zombie.zombiesType.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombieFactory {
    private static final Map<Zombies, String> enumToAlias = new HashMap<>();
    static {
        enumToAlias.put(Zombies.ZOMBIE, "ZombieDefault");
        enumToAlias.put(Zombies.ARMORED, "ZombieArmor1"); // نمونه
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
    }

    // متد اصلی: ساخت زامبی بر اساس alias
    public Zombie createZombie(String alias) {
        ZombieProperties props = ZombieRegistry.getZombieProperties(alias);
        if (props == null) {
            throw new IllegalArgumentException("Unknown zombie alias: " + alias);
        }
        String objclass = props.getObjclass();
        ZombieData data = props.getObjdata();

        int health = data.getHitpoints();
        int eatDPS = data.getEatDPS();
        double speed = data.getSpeed();

        switch (objclass) {
            case "ZombiePropertySheet":
                return buildBasicZombie(health, eatDPS, speed, data);
            case "ZombieGargantuarProps":
                return new SpawnerZombie(health, (int)speed, eatDPS, true);
            case "ZombieRaProps":
                return new SunStealerZombie(health, (int)speed, eatDPS, true);
            case "ZombieExplorerProps":
                return new ElementalZombie(health, (int)speed, eatDPS, true);
            case "ZombieIceAgeHunterProps":
                return new RangedZombie(health, (int)speed, eatDPS, "SNOWBALL");
            case "ZombieBeachOctopusProps":
                return new RangedZombie(health, (int)speed, eatDPS, "OCTOPUS");
            case "ZombieTombRaiserProps":
                return new RangedZombie(health, (int)speed, eatDPS, "BONE");
            case "ZombieDarkJugglerProps":
                return new DeflectorZombie(health, (int)speed, eatDPS, true);
            case "ZombieLostCityJaneProps": // چتردار
                return new DeflectorZombie(health, (int)speed, eatDPS, false);
            case "ZombieDarkWizardProps":
                return new WizardZombie(health, (int)speed, eatDPS);
            case "ZombieDarkKingProps":
                return new SpawnerZombie(health, (int)speed, eatDPS, false);
            case "ZombieBeachFishermanProps":
                return new FishermanZombie(health, eatDPS);
            case "ZombieIceAgeDodoProps":
                return new DodoRiderZombie(health, (int)speed, eatDPS);
            case "ZombieModernAllStarProps":
                return new PhasingZombie(health, (int)speed, eatDPS, 0, false);
            case "ZombieNewspaperProps":
                return new PhasingZombie(health, (int)speed, eatDPS, 800, true);
            case "ZombiePianoProps":
                return new PusherZombie(health, (int)speed, eatDPS, "PIANO", 0);
            case "ZombieArcadeProps":
                return new PusherZombie(health, (int)speed, eatDPS, "ARCADE", 600);
            case "ZombieIceAgeTroglobiteProps":
                return new PusherZombie(health, (int)speed, eatDPS, "ICEBLOCK", 600);
            case "ZombieBeachSnorkelProps":
                return new SnorkelZombie(health, (int)speed, eatDPS);
            case "ZombieImpProps":
                return new ImpZombie(health, (int)speed, eatDPS, false);
            case "ZombieDarkImpDragonProps":
                return new ImpZombie(health, (int)speed, eatDPS, true);
            default:
                return buildBasicZombie(health, eatDPS, speed, data);
        }
    }

    private Zombie buildBasicZombie(int health, int eatDPS, double speed, ZombieData data) {
        List<String> armorRefs = data.getZombieArmorProps();
        if (armorRefs != null && !armorRefs.isEmpty()) {
            int totalArmorHealth = 0;
            boolean magnetic = false;
            for (String ref : armorRefs) {
                String alias = extractAlias(ref);
                ArmorProperties armor = ZombieRegistry.getArmorProperties(alias);
                if (armor != null) {
                    ArmorData ad = armor.getObjdata();
                    totalArmorHealth += ad.getBaseHealth();
                    if (ad.getArmorFlags().contains("metallic")) {
                        magnetic = true;
                    }
                }
            }
            return new ArmoredZombie(health, (int)speed, eatDPS, totalArmorHealth, magnetic);
        }
        // زامبی معمولی (بدون زره)
        return new Zombie(Zombies.ZOMBIE, health, (int)speed, eatDPS) {};
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

    // متد برای enum
    public Zombie createZombie(Zombies type) {
        String alias = enumToAlias.get(type);
        if (alias == null) return null;
        return createZombie(alias);
    }
}