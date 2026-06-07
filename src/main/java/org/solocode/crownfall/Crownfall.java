package org.solocode.crownfall;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class Crownfall extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("hello world");

        initSystems();

        Bukkit.getScheduler().runTaskTimer(this, this::loop, 0L, 1L);
    }


    private void initSystems() {
        addEventListeners();
        addCommands();
    }


    private void addCommands() {

    }


    private void addEventListeners() {

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loop() {

    }
}