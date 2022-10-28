package net.cvm.fyesbycvm;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;

import static net.cvm.fyesbycvm.FYES.*;

public class FYESInitializer {
    private static final Path configPath = FabricLoader.getInstance().getConfigDir();
    private static final Gson jsonParser = new GsonBuilder().setPrettyPrinting().create();
    public static ArrayList<String> requiredBannedMobs = new ArrayList<>();
    public static File configFile = new File(configPath.toFile(), "fyes_config.json");

    /* ====== Singleton ====== */
    private static final FYESInitializer INSTANCE = new FYESInitializer();
    private FYESInitializer() {}
    public static FYESInitializer getInstance() {
        return INSTANCE;
    }
    /* ======================= */

    private static void initRequiredBannedList() {
        requiredBannedMobs.add("minecraft:spider");
        requiredBannedMobs.add("minecraft:cave_spider");
        requiredBannedMobs.add("minecraft:squid");
        requiredBannedMobs.add("minecraft:glow_squid");
    }
    private static boolean isSubset(ArrayList<String> list1, ArrayList<String> list2) {
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);

        return set1.containsAll(set2);
    }
    public void checkBannedMobsList() {
        LOGGER.info(MOD_PREFIX + " Loaded " + bannedMobs.size() + " banned mobs");
    }
    public void initConfigFile() {
        initRequiredBannedList();
        if (!configFile.exists()) {
            bannedMobs.addAll(requiredBannedMobs);

            try (FileWriter configWriter = new FileWriter(configFile)) {
                String jsonCfg = jsonParser.toJson(bannedMobs);
                configWriter.write(jsonCfg);
                LOGGER.info(MOD_PREFIX + " Config file has been created");
            }
            catch (Exception e) {
                LOGGER.error(MOD_PREFIX + " Failed to create config file");
            }
        }
        else {
            try (FileReader configReader = new FileReader(configFile)) {
                Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                bannedMobs = jsonParser.fromJson(configReader, listType);

                if (!isSubset(bannedMobs, requiredBannedMobs)) {
                    Set<String> set = new LinkedHashSet<>(requiredBannedMobs);
                    set.addAll(bannedMobs);
                    bannedMobs = new ArrayList<>(set);
                    try (FileWriter configWriter = new FileWriter(configFile)) {
                        String jsonCfg = jsonParser.toJson(bannedMobs);
                        configWriter.write(jsonCfg);
                    }
                    catch (Exception e) {
                        LOGGER.error(MOD_PREFIX + " Failed to modify config file");
                    }
                    LOGGER.info(MOD_PREFIX + " Some of the required for restriction mobs has not found. Adding required mods. ");
                }
                LOGGER.info(MOD_PREFIX + " Config file has been loaded");
            }
            catch (Exception e) {
                LOGGER.error(MOD_PREFIX + " Failed to read config file");
            }
        }
    }
}