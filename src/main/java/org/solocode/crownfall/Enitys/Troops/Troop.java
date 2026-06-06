package org.solocode.crownfall.Enitys.Troops;

import com.destroystokyo.paper.entity.Pathfinder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.solocode.crownfall.Crownfall;

import java.util.ArrayList;
import java.util.List;

public class Troop {
    private int globalCurrentID = 0;
    private final NamespacedKey troopKey;
    private final Crownfall plugin;

    public Troop(Crownfall plugin) {
        this.plugin = plugin;
        this.troopKey = new NamespacedKey(plugin, "troop");
    }

    public void createNewTroop(Location loc, TroopType type) {

        World world = loc.getWorld();

        Zombie zombie = (Zombie) world.spawn(loc, Zombie.class);

        zombie.customName(
                Component.text("LvL 1")
                        .color(NamedTextColor.AQUA)
        );

        zombie.setHealth(10);

        zombie.setAI(true); // IMPORTANT

        globalCurrentID++;
        final int currentTroopID = globalCurrentID;

        zombie.getPersistentDataContainer().set(
                troopKey,
                PersistentDataType.INTEGER,
                currentTroopID
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

    public void setGoalPoint(Location target, Entity entity) {
         if(!(entity instanceof Mob mob)) {
             return;
         }

        Vector dir = target.toVector()
                .subtract(mob.getLocation().toVector())
                .normalize();
         mob.setVelocity(dir.multiply(0.35));


    }

    public NamespacedKey getTroopKey() {
        return troopKey;
    }

}
