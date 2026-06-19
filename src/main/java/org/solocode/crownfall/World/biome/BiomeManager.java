package org.solocode.crownfall.World.biome;

import org.bukkit.configuration.file.YamlConfiguration;
import org.solocode.betterConfig.BetterConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BiomeManager {

    private final BetterConfig config;
    private final Map<String, CustomBiome> biomes = new HashMap<>();

    public BiomeManager(BetterConfig config) {
        this.config = config;
    }

    /**
     * Loads all biomes from map-config.yml
     */
    public void loadBiomes() {
        biomes.clear();

        YamlConfiguration cfg = config.getConfig("map-config");

        if (!cfg.isConfigurationSection("biomes")) return;

        var section = cfg.getConfigurationSection("biomes");

        for (String key : section.getKeys(false)) {

            var biomeSection = section.getConfigurationSection(key);

            if (biomeSection == null) continue;

            CustomBiome biome = new CustomBiome(key, biomeSection);
            biomes.put(key.toLowerCase(), biome);
        }
    }

    /**
     * Get a biome by id
     */
    public CustomBiome get(String id) {
        return biomes.get(id.toLowerCase());
    }

    /**
     * Get all biomes
     */
    public Collection<CustomBiome> getAll() {
        return biomes.values();
    }

    /**
     * Check if biome exists
     */
    public boolean exists(String id) {
        return biomes.containsKey(id.toLowerCase());
    }
}