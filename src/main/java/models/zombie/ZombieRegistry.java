package models.zombie;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.zombie.data.ArmorProperties;
import models.zombie.data.ZombieProperties;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ZombieRegistry {
    private static final Logger LOGGER = Logger.getLogger(ZombieRegistry.class.getName());
    private static final Map<String, ZombieProperties> ZOMBIE_MAP = new HashMap<>();
    private static final Map<String, ArmorProperties> ARMOR_MAP = new HashMap<>();

    static {
        loadZombies();
        loadArmors();
    }

    private static void loadZombies() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try (InputStream is = ZombieRegistry.class.getResourceAsStream("/zombies.json")) {
            if (is == null) {
                LOGGER.severe("zombies.json not found in resources!");
                return;
            }
            List<ZombieProperties> list = mapper.readValue(is,
                    mapper.getTypeFactory().constructCollectionType(List.class, ZombieProperties.class));
            for (ZombieProperties zp : list) {
                for (String alias : zp.getAliases()) {
                    ZOMBIE_MAP.put(alias, zp);
                }
            }
//            logger.info("Loaded " + zombieMap.size() + " zombie entries.");
        } catch (Exception e) {
            LOGGER.severe("Failed to load zombies.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadArmors() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try (InputStream is = ZombieRegistry.class.getResourceAsStream("/ArmorTypeData.json")) {
            if (is == null) {
                LOGGER.severe("ArmorTypeData.json not found in resources!");
                return;
            }
            List<ArmorProperties> list = mapper.readValue(is,
                    mapper.getTypeFactory().constructCollectionType(List.class, ArmorProperties.class));
            for (ArmorProperties ap : list) {
                for (String alias : ap.getAliases()) {
                    ARMOR_MAP.put(alias, ap);
                }
            }
//            logger.info("Loaded " + armorMap.size() + " armor entries.");
        } catch (Exception e) {
            LOGGER.severe("Failed to load ArmorTypeData.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ZombieProperties getZombieProperties(String alias) {
        return ZOMBIE_MAP.get(alias);
    }

    public static ArmorProperties getArmorProperties(String alias) {
        return ARMOR_MAP.get(alias);
    }
}