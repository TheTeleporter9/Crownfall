package org.solocode.crownfall.TroopSystem;

import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Troop {

    private int level = 1;
    private TroopTypes troopType;
    private final List<TroopMember> troopMembers = new ArrayList<>();

    public Troop(int troopLevel, TroopTypes troopType) {
        this.level = troopLevel;
        this.troopType = troopType;
    }

    public void initializeTroop() {
        switch (troopType) {
            case Defence -> initDefenceTroop();
            case Infantry -> initInfantryTroop();
        }
    }

    private void initDefenceTroop() {
        for (int i = 0; i < level; i++) {
            troopMembers.add(new TroopMember(TroopTypes.Defence, level));
        }
    }

    private void initInfantryTroop() {
        for (int i = 0; i < level; i++) {
            troopMembers.add(new TroopMember(TroopTypes.Infantry, level));
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TroopTypes getTroopType() {
        return troopType;
    }

    public void setTroopType(TroopTypes troopType) {
        this.troopType = troopType;
    }

    public List<TroopMember> getTroopMembers() {
        return troopMembers;
    }

    public static class TroopMember {

        private TroopTypes type;
        private int level;
        private UUID entityId;

        public TroopMember(TroopTypes type, int level) {
            this.type = type;
            this.level = level;
        }

        public TroopTypes getType() {
            return type;
        }

        public void setType(TroopTypes type) {
            this.type = type;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public UUID getEntityId() {
            return entityId;
        }

        public void setEntity(Entity entity) {
            this.entityId = entity.getUniqueId();
        }

        public void setEntityId(UUID entityId) {
            this.entityId = entityId;
        }
    }
}