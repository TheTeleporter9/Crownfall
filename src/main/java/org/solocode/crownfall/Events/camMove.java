package org.solocode.crownfall.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.EventListener;

/**
 * Handles player movement events.
 * Manages vertical constraints for in-game players.
 */
public class camMove implements EventListener, Listener {

    /**
     * Handles player movement events.
     *
     * @param event the move event
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.getScoreboardTags().contains("inGame")) {
            return;
        }

        Location to = event.getTo();
        to.setPitch(60F);

        double y = to.getY();

        if (y < -58) {
            Vector push = new Vector(0, 0.5, 0);
            player.setVelocity(player.getVelocity().add(push));
        } else if (y > -40) {
            Vector push = new Vector(0, -0.5, 0);
            player.setVelocity(player.getVelocity().add(push));
        }
    }

}
