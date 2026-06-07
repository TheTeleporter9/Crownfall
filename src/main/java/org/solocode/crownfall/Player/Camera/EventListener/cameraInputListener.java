package org.solocode.crownfall.Player.Camera.EventListener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;


public class cameraInputListener implements Listener {


    @EventHandler
    public void onHotbarScroll(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Entity camera = player.getVehicle();

        if(camera == null || !camera.getScoreboardTags().contains("camera")) {
            return;
        }

        event.setCancelled(true);

        int previousSlot = event.getPreviousSlot();
        int currentSlot = event.getNewSlot();

        //Detect direction of scroll wheel
        boolean scrollUp = (currentSlot > previousSlot && !(previousSlot == 0 && currentSlot == 8)) || (previousSlot == 8 & currentSlot == 0);
    }



}
