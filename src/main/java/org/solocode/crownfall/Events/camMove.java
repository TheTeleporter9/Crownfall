package org.solocode.crownfall.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class camMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.getScoreboardTags().contains("inGame")) {
            return;
        }

        Location to = event.getTo();

        // We only override the pitch if the client tries to look up or down.
        // Yaw (left/right) is completely untouched so they can spin freely.
        if (to.getPitch() != 60F) {
            to.setPitch(60F);
            event.setTo(to);
        }

       
        double y = to.getY();
        Vector vel = player.getVelocity();

        if (y < -58.0) {
            // Calculate how far below the line they are
            double depth = -58.0 - y;

            // Apply a smooth upward velocity (cap at 0.3)
            double smoothPush = Math.min(0.3, depth * 0.15);

            vel.setY(smoothPush);
            player.setVelocity(vel);

        } else if (y > -40.0) {
            // Calculate how far above the line they are
            double height = y - (-40.0);

            // Apply a smooth downward velocity (cap at -0.3)
            double smoothPush = Math.max(-0.3, -height * 0.15);

            vel.setY(smoothPush);
            player.setVelocity(vel);
        }
    }
}