package org.solocode.betterConfig;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public interface Config {
    void createConfig(ConfigType type, String name);

    void createConfig(ConfigType type);

    YamlConfiguration loadConfig(String fileName) throws IllegalAccessException, IOException, InvalidConfigurationException;

    void saveConfig(String fileName) throws IllegalAccessException, IOException;
}
