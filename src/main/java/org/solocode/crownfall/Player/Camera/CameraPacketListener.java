package org.solocode.crownfall.Player.Camera;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.solocode.crownfall.Crownfall;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CameraPacketListener implements Listener {
    private final Crownfall plugin;

    public CameraPacketListener(Crownfall plugin) {
        this.plugin = plugin;
    }


}
