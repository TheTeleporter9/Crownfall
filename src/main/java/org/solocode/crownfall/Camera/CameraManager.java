package org.solocode.crownfall.Camera;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.solocode.crownfall.Crownfall;

public class CameraManager {

    private final float cameraPitch = 60f;


    public void updateCamera(Player player) {

        if (!player.getScoreboardTags().contains("inGame")) {
            return;
        }

        // Prevent vertical movement
        Vector velocity = player.getVelocity();
        velocity.setY(0);
        player.setVelocity(velocity.multiply(10));

    }

    public void setupCamera(Player player) {

        if (!player.getScoreboardTags().contains("inGame")) {
            player.setFlySpeed(0.2f);
            player.setWalkSpeed(0.2f);
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed(1f);
        player.setWalkSpeed(1f);
    }
}