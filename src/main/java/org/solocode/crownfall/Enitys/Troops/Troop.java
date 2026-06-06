package org.solocode.crownfall.Enitys.Troops;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.solocode.crownfall.Camera.Selection;
import org.solocode.crownfall.Crownfall;
import org.solocode.crownfall.Enitys.UI.Markers;

import java.util.*;


public class Troop {
    private int globalCurrentID = 0;
    private final NamespacedKey troopKey;
    private final Crownfall plugin;

    public final Map<Mob, Location> mobTargets = new HashMap<>();

    public Troop(Crownfall plugin) {
        this.plugin = plugin;
        this.troopKey = new NamespacedKey(plugin, "troop");
    }

    public void createNewTroop(Location loc, TroopType type) {
        Zombie zombie = loc.getWorld().spawn(loc.add(0, 1, 0), Zombie.class);

        zombie.customName(
                Component.text("LvL 1")
                        .color(NamedTextColor.AQUA)
        );

        zombie.setCustomNameVisible(true);

        zombie.setHealth(10);
        zombie.setAI(true);

        globalCurrentID++;
        int id = globalCurrentID;

        zombie.getPersistentDataContainer().set(
                troopKey,
                PersistentDataType.INTEGER,
                id
        );
    }

    public boolean isInTroop(Entity entity, int ID) {
        if (!entity.getPersistentDataContainer().has(
                troopKey,
                PersistentDataType.INTEGER
        )) {
            return false;
        }

        Integer storedID = entity.getPersistentDataContainer().get(troopKey, PersistentDataType.INTEGER);
        return storedID !=null && storedID == ID;
    }

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

    public List<Entity> getEntityByGroup(World world) {
        List<Entity> matchedEntities = new ArrayList<>();

        for(Entity entity: world.getEntities()) {
            if(entity.getPersistentDataContainer().has(
                    troopKey, PersistentDataType.INTEGER
            )) {
                Integer storedID = entity.getPersistentDataContainer().get(troopKey, PersistentDataType.INTEGER);

                    matchedEntities.add(entity);

            }
        }

        return matchedEntities;
    }

    int trys = 5;
    public void setGoalPoint(Location target, Entity entity) {

        if (!(entity instanceof Mob mob)) return;

        Pathfinder pathfinder = mob.getPathfinder();

        mobTargets.put(mob, target);
    }

    public void updateMob(Mob mob, Location target, Markers marker) {

        double distance = mob.getLocation().distance(target);

        if (distance <= 1.5) {
            mob.setAI(false);
            mob.setVelocity(new Vector(0, 0, 0));
            marker.ma
            return;
        }

        mob.setAI(true);
        mob.getPathfinder().moveTo(target, 1.2);
    }

    public NamespacedKey getTroopKey() {
        return troopKey;
    }

}
