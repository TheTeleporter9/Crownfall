package org.solocode.crownfall.Camera;

import org.bukkit.entity.Player;

public class CameraManager {

    /**
     * Updates the camera state for a player.
     */
    public void updateCamera(Player player) {
        if (!player.getScoreboardTags().contains("inGame")) {
            return;
        }

    }

    /**
     * Sets up the camera for a player entering or leaving the game.
     */
    public void setupCamera(Player player) {
        if (!player.getScoreboardTags().contains("inGame")) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setFlySpeed(0.1f); // Default Minecraft fly speed
            player.setWalkSpeed(0.2f); // Default Minecraft walk speed
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed(0.15f);
        player.setWalkSpeed(0.2f);
    }
}