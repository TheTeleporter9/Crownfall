package org.solocode.Corex;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.solocode.Corex.config.Config;
import org.solocode.Corex.config.ConfigCore;
import org.solocode.Corex.config.ConfigInjector;

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
    private final List<Class<?>> detectedConfigClasses = new ArrayList<>();

    // Changed to JavaPlugin so we can access Paper's Lifecycle command manager
    public Corex(JavaPlugin plugin) {
        this.configCore = new ConfigCore(plugin);
        autoScanAndInit(plugin);
        autoRegisterCommand(plugin);
    }

    private void autoScanAndInit(JavaPlugin plugin) {
        try {
            File jarFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            URL jarUrl = jarFile.toURI().toURL();

            String mainPackage = plugin.getClass().getPackageName();

            try (ZipInputStream zip = new ZipInputStream(jarUrl.openStream())) {
                while (true) {
                    ZipEntry entry = zip.getNextEntry();
                    if (entry == null) break;

                    String name = entry.getName();
                    if (name.endsWith(".class")) {
                        String className = name.replace('/', '.').substring(0, name.length() - 6);

                        if (className.startsWith(mainPackage)) {
                            if (className.contains("Config") || className.contains("Settings")) {
                                Class<?> clazz = Class.forName(className);

                                for (Field field : clazz.getDeclaredFields()) {
                                    if (field.isAnnotationPresent(Config.class)) {

                                        Object initializedInstance = configCore.init(clazz);

                                        initializedConfigs.add(initializedInstance);
                                        detectedConfigClasses.add(clazz);

                                        Bukkit.getConsoleSender().sendMessage(
                                                Component.text()
                                                        .append(Component.text("[Corex] ").color(NamedTextColor.GREEN))
                                                        .append(Component.text("Automatically initialized config class: ").color(NamedTextColor.GRAY))
                                                        .append(Component.text(clazz.getSimpleName()).color(NamedTextColor.AQUA))
                                                        .build()
                                        );
                                        break;
                                    }
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

    private void autoRegisterCommand(JavaPlugin plugin) {
        String commandName = plugin.getName().toLowerCase() + "reload";

        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(commandName, "Reloads configuration files for " + plugin.getName(), new CorexReloadCommand(this, plugin.getName()));
        });
    }

    public <T> T getConfigInstance(Class<T> clazz) {
        for (Object obj : initializedConfigs) {
            if (clazz.isInstance(obj)) return clazz.cast(obj);
        }
        return null;
    }

    public void reloadAll() {
        for (Class<?> clazz : detectedConfigClasses) {
            ConfigInjector.inject(clazz, configCore);
        }
    }

    public ConfigCore getConfigCore() {
        return configCore;
    }

    private static class CorexReloadCommand implements BasicCommand {
        private final Corex corex;
        private final String pluginName;

        public CorexReloadCommand(Corex corex, String pluginName) {
            this.corex = corex;
            this.pluginName = pluginName;
        }

        @Override
        public void execute(CommandSourceStack source, String[] args) {
            // We no longer need to call getPlugin(), we just use the variable!
            String permission = pluginName.toLowerCase() + ".admin.reload";

            if (!source.getSender().hasPermission(permission)) {
                source.getSender().sendMessage(
                        Component.text("You do not have permission to run this command!").color(NamedTextColor.RED)
                );
                return;
            }

            try {
                corex.reloadAll();

                source.getSender().sendMessage(
                        Component.text()
                                .append(Component.text("[" + pluginName + "] ").color(NamedTextColor.GREEN))
                                .append(Component.text("ALL configuration files have been successfully reloaded!").color(NamedTextColor.GRAY))
                                .build()
                );

            } catch (Exception e) {
                source.getSender().sendMessage(
                        Component.text("[" + pluginName + "] Failed to reload configs! Check console for syntax errors.").color(NamedTextColor.RED)
                );
                e.printStackTrace();
            }
        }
    }
}