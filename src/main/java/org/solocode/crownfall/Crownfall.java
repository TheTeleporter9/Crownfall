package org.solocode.crownfall;

import io.papermc.paper.command.brigadier.BasicCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.solocode.crownfall.Camera.CameraManager;
import org.solocode.crownfall.Commands.startCommand;
import org.solocode.crownfall.Enitys.Troops.Troop;
import org.solocode.crownfall.Events.ItemClickEvent;
import org.solocode.crownfall.Events.camMove;

import java.util.Map;
import java.util.UUID;

public final class Crownfall extends JavaPlugin {

    private CameraManager camManager;
    private Troop troop;

    @Override
    public void onEnable() {
        getLogger().info("hello world");

        initSystems();

        Bukkit.getScheduler().runTaskTimer(this, this::loop, 0L, 1L);
    }

    private void initSystems() {
        camManager = new CameraManager();
        troop = new Troop(this);
        addEventListeners();
        addCommands();
    }

    private void addCommands() {
        registerCommand("start", new startCommand());
    }

    private void addEventListeners() {
        getServer().getPluginManager().registerEvents(new camMove(), this);
        getServer().getPluginManager().registerEvents(new ItemClickEvent(troop), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loop() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            camManager.setupCamera(player);
            camManager.updateCamera(player);
        }

        for(Map.Entry<Mob, Location> entry : troop.mobTargets.entrySet()) {
           Mob mob = entry.getKey();
           Location loc = entry.getValue();
            troop.updateMob(mob, loc);
        }

    }
}