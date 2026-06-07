package org.solocode.crownfall.Player.Camera.EventListener;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.solocode.crownfall.Crownfall;
import org.solocode.crownfall.Player.Camera.Camera;
import org.solocode.crownfall.Player.Camera.CameraManager;

public class cameraInputListener implements Listener {
    private final Crownfall plugin;
    private final CameraManager cameraManager;

    public cameraInputListener(Crownfall plugin, CameraManager cameraManager) {
        this.plugin = plugin;
        this.cameraManager = cameraManager;
    }

    @EventHandler
    public void onHotbarScroll(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        
        if (!player.getScoreboardTags().contains("inGame")) return;
        
        Entity vehicle = player.getVehicle();
        if (vehicle == null || !vehicle.getScoreboardTags().contains("camera")) return;
        
        if (!cameraManager.getPacketListener().isShifting(player)) return;

        event.setCancelled(true);

        int previousSlot = event.getPreviousSlot();
        int currentSlot = event.getNewSlot();

        boolean scrollUp = (currentSlot > previousSlot && !(previousSlot == 0 && currentSlot == 8)) || (previousSlot == 8 && currentSlot == 0);
        player.sendMessage(Component.text("You are scrolling up: "+ scrollUp));

        Camera camera = cameraManager.getCamera(player);
        if (camera != null) {
            camera.zoom(scrollUp, plugin);
        }
    }


}
