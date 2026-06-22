package org.solocode.crownfall.Game.TroopSystem;

import com.destroystokyo.paper.entity.Pathfinder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class Troop {

    private final List<TroopMember> troopMembers = new ArrayList<>();
    private int troopSize = 1;

    private final NamespacedKey selectedKey;
    private final NamespacedKey healthKey;

    private boolean isPathfinding = false;

    public Troop(int troopSize,
                 TroopTypes troopTypes,
                 Player troopOwner,
                 NamespacedKey selectedKey,
                 NamespacedKey healthKey) {

        this.troopSize = troopSize;
        this.selectedKey = selectedKey;
        this.healthKey = healthKey;

        troopMembers.add(
                new TroopMember(troopTypes, 1, troopOwner)
        );
    }


    public void spawn(Location location) {
        World world = location.getWorld();

        for (int i = 0; i < troopSize; i++) {
            for (TroopMember troopMember : troopMembers) {

                Mob mob = (Mob) world.spawnEntity(
                        location,
                        getMobTypeFromTroopType(troopMember.getTroopType())
                );

                Component name =
                        text(troopMember.getTroopType().toString(), NamedTextColor.GOLD)
                                .append(text(" Troop", NamedTextColor.AQUA))
                                .append(text(" | LvL " + troopMember.getTroopMemberLevel(), NamedTextColor.RED));

                mob.customName(name);
                mob.setCustomNameVisible(true);

                mob.setAI(false);

                mob.addScoreboardTag("troop");
                mob.addScoreboardTag(troopMember.getTroopType().toString() + "-troop");

                mob.getPersistentDataContainer().set(
                        selectedKey,
                        PersistentDataType.BOOLEAN,
                        false
                );


                mob.getPersistentDataContainer().set(
                        healthKey,
                        PersistentDataType.INTEGER,
                        getMaxHealth(troopMember)
                );

                troopMember.setMob(mob);
            }
        }
    }


    public void goTo(Location location) {
        isPathfinding = true;

        for (TroopMember troopMember : troopMembers) {

            Mob mob = troopMember.getMob();
            if (mob == null || mob.isDead()) continue;

            if (!mob.getScoreboardTags().contains("troop")) continue;

            Boolean selected = mob.getPersistentDataContainer()
                    .get(selectedKey, PersistentDataType.BOOLEAN);

            if (selected != null && !selected) continue;

            mob.setAI(true);
            mob.getPathfinder().moveTo(location);
        }
    }

    public void stop() {
        isPathfinding = false;

        for (TroopMember member : troopMembers) {

            Mob mob = member.getMob();
            if (mob == null || mob.isDead()) continue;

            mob.getPathfinder().stopPathfinding();
            mob.setAI(false);
            mob.setVelocity(new Vector(0, 0, 0));
        }
    }


    public int calculateDamage(Entity target) {
        if (!(target instanceof LivingEntity)) return 0;

        int total = 0;

        for (TroopMember member : troopMembers) {

            Mob mob = member.getMob();
            if (mob == null || mob.isDead()) continue;

            if (mob.getLocation().distance(target.getLocation()) <= 3.5) {
                total += 2 * (1 + member.getTroopMemberLevel());
            }
        }

        return total;
    }


    private int getHealth(Mob mob) {
        Integer hp = mob.getPersistentDataContainer()
                .get(healthKey, PersistentDataType.INTEGER);

        return hp == null ? 0 : hp;
    }

    private void setHealth(Mob mob, int hp) {
        mob.getPersistentDataContainer().set(
                healthKey,
                PersistentDataType.INTEGER,
                Math.max(0, hp)
        );
    }

    private void removeHealth(Mob mob, int amount) {
        setHealth(mob, getHealth(mob) - amount);
    }

    public void applyDamage(Mob mob, int damage) {

        if (mob == null || mob.isDead()) return;

        removeHealth(mob, damage);

        if (getHealth(mob) <= 0) {
            killUnit(mob);
        }
    }

    public void heal(Mob mob, int amount) {
        if (mob == null || mob.isDead()) return;

        int hp = getHealth(mob);
        setHealth(mob, hp + amount);
    }

    private void killUnit(Mob mob) {
        mob.setAI(false);
        mob.getPathfinder().stopPathfinding();

        mob.setHealth(0);
        mob.setVelocity(new Vector(0, 0, 0));

        mob.removeScoreboardTag("troop");
    }

    private int getMaxHealth(TroopMember member) {
        return 20 * member.getTroopMemberLevel();
    }


    private EntityType getMobTypeFromTroopType(TroopTypes type) {
        return switch (type) {
            case Infantry -> EntityType.SKELETON;
            case Defence -> EntityType.ZOMBIE;
        };
    }


    private static class TroopMember {

        private final TroopTypes troopType;
        private int troopMemberLevel;
        private Player owner;
        private Mob mob;

        public TroopMember(TroopTypes type, int level, Player owner) {
            this.troopType = type;
            this.troopMemberLevel = level;
            this.owner = owner;
        }

        public TroopTypes getTroopType() {
            return troopType;
        }

        public int getTroopMemberLevel() {
            return troopMemberLevel;
        }

        public Player getOwner() {
            return owner;
        }

        public Mob getMob() {
            return mob;
        }

        public void setMob(Mob mob) {
            this.mob = mob;
        }

        public void upgrade() {
            troopMemberLevel++;
        }
    }
}