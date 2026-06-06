package org.solocode.crownfall.Enitys;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages point markers for entities in the game.
 */
public class EntityManger {

    private final Map<Location, LocationType> points = new HashMap<>();

    /**
     * Adds a point marker at the specified location.
     *
     * @param inputLocation the location of the marker
     * @param type          the type of location marker
     */
    public void addPointMarker(Location inputLocation, LocationType type) {
        points.put(inputLocation, type);
    }



}
