package org.solocode.crownfall.Game.TroopSystem;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.solocode.crownfall.Crownfall;

public class TroopManager {
    private Plugin plugin;
    private final NamespacedKey TROOP_SELECTOR_KEY;

    public TroopManager(Crownfall plugin) {
        this.plugin = plugin;

        TROOP_SELECTOR_KEY = new NamespacedKey(plugin, "selected");
    }


}
