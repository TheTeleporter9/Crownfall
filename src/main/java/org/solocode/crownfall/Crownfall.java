package org.solocode.crownfall;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.crownfall.Camera.CameraManager;
import org.solocode.crownfall.Commands.startCommand;
import org.solocode.crownfall.Enitys.Troops.Troop;
import org.solocode.crownfall.Enitys.UI.Markers;
import org.solocode.crownfall.Events.ItemClickEvent;
import org.solocode.crownfall.Events.camMove;

import java.util.ArrayList;
import java.util.Map;

public final class Crownfall extends JavaPlugin {

    private CameraManager camManager;
    private Troop troop;
    private Markers markers;

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        getLogger().info("hello world");

        initSystems();

        Bukkit.getScheduler().runTaskTimer(this, this::loop, 0L, 1L);
    }

    /**
     * Initializes all game systems including camera, troops, and markers.
     */
    private void initSystems() {
        camManager = new CameraManager();
        troop = new Troop(this);
        markers = new Markers();
        addEventListeners();
        addCommands();
    }

    /**
     * Registers all plugin commands.
     */
    private void addCommands() {
        registerCommand("start", new startCommand());
    }

    /**
     * Registers all event listeners for the plugin.
     */
    private void addEventListeners() {
        getServer().getPluginManager().registerEvents(new camMove(), this);
        getServer().getPluginManager().registerEvents(new ItemClickEvent(troop, markers), this);
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Main game loop executed every tick.
     * Updates camera positions and mob movements.
     */
    public void loop() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            camManager.setupCamera(player);
            camManager.updateCamera(player);
        }

        // Create a copy to avoid ConcurrentModificationException
        for(Map.Entry<Mob, Location> entry : new ArrayList<>(troop.mobTargets.entrySet())) {
           Mob mob = entry.getKey();
           Location loc = entry.getValue();
           troop.updateMob(mob, loc, markers);
        }

        // Animate all markers
        markers.animateAllMarkers();
    }
}