package org.solocode.betterConfig;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Map;

public interface Config {
    void createConfig(ConfigType type, String name);

    void createConfig(ConfigType type);

    YamlConfiguration loadConfig(String fileName) throws IllegalAccessException, IOException, InvalidConfigurationException;

    void saveConfig(String fileName) throws IllegalAccessException, IOException;

    void addDefault(String fileName, String path, Object value);

    void addDefault(String fileName, Map<String, Object> defaults);
}
