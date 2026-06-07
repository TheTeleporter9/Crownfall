package org.solocode.crownfall;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.crownfall.Player.Camera.Camera;
import org.solocode.crownfall.Player.Camera.CameraPacketListener;
import org.solocode.crownfall.Player.Camera.EventListener.cameraInputListener;

import org.solocode.crownfall.Commands.startCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class Crownfall extends JavaPlugin {

    public ProtocolManager protocolManager;
    private final Map<UUID, Camera> activeCameras = new HashMap<>();
    private CameraPacketListener cameraPacketListener = new CameraPacketListener(this);

    @Override
    public void onEnable() {
        getLogger().info("hello world");

        protocolManager = ProtocolLibrary.getProtocolManager();

        initSystems();

        Bukkit.getScheduler().runTaskTimer(this, this::loop, 0L, 1L);
    }


    private void initSystems() {
        CameraPacketListener.register();
        addEventListeners();
        addCommands();
    }


    private void addCommands() {
        getCommand("start").setExecutor(new startCommand());
    }


    private void addEventListeners() {
        getServer().getPluginManager().registerEvents(new cameraInputListener(this), this);

    }

    @Override
    public void onDisable() {
        for (Camera camera : ac)
    }

    public void loop() {

    }


    public Camera getCamera(Player player) { return activeCameras.get(player.getUniqueId()); }


}