package org.solocode.crownfall.Player.Camera;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.solocode.crownfall.Crownfall;
import org.solocode.crownfall.Player.Camera.EventListener.cameraInputListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CameraManager {
    private final Crownfall plugin;
    private final Map<UUID, Camera> activeCameras = new HashMap<>();
    private final CameraPacketListener cameraPacketListener;

    public CameraManager(Crownfall plugin) {
        this.plugin = plugin;
        this.cameraPacketListener = new CameraPacketListener(plugin);
    }



    public void initialize() {
        cameraPacketListener.register(plugin.protocolManager);
        plugin.getServer().getPluginManager().registerEvents(new cameraInputListener(plugin, this), plugin);
    }

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getScoreboardTags().contains("inGame")) continue;

            Camera camera = activeCameras.get(player.getUniqueId());
            if (camera == null) continue;

            PlayerInputType input = cameraPacketListener.getPlayerMovement(player);
            switch (input) {
                case MOVE_FORWARD:
                    camera.move(0, -1);
                    break;
                case MOVE_BACKWARD:
                    camera.move(0, 1);
                    break;
                case MOVE_LEFT:
                    camera.move(-1, 0);
                    break;
                case MOVE_RIGHT:
                    camera.move(1, 0);
                    break;
                case NONE:
                    camera.move(0, 0);
                    break;
                default:
                    break;
            }
        }
    }

    public void enableCamera(Player player) {
//        if (activeCameras.containsKey(player.getUniqueId())) {
//            return;
//        }
        Camera camera = new Camera(player);
        camera.enable();
        activeCameras.put(player.getUniqueId(), camera);
    }

    public void disableCamera(Player player) {
        Camera camera = activeCameras.remove(player.getUniqueId());
        if (camera != null) {
            camera.disable();
            player.removeScoreboardTag("inGame");
        }
    }

    public void disableAll() {
        for (Camera camera : activeCameras.values()) {
            camera.disable();
        }
        activeCameras.clear();
    }

    public Camera getCamera(Player player) {
        return activeCameras.get(player.getUniqueId());
    }

    public CameraPacketListener getPacketListener() {
        return cameraPacketListener;
    }
}
