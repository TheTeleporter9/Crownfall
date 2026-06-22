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
import org.solocode.crownfall.Crownfall;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class Troop {

    private List<TroopMember> troopMembers = new ArrayList<>();
    private int troopSize = 1;
    private NamespacedKey namespacedKey;

    public Troop(int troopSize, TroopTypes troopTypes, Player troopOwner, NamespacedKey namespacedKey) {
        this.troopSize = troopSize;
        this.namespacedKey = namespacedKey;
        troopMembers.add(
                new TroopMember(troopTypes, 1, troopOwner)
        );
    }

    public void spawn(Location location) {
        World world = location.getWorld();

        for(TroopMember troopMember : troopMembers) {
            assert getMobTypeFromTroopType(troopMember.getTroopType()) != null;
            Mob mob = (Mob) world.spawnEntity(
                    location, getMobTypeFromTroopType(troopMember.getTroopType())
            );

            final Component troopTypeText = text()
                    .append(text(
                            troopMember.getTroopType().toString(),
                            NamedTextColor.GOLD
                    ))
                    .append(text(
                            " Troop",
                            NamedTextColor.AQUA
                    ))
                    .build();

            final Component troopLevelText =
                    text(" LvL. " + troopMember.getTroopMemberLevel(),
                            NamedTextColor.RED);

            final Component separatorText =
                    text(" | ", NamedTextColor.GRAY);

            mob.customName(
                    troopTypeText
                            .append(separatorText)
                            .append(troopLevelText)
            );

            mob.setCustomNameVisible(true);

            mob.setAI(false);

            mob.addScoreboardTag("troop");
            mob.addScoreboardTag(troopMember.getTroopType().toString() + "-troop");

            //Set the mob/entity selected to false
            mob.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, false);

            troopMember.setMob(mob);
        }

    }

    private EntityType getMobTypeFromTroopType(TroopTypes type) {
        switch (type) {
            case Infantry -> {
                return EntityType.SKELETON;
            }
            case Defence -> {
                return EntityType.ZOMBIE;
            }
        }
        return null;
    }

    /***
     * Make troop entities go to a location
     * Only if they have the data tag, of selected
     */
    public void goTo(Location location) {
        World world = location.getWorld();

        for(TroopMember troopMember : troopMembers) {
            Mob troopMemberMob = troopMember.getMob();
            if(!(troopMemberMob.getScoreboardTags().contains("troop"))) return;

            PersistentDataContainer pdc = troopMemberMob.getPersistentDataContainer();
            Boolean selected = pdc.get(namespacedKey, PersistentDataType.BOOLEAN);

            //Make shure that it is the correct troop;
            assert selected != null;
            if(!selected) return;

            Pathfinder pathfinder = troopMemberMob.getPathfinder();
            pathfinder.moveTo(location);

        }
    }


    // This class represents one unit of a troop!
    private static class TroopMember {

        private final TroopTypes troopType;
        private int troopMemberLevel = 0;
        private Player owner;
        private boolean alive = true;
        private Mob mob;

        public TroopMember(TroopTypes type, int troopMemberLevel, Player troopMemberOwner) {
            this.troopType = type;
            this.troopMemberLevel = troopMemberLevel;
            this.owner = troopMemberOwner;
        }

        public TroopTypes getTroopType() {
            return troopType;
        }

        public Mob setMob(Mob mob) {
            return this.mob = mob;
        }

        public Mob getMob() {
            return mob;
        }

        public int getTroopMemberLevel() {
            return troopMemberLevel;
        }

        public void setTroopMemberLevel(int troopMemberLevel) {
            this.troopMemberLevel = troopMemberLevel;
        }

        public Player getOwner() {
            return owner;
        }

        public void setOwner(Player owner) {
            this.owner = owner;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        public void upgrade() {
            this.troopMemberLevel++;
        }

        public void kill() {
            this.alive = false;
        }

        public void revive() {
            this.alive = true;
        }

    }

}