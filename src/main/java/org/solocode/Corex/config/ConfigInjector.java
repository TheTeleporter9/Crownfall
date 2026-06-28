package org.solocode.Corex.config;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.configuration.file.YamlConfiguration;
import java.lang.reflect.Field;
import org.bukkit.configuration.file.YamlConfiguration;
import java.lang.reflect.Field;

public class ConfigInjector {

    public static void inject(Object targetInstance, ConfigCore configCore) {
        Class<?> clazz = targetInstance.getClass();
        java.util.Set<String> filesToSave = new java.util.HashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Config.class)) {
                Config annotation = field.getAnnotation(Config.class);
                String rawPath = annotation.value();

                String fileName;
                String configPath;

                // --- PATH PARSING LOGIC ---
                if (rawPath.isEmpty()) {
                    // Scenario 3: Nothing provided -> Default file, Variable name
                    fileName = "config";
                    configPath = field.getName();
                } else {
                    int firstDot = rawPath.indexOf('.');
                    if (firstDot == -1) {
                        // Scenario 2: Path provided but NO dot -> Default file, Specified name
                        fileName = "config";
                        configPath = rawPath;
                    } else {
                        // Scenario 1: standard "filename.path"
                        fileName = rawPath.substring(0, firstDot);
                        configPath = rawPath.substring(firstDot + 1);
                    }
                }

                // Make sure the parsed file is registered in the backend
                if (!configCore.exists(fileName)) {
                    configCore.createConfig(ConfigType.Custom, fileName);
                }

                YamlConfiguration yaml = configCore.getConfig(fileName);
                field.setAccessible(true);

                try {
                    if (yaml.contains(configPath)) {
                        Object value = yaml.get(configPath);
                        if (value != null) {
                            field.set(targetInstance, value);
                        }
                    } else {
                        Object javaDefaultValue = field.get(targetInstance);
                        if (javaDefaultValue != null) {
                            configCore.addDefault(fileName, configPath, javaDefaultValue);
                            filesToSave.add(fileName);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[Corex] Failed to process field " + field.getName() + ": " + e.getMessage());
                }
            }
        }

        // Asynchronously save any configuration files that had new defaults injected
        for (String fileName : filesToSave) {
            configCore.saveConfig(fileName);
        }
    }

    /**
     * Specialized injection helper that maps a localized Bukkit ConfigurationSection
     * directly into an object instance (ignoring file name prefixes).
     */
    public static void injectFromSection(Object targetInstance, org.bukkit.configuration.ConfigurationSection section, ConfigCore configCore) {
        Class<?> clazz = targetInstance.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Config.class)) {
                Config annotation = field.getAnnotation(Config.class);
                String configPath = annotation.value();

                // If they provided a full path with a dot, strip it down to the last element for sections
                if (configPath.contains(".")) {
                    configPath = configPath.substring(configPath.lastIndexOf('.') + 1);
                }

                field.setAccessible(true);

                try {
                    if (section.contains(configPath)) {
                        Object value = section.get(configPath);
                        if (value != null) {
                            field.set(targetInstance, value);
                        }
                    } else {
                        // Save default fallback values back into the live dynamic section structure
                        Object javaDefaultValue = field.get(targetInstance);
                        if (javaDefaultValue != null) {
                            section.set(configPath, javaDefaultValue);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[Corex] Failed to loop section field " + field.getName() + ": " + e.getMessage());
                }
            }
        }
    }
}
