package org.solocode.crownfall.Camera;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Manages camera behavior for players in the game.
 * Handles camera setup and updates for in-game players.
 */
public class CameraManager {


    /**
     * Updates the camera state for a player.
     * Prevents vertical movement for in-game players.
     *
     * @param player the player to update the camera for
     */
    public void updateCamera(Player player) {

        if (!player.getScoreboardTags().contains("inGame")) {
            return;
        }

        // Prevent vertical movement
        Vector velocity = player.getVelocity();
        velocity.setY(0);
        player.setVelocity(velocity);
    }

    /**
     * Sets up the camera for a player entering or leaving the game.
     *
     * @param player the player to setup the camera for
     */
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