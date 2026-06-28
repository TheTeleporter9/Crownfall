package org.solocode.Corex.config;

import org.bukkit.configuration.ConfigurationSection;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class ConfigMapper {

    /**
     * Instantiates an immutable object using its constructor by mapping parameters
     * directly to keys in a Bukkit ConfigurationSection.
     */
    public static <T> T mapSectionToObject(Class<T> clazz, ConfigurationSection section) {
        try {
            // Find the constructor with the most parameters (the primary data constructor)
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 0) return null;

            Constructor<?> constructor = constructors[0];
            for (Constructor<?> c : constructors) {
                if (c.getParameterCount() > constructor.getParameterCount()) {
                    constructor = c;
                }
            }

            Parameter[] parameters = constructor.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                String paramName = param.getName(); // Returns e.g. "resourceKey" or "arg1"

                // Convert camelCase field/parameter names to standard kebab-case config keys
                // e.g., "resourceKey" -> "resource-key", "waterFogColor" -> "water-fog-color"
                String configKey = paramName.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();

                // Fetch value or fall back to sensible primitive defaults if missing
                if (section.contains(configKey)) {
                    args[i] = section.get(configKey);
                } else if (configKey.equals("id") || configKey.equals("key")) {
                    // Special case: map the section's parent key name to 'id' or 'key' parameter
                    args[i] = section.getName();
                } else {
                    // Primitive default safety fallbacks
                    if (param.getType() == boolean.class) args[i] = false;
                    else if (param.getType() == int.class) args[i] = 0;
                    else if (param.getType() == double.class) args[i] = 0.0;
                    else args[i] = "#FFFFFF"; // Standard color string default
                }
            }

            // Cast and return the newly instantiated immutable object instance!
            return clazz.cast(constructor.newInstance(args));

        } catch (Exception e) {
            throw new RuntimeException("Corex failed to map configuration section to class: " + clazz.getName(), e);
        }
    }
}