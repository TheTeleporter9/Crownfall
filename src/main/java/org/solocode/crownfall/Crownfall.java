package org.solocode.crownfall;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.solocode.crownfall.Camera.CameraManager;
import org.solocode.crownfall.Events.ClickEvent;
import org.solocode.crownfall.Events.camMove;

public final class Crownfall extends JavaPlugin {

    private CameraManager camManager;


    @Override
    public void onEnable() {
        getLogger().info("hello world");

        initSystems();

        Bukkit.getScheduler().runTaskTimer(this, this::loop, 0L, 1L);
    }

    private void initSystems() {
        camManager = new CameraManager();
        addEventListeners();
    }

    private void addEventListeners() {
        getServer().getPluginManager().registerEvents(new camMove(), this);
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
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
    }
}