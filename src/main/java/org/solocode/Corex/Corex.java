package org.solocode.Corex;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.solocode.Corex.config.Config;
import org.solocode.Corex.config.ConfigCore;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Corex {

    private final ConfigCore configCore;
    private final List<Object> initializedConfigs = new ArrayList<>();

    public Corex(Plugin plugin) {
        this.configCore = new ConfigCore(plugin);

        autoScanAndInit(plugin);
    }

    private void autoScanAndInit(Plugin plugin) {
        try {
            File jarFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            URL jarUrl = jarFile.toURI().toURL();

            String mainPackage = plugin.getClass().getPackageName();

            // Read through the contents of the compiled Jar
            try (ZipInputStream zip = new ZipInputStream(jarUrl.openStream())) {
                while (true) {
                    ZipEntry entry = zip.getNextEntry();
                    if (entry == null) break;

                    String name = entry.getName();
                    if (name.endsWith(".class")) {
                        // Convert internal file path back to a Java class path
                        String className = name.replace('/', '.').substring(0, name.length() - 6);

                        // Only scan classes belonging to the developer's plugin package
                        if (className.startsWith(mainPackage)) {
                            Class<?> clazz = Class.forName(className);

                            for (Field field : clazz.getDeclaredFields()) {
                                if (field.isAnnotationPresent(Config.class)) {

                                    Object initializedInstance = configCore.init(clazz);
                                    initializedConfigs.add(initializedInstance);
                                    Bukkit.getConsoleSender().sendMessage(
                                            Component.text()
                                                    .append(Component.text("[Corex] ").color(NamedTextColor.GREEN))
                                                    .append(Component.text("Automatically initialized config class: ").color(NamedTextColor.GRAY))
                                                    .append(Component.text(clazz.getSimpleName()).color(NamedTextColor.AQUA))
                                                    .build() // Make sure to call .build() to complete the component builder
                                    );
                                    break; // Move to the next class
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(
                    Component.text()
                            .append(Component.text("[Corex] ").color(NamedTextColor.GREEN))
                            .append(Component.text("Failed during configuration scanning!").color(NamedTextColor.RED))
                            .build()
            );
            e.printStackTrace();
        }
    }

    /**
     * Optional utility if they need to retrieve the generated configuration instance later
     */
    public <T> T getConfigInstance(Class<T> clazz) {
        for (Object obj : initializedConfigs) {
            if (clazz.isInstance(obj)) return clazz.cast(obj);
        }
        return null;
    }
}