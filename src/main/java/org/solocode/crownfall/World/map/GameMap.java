package org.solocode.crownfall.World.map;

public class GameMap {

    private final String id;
    private final String displayName;
    private final String biomeId;
    private final String worldPath;

    public GameMap(String id, String displayName, String biomeId, String worldPath) {
        this.id = id;
        this.displayName = displayName;
        this.biomeId = biomeId;
        this.worldPath = worldPath;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBiomeId() {
        return biomeId;
    }

    /**
     * Path to the world folder (used for loading/copying maps)
     */
    public String getWorldPath() {
        return worldPath;
    }
}