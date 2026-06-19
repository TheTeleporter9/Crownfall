package org.solocode.crownfall.World;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.solocode.betterConfig.BetterConfig;
import org.solocode.betterConfig.ConfigType;
import org.solocode.crownfall.World.map.GameMap;

public class Mapmanager {

    private final BetterConfig config;
    private final Plugin plugin;

    public Mapmanager(Plugin plugin) {
        this.plugin = plugin;
        this.config = new BetterConfig(plugin);
    }

    public void createConfig() {
        config.createConfig(ConfigType.Custom, "map-config");

        config.addSectionDefault("map-config", "biomes.default", biome -> {

            biome.set("display-name", "Default Biome");

            // Color settings
            biome.set("color.sky", "#78A7FF");
            biome.set("color.fog", "#CFE8FF");
            biome.set("color.grass", "#55FF55");
            biome.set("color.water", "#3F76E4");

            // Terrain settings
            biome.set("terrain.height", 64);
            biome.set("terrain.scale", 1.0);

            // Environment effects
            biome.set("effects.temperature", 0.8);
            biome.set("effects.downfall", 0.4);
        });

        config.saveConfig("map-config");
    }

    private void loadMaps(YamlConfiguration cfg) {

        ConfigurationSection sec = cfg.getConfigurationSection("maps");
        if (sec == null) return;

        for (String key : sec.getKeys(false)) {

            ConfigurationSection m = sec.getConfigurationSection(key);
            if (m == null) continue;

            GameMap map = new GameMap(
                    key,
                    m.getString("display-name"),
                    m.getString("biome"),
                    m.getString("world-path") //
            );

            maps.put(key.toLowerCase(), map);
        }
    }

}