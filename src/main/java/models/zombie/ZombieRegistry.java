package models.zombie;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.zombie.data.ArmorProperties;
import models.zombie.data.ZombieProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class ZombieRegistry {
        private static final Map<String, ZombieProperties> zombieMap = new HashMap<>();
        private static final Map<String, ArmorProperties> armorMap = new HashMap<>();

        static {
            loadZombies();
            loadArmors();
        }

        private static void loadZombies() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
            try (InputStream is = ZombieRegistry.class.getResourceAsStream("/zombies.json")) {
                List<ZombieProperties> list = mapper.readValue(is,
                        mapper.getTypeFactory().constructCollectionType(List.class, ZombieProperties.class));
                for (ZombieProperties zp : list) {
                    for (String alias : zp.getAliases()) {
                        zombieMap.put(alias, zp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static void loadArmors() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
            try (InputStream is = ZombieRegistry.class.getResourceAsStream("/ArmorTypeData.json")) {
                List<ArmorProperties> list = mapper.readValue(is,
                        mapper.getTypeFactory().constructCollectionType(List.class, ArmorProperties.class));
                for (ArmorProperties ap : list) {
                    for (String alias : ap.getAliases()) {
                        armorMap.put(alias, ap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static ZombieProperties getZombieProperties(String alias) {
            return zombieMap.get(alias);
        }

        public static ArmorProperties getArmorProperties(String alias) {
            return armorMap.get(alias);
        }
    }


