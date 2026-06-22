package org.solocode.crownfall.Game.TroopSystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class Troop {

    private List<TroopMember> troopMembers = new ArrayList<>();
    private int troopSize = 1;

    public Troop(int troopSize, TroopTypes troopTypes, Player troopOwner) {
        this.troopSize = troopSize;
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




    // This class represents one unit of a troop!
    private static class TroopMember {

        private final TroopTypes troopType;
        private int troopMemberLevel = 0;
        private Player owner;
        private boolean alive = true;

        public TroopMember(TroopTypes type, int troopMemberLevel, Player troopMemberOwner) {
            this.troopType = type;
            this.troopMemberLevel = troopMemberLevel;
            this.owner = troopMemberOwner;
        }

        public TroopTypes getTroopType() {
            return troopType;
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