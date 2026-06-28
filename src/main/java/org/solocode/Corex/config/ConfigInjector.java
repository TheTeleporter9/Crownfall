package org.solocode.Corex.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ConfigInjector {

    public static void inject(Object source, ConfigCore configCore) {
        // If 'source' is already a Class object, use it directly. Otherwise, grab its class.
        Class<?> clazz = (source instanceof Class<?>) ? (Class<?>) source : source.getClass();
        // If 'source' is a Class object, we have no instance (it's static), so targetInstance is null.
        Object targetInstance = (source instanceof Class<?>) ? null : source;

        Set<String> filesToSave = new HashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Config.class)) {
                Config annotation = field.getAnnotation(Config.class);
                String fullPath = annotation.value();

                int firstDot = fullPath.indexOf('.');
                if (firstDot == -1) continue;

                String fileName = fullPath.substring(0, firstDot);
                String configPath = fullPath.substring(firstDot + 1);

                if (!configCore.exists(fileName)) {
                    configCore.createConfig(ConfigType.Custom, fileName);
                }

                YamlConfiguration yaml = configCore.getConfig(fileName);
                field.setAccessible(true);

                try {
                    // AUTOMATIC COMPLEX TYPE HANDLING (Maps of Objects)
                    if (Map.class.isAssignableFrom(field.getType())) {
                        ConfigurationSection section = yaml.getConfigurationSection(configPath);
                        if (section != null) {
                            ParameterizedType mapType = (ParameterizedType) field.getGenericType();
                            Class<?> valueClass = (Class<?>) mapType.getActualTypeArguments()[1];

                            Map<String, Object> automaticallyFilledMap = new HashMap<>();

                            for (String key : section.getKeys(false)) {
                                ConfigurationSection itemSection = section.getConfigurationSection(key);
                                if (itemSection == null) continue;

                                Object item = autoMapSection(valueClass, itemSection);
                                if (item != null) {
                                    automaticallyFilledMap.put(key.toLowerCase(), item);
                                }
                            }
                            // Safe injection for static or instance fields
                            field.set(targetInstance, automaticallyFilledMap);
                        }
                    }
                    // STANDARD SIMPLE TYPE HANDLING (Primitives, Strings, Booleans)
                    else {
                        if (yaml.contains(configPath)) {
                            Object value = yaml.get(configPath);
                            if (value != null) {
                                // Safe injection for static or instance fields
                                field.set(targetInstance, value);
                            }
                        } else {
                            Object javaDefaultValue = field.get(targetInstance);
                            if (javaDefaultValue != null) {
                                configCore.addDefault(fileName, configPath, javaDefaultValue);
                                filesToSave.add(fileName);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[Corex] Failed to process automated field " + field.getName() + ": " + e.getMessage());
                }
            }
        }

        for (String fileName : filesToSave) {
            configCore.saveConfig(fileName);
        }
    }

    private static Object autoMapSection(Class<?> clazz, ConfigurationSection section) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) return null;
        Constructor<?> constructor = constructors[0];

        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            String paramName = param.getName();
            String configKey = paramName.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();

            if (section.contains(configKey)) {
                args[i] = section.get(configKey);
            } else if (configKey.equals("id") || configKey.equals("key")) {
                args[i] = section.getName();
            } else {
                if (param.getType() == boolean.class) args[i] = false;
                else if (param.getType() == int.class) args[i] = 0;
                else args[i] = "#FFFFFF";
            }
        }
        return constructor.newInstance(args);
    }
}