package org.solocode.crownfall.Enitys.Troops;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.solocode.crownfall.Crownfall;
import org.solocode.crownfall.Enitys.UI.Markers;

import java.util.*;
import org.bukkit.entity.ArmorStand;

/**
 * Manages troop entities and their movement.
 * Handles creation, tracking, and control of troops.
 */
public class Troop {
    private int globalCurrentID = 0;
    private final NamespacedKey troopKey;

    public final Map<Mob, Location> mobTargets = new HashMap<>();
    public final Map<Mob, ArmorStand> mobMarkers = new HashMap<>();

    /**
     * Creates a new Troop manager.
     *
     * @param plugin the main plugin instance
     */
    public Troop(Crownfall plugin) {
        this.troopKey = new NamespacedKey(plugin, "troop");
    }

    /**
     * Creates a new troop at the specified location.
     *
     * @param loc  the location to spawn the troop
     * @param type the type of troop to create
     */
    public void createNewTroop(Location loc, TroopType type) {
        Zombie zombie = loc.getWorld().spawn(loc.add(0, 1, 0), Zombie.class);

        zombie.customName(
                Component.text("LvL 1")
                        .color(NamedTextColor.AQUA)
        );

        zombie.setCustomNameVisible(true);

        zombie.setHealth(10);
        zombie.setAI(true);
        zombie.setVisualFire(TriState.FALSE);
        zombie.setInvulnerable(true);

        globalCurrentID++;
        int id = globalCurrentID;

        zombie.getPersistentDataContainer().set(
                troopKey,
                PersistentDataType.INTEGER,
                id
        );
    }

    /**
     * Checks if an entity belongs to a specific troop.
     *
     * @param entity the entity to check
     * @param ID     the troop ID
     * @return true if the entity is in the troop, false otherwise
     */
    public boolean isInTroop(Entity entity, int ID) {
        if (!entity.getPersistentDataContainer().has(
                troopKey,
                PersistentDataType.INTEGER
        )) {
            return false;
        }

        Integer storedID = entity.getPersistentDataContainer().get(troopKey, PersistentDataType.INTEGER);
        return storedID != null && storedID == ID;
    }

    /**
     * Gets all entities belonging to a specific troop group.
     *
     * @param world   the world to search in
     * @param groupID the troop ID to search for
     * @return a list of entities in the troop
     */
    public List<Entity> getEntityByGroupID(World world, int groupID) {
        List<Entity> matchedEntities = new ArrayList<>();

        for(Entity entity: world.getEntities()) {
            if(entity.getPersistentDataContainer().has(
                    troopKey, PersistentDataType.INTEGER
            )) {
                Integer storedID = entity.getPersistentDataContainer().get(troopKey, PersistentDataType.INTEGER);
                if(storedID !=null && storedID == groupID) {
                    matchedEntities.add(entity);
                }
            }
        }

        return matchedEntities;
    }

    /**
     * Gets all troops in a world.
     *
     * @param world the world to search in
     * @return a list of all entities in any troop
     */
    public List<Entity> getEntityByGroup(World world) {
        List<Entity> matchedEntities = new ArrayList<>();

        for(Entity entity: world.getEntities()) {
            if(entity.getPersistentDataContainer().has(
                    troopKey, PersistentDataType.INTEGER
            )) {
                matchedEntities.add(entity);
            }
        }

        return matchedEntities;
    }

    /**
     * Sets a goal point for a mob to pathfind to.
     *
     * @param target the target location
     * @param entity the entity to set the goal for
     * @param marker the armor stand marker to associate with this mob
     */
    public void setGoalPoint(Location target, Entity entity, ArmorStand marker) {

        if (!(entity instanceof Mob mob)) return;

        mobTargets.put(mob, target);
        mobMarkers.put(mob, marker);
        mob.setAI(true);
    }

    /**
     * Updates a mob's movement towards its target.
     *
     * @param mob    the mob to update
     * @param target the target location
     * @param markers the marker manager
     */
    public void updateMob(Mob mob, Location target, Markers markers) {
        // Remove if mob is dead
        if (!mob.isValid() || mob.isDead()) {
            ArmorStand associatedMarker = mobMarkers.remove(mob);
            Bukkit.getLogger().info("Please remvoe!");
            if (associatedMarker != null) {
                markers.removeMarker(associatedMarker);
                ArmorStand stand = markers.getArmorStand();
                stand.remove();
            }
            mobTargets.remove(mob);
            return;
        }

        double distance = mob.getLocation().distance(target);

        // Reached target - stop movement and remove marker
        if (distance <= 2.0) {
            mob.setAI(false);
            mob.setVelocity(new Vector(0, 0, 0));
            
            // Remove the associated marker
            ArmorStand associatedMarker = mobMarkers.remove(mob);
            if (associatedMarker != null) {
                markers.removeMarker(associatedMarker);
            }
            
            mobTargets.remove(mob);
            return;
        }

        // Move towards target
        mob.setAI(true);
        mob.getPathfinder().moveTo(target, 1.0);
    }

    /**
     * Gets the namespaced key used for troop identification.
     *
     * @return the troop identification key
     */
    public NamespacedKey getTroopKey() {
        return troopKey;
    }

}
