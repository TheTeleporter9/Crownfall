package org.solocode.crownfall.Events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.solocode.crownfall.Crownfall;
import org.solocode.crownfall.Enitys.EntityManger;
import org.solocode.crownfall.Enitys.Troops.Troop;
import org.solocode.crownfall.Enitys.Troops.TroopType;

import java.util.EventListener;

public class ItemClickEvent implements EventListener, Listener {

    private final Troop troop;

    public ItemClickEvent(Troop troop) {
        this.troop = troop;
    }

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
                    troop.setGoalPoint(block.getLocation(), entity);
                }
            }

            if(event.getItem().getType() == Material.BREEZE_ROD) {

            }

        }
    }

}
