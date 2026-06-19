package org.solocode.crownfall.World;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.solocode.betterConfig.BetterConfig;
import org.solocode.betterConfig.ConfigType;
import org.solocode.crownfall.World.biome.CustomBiome;
import org.solocode.crownfall.World.map.GameMap;

import java.util.HashMap;
import java.util.Map;

public class Mapmanager {

    private final Plugin plugin;
    private final BetterConfig config;

    private final Map<String, GameMap> maps = new HashMap<>();
    private final Map<String, CustomBiome> biomes = new HashMap<>();

    public Mapmanager(Plugin plugin) {
        this.plugin = plugin;
        this.config = new BetterConfig(plugin);

        createConfig();
    }

    /**
     * Creates config file if it doesn't exist
     */
    private void createConfig() {
        config.createConfig(ConfigType.Custom, "map-config");
    }

    /**
     * Loads ALL maps + biomes into memory
     */
    public void load() {
        maps.clear();
        biomes.clear();

        YamlConfiguration cfg = config.getConfig("map-config");

        loadBiomes(cfg);
        loadMaps(cfg);
    }


    private void loadBiomes(YamlConfiguration cfg) {

        ConfigurationSection section = cfg.getConfigurationSection("biomes");
        if (section == null) return;

        for (String key : section.getKeys(false)) {

            ConfigurationSection b = section.getConfigurationSection(key);
            if (b == null) continue;

            CustomBiome biome = new CustomBiome(
                    key,
                    b.getString("resource-key"),
                    b.getString("fog-color"),
                    b.getString("foliage-color"),
                    b.getString("sky-color"),
                    b.getString("water-color"),
                    b.getString("water-fog-color")
            );

            biomes.put(key.toLowerCase(), biome);
        }
    }


    private void loadMaps(YamlConfiguration cfg) {

        ConfigurationSection section = cfg.getConfigurationSection("maps");
        if (section == null) return;

        for (String key : section.getKeys(false)) {

            ConfigurationSection m = section.getConfigurationSection(key);
            if (m == null) continue;

            GameMap map = new GameMap(
                    key,
                    m.getString("display-name"),
                    m.getString("biome"),
                    m.getString("world-path")
            );

            maps.put(key.toLowerCase(), map);
        }
    }



    public GameMap getMap(String id) {
        return maps.get(id.toLowerCase());
    }

    public CustomBiome getBiome(String id) {
        return biomes.get(id.toLowerCase());
    }

    public CustomBiome getBiomeFromMap(String mapId) {
        GameMap map = getMap(mapId);
        if (map == null) return null;

        return getBiome(map.getBiomeId());
    }

    public Map<String, GameMap> getAllMaps() {
        return maps;
    }

    public Map<String, CustomBiome> getAllBiomes() {
        return biomes;
    }
}