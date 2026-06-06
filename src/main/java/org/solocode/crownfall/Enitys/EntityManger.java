package org.solocode.crownfall.Enitys;

import jdk.javadoc.doclet.Taglet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManger {

    Map<Location, LocationType> points = new HashMap<>();

    public void addPointMarker(Location inputLocation, LocationType type) {
        points.put(inputLocation, type);
    }



}
