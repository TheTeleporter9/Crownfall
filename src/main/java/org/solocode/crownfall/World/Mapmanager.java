package org.solocode.crownfall.World;

import org.bukkit.plugin.Plugin;
import org.solocode.betterConfig.BetterConfig;
import org.solocode.betterConfig.ConfigType;

import java.lang.foreign.PaddingLayout;

public class Mapmanager {
    private BetterConfig config;
    private Plugin plugin;

    public Mapmanager(Plugin plugin)
    {
        this.plugin = plugin;
        config = new BetterConfig(plugin);
    }

    void createConfig() {
        config.createConfig(ConfigType.Custom, "map-config");
        config.addSectionDefault("map-config", "default", biome ->
                biome.setDefaults(
                        ""
                )
                );
    }
}
