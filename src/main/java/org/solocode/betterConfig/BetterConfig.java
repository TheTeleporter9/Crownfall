package org.solocode.betterConfig;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 A lightweight configuration manager for Bukkit/Paper plugins.
 * <p>
 * BetterConfig allows developers to create, load, modify, reload, and save
 * YAML configuration files without dealing directly with file handling.
 * Configurations are cached in memory for fast access, while save operations
 * are performed asynchronously to prevent blocking the server's main thread.
 * </p>
 *
 */

public class BetterConfig implements Config {

    private final Plugin plugin;

    private final Map<String, File> files = new HashMap<>();
    private final Map<String, YamlConfiguration> configs = new HashMap<>();

    public BetterConfig(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createConfig(ConfigType type, String name) {
        if (type == ConfigType.Default) {
            plugin.saveDefaultConfig();
            return;
        }

        File file = new File(plugin.getDataFolder(), name + ".yml");

        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            files.put(name, file);
            configs.put(name, YamlConfiguration.loadConfiguration(file));

        } catch (IOException e) {
            throw new RuntimeException("Failed to create config '" + name + "'", e);
        }
    }

    public void createConfig(ConfigType type) {
        plugin.saveDefaultConfig();
    }

    @Override
    public YamlConfiguration loadConfig(String fileName) {
        YamlConfiguration config = configs.get(fileName);

        if (config == null) {
            throw new IllegalArgumentException(
                    "Config '" + fileName + "' does not exist."
            );
        }

        return config;
    }

    public YamlConfiguration getConfig(String fileName) {
        return loadConfig(fileName);
    }

    @Override
    public void saveConfig(String fileName) {
        File file = files.get(fileName);
        YamlConfiguration config = configs.get(fileName);

        if (file == null || config == null) {
            throw new IllegalArgumentException(
                    "Config '" + fileName + "' does not exist."
            );
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().severe(
                        "Failed to save config '" + fileName + "'"
                );
                e.printStackTrace();
            }
        });
    }

    public void reloadConfig(String fileName) {
        File file = files.get(fileName);

        if (file == null) {
            throw new IllegalArgumentException(
                    "Config '" + fileName + "' does not exist."
            );
        }

        configs.put(fileName, YamlConfiguration.loadConfiguration(file));
    }

    public boolean exists(String fileName) {
        return configs.containsKey(fileName);
    }

    /**
     * Sets a value only if the path does not already exist.
     *
     * @param fileName The configuration name without the .yml extension.
     * @param path The path within the configuration.
     * @param value The default value.
     *
     * @throws IllegalArgumentException If the configuration does not exist.
     */
    public void addDefault(String fileName, String path, Object value) {
        YamlConfiguration config = getConfig(fileName);

        if (!config.contains(path)) {
            config.set(path, value);
        }
    }

    /**
     * Sets a value only if the path does not already exist.
     *
     * @param fileName The configuration name without the .yml extension.
     * @param defaults A map with a string for path and a value after wards
     *
     * @throws IllegalArgumentException If the configuration does not exist.
     */
    public void addDefault(String fileName, Map<String, Object> defaults) {
        YamlConfiguration config = getConfig(fileName);

        defaults.forEach((path, value) -> {
            if (!config.contains(path)) {
                config.set(path, value);
            }
        });
    }

    /**
     * Creates a default configuration section only if it does not already exist.
     *
     * Useful for things like biomes, kits, items, etc.
     *
     * @param fileName config file name
     * @param sectionPath path like "biomes.example"
     * @param builder lambda to define default values
     */
    public void addSectionDefault(String fileName, String sectionPath, java.util.function.Consumer<YamlConfiguration> builder) {
        YamlConfiguration config = getConfig(fileName);

        if (config.isConfigurationSection(sectionPath)) {
            return;
        }

        YamlConfiguration temp = new YamlConfiguration();
        builder.accept(temp);

        config.createSection(sectionPath, temp.getValues(true));
    }

}