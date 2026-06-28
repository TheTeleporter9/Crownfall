package org.solocode.crownfall.Game.TroopSystem;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.solocode.crownfall.Crownfall;

import java.util.ArrayList;
import java.util.List;

public class TroopManager {
    private Plugin plugin;
    private final NamespacedKey TROOP_SELECTOR_KEY;
    private final List<Troop> troops = new ArrayList<>();



    public TroopManager(Crownfall plugin) {
        this.plugin = plugin;

        TROOP_SELECTOR_KEY = new NamespacedKey(plugin, "selected");
    }

    /*TODO:
       Sotre and manage all troops!!!!
       1.Route Player commands (I.E attack, move_to, ect)
       2.Handle Global updates (I.e, cleanup dead troops, remove empty troops, sync state)
       3.Handle eninty/troop mapping
       4.handle combat routing (Not combat logic, just who needs to deal damage to whom)
       5.Handle Selection system
     */




}
