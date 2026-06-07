package org.solocode.crownfall;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.crownfall.Commands.startCommand;
import org.solocode.crownfall.Player.Camera.CameraManager;
import org.solocode.crownfall.Player.Camera.CameraPacketListener;
import org.solocode.crownfall.Player.Camera.EventListener.cameraInputListener;

public final class Crownfall extends JavaPlugin {

    public ProtocolManager protocolManager;
    private CameraManager cameraManager;
    private CameraPacketListener cameraPacketListener;

    @Override
    public void onEnable() {
        getLogger().info("Crownfall enabled");

        protocolManager = ProtocolLibrary.getProtocolManager();
        cameraManager = new CameraManager(this);

        initSystems();
        Bukkit.getScheduler().runTaskTimer(this, this::update, 0L, 1L);
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        //cameraPacketListener.register(protocolManager);
    }

    private void initSystems() {
        cameraManager.initialize();
        registerCommand("start", new startCommand(this, cameraManager));
        getServer().getPluginManager().registerEvents(new cameraInputListener(this, cameraManager), this);
    }

    public void update() {
       cameraManager.update();
    }

    @Override
    public void onDisable() {
//        cameraManager.disableAll();
        getLogger().info("Crownfall disabled");
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

}
