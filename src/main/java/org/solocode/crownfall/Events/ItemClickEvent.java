package org.solocode.crownfall.Events;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.solocode.crownfall.Enitys.LocationType;
import org.solocode.crownfall.Enitys.Troops.Troop;
import org.solocode.crownfall.Enitys.Troops.TroopType;
import org.solocode.crownfall.Enitys.UI.Markers;

import java.util.EventListener;

/**
 * Handles item click events for players in the game.
 * Allows players to interact with troops and markers using items.
 */
public class ItemClickEvent implements EventListener, Listener {

    private final Troop troop;
    private final Markers markers;

    /**
     * Creates an ItemClickEvent handler.
     *
     * @param troop the troop manager
     * @param markers the markers manager
     */
    public ItemClickEvent(Troop troop, Markers markers) {
        this.troop = troop;
        this.markers = markers;
    }

    /**
     * Handles player interact events (item clicks).
     *
     * @param event the interact event
     */
    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(player.getScoreboardTags().contains("inGame")) {

            Location eye = player.getEyeLocation();
            Vector direction = eye.getDirection();

            RayTraceResult result = player.getWorld().rayTraceBlocks(
                    eye,
                    direction,
                    25,
                    FluidCollisionMode.NEVER,
                    true,
                    block -> block.getType() != Material.BARRIER //Ignore barriers
            );

            if (result == null || result.getHitBlock() == null) return;

            Block block = result.getHitBlock();

            Particle.GUST_EMITTER_LARGE.builder()
                    .location(block.getLocation())
                    .count(10)
                    .receivers(32, true)
                    .spawn();

            assert event.getItem() != null;
            if(event.getItem().getType() == Material.DIAMOND) {
                troop.createNewTroop(block.getLocation(), TroopType.Infantry);
            }

            if(event.getItem().getType() == Material.STICK) {
                for(Entity entity : troop.getEntityByGroup(event.getPlayer().getWorld())) {
                    ArmorStand marker = markers.createMarker(LocationType.GOTO, block.getLocation());
                    troop.setGoalPoint(block.getLocation(), entity, marker);
                }
            }

            if(event.getItem().getType() == Material.BREEZE_ROD) {

            }

        }
    }

}
