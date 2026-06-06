package org.solocode.crownfall.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.EventListener;

public class camMove implements EventListener, Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.getScoreboardTags().contains("inGame")) {
            return;
        }

        Location location = event.getTo();

        Location to = event.getTo();

        to.setPitch(60F);

        double y = location.getY();

        if (y < -58) {
            Vector push = new Vector(0, 1, 0); //push up

            player.setVelocity(player.getVelocity().add(push));
        } else if (y > -40) {
            Vector push = new Vector(0, -1, 0); //push down
            player.setVelocity(player.getVelocity().add(push));
        }

    }

}
