package org.solocode.crownfall.Enitys.UI;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;
import org.solocode.crownfall.Enitys.LocationType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Manages visual markers displayed in the game world.
 * Creates and animates armor stand markers for visual feedback.
 */
public class Markers {

    public List<ArmorStand> markers = new ArrayList<>();
    private ArmorStand stand;

    /**
     * Creates a visual marker at the specified location.
     *
     * @param type     the type of marker to create
     * @param location the location to place the marker
     * @return the created armor stand marker
     */
    public ArmorStand createMarker(LocationType type, Location location) {
        World world = location.getWorld();

        stand = (ArmorStand) world.spawn(location, ArmorStand.class);

        stand.setInvisible(false);
        stand.setMarker(false);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setSilent(true);
        stand.setArms(true);
        stand.setBasePlate(false);
        stand.setSmall(true);

        stand.getEquipment().setHelmet(setupHead(getTextureValueByLocationType(type)));

        markers.add(stand);
        return stand;
    }

    /**
     * Removes a marker from the game world.
     *
     * @param stand the armor stand marker to remove
     */
    public void removeMarker(ArmorStand stand) {
        if(stand == null || !stand.isValid()) return;

        boolean wasInList = markers.remove(stand);

        if (wasInList) stand.remove();
    }

    /**
     * Gets the last created armor stand marker.
     *
     * @return the armor stand marker
     */
    public ArmorStand getArmorStand() {
        return stand;
    }

    /**
     * Animates all markers by rotating their heads.
     */
    public void animateAllMarkers() {
        // Create a copy to avoid ConcurrentModificationException
        List<ArmorStand> markersCopy = new ArrayList<>(markers);
        
        for (ArmorStand stand : markersCopy) {
            if (!stand.isValid() || stand.isDead()) {
                markers.remove(stand);
                continue;
            }

            EulerAngle currentPose = stand.getHeadPose();
            double currentYaw = currentPose.getY();
            currentYaw += Math.toRadians(5);

            stand.setHeadPose(new EulerAngle(currentPose.getX(), currentYaw, currentPose.getZ()));
        }
    }

    /**
     * Updates all markers in the world.
     *
     * @param world the world containing the markers
     */
    public void updateMarker(World world) {
            for (ArmorStand stand : world.getEntitiesByClass(ArmorStand.class)) {

                if(!markers.contains(stand)) {
                    stand.remove();
                }
            }
    }

    /**
     * Sets up a player head with a custom texture.
     *
     * @param textureValue the texture value string
     * @return the configured item stack
     */
    private ItemStack setupHead(String textureValue) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        head.editMeta(SkullMeta.class, skullMeta -> {
            PlayerProfile head_profile = Bukkit.createProfile(UUID.randomUUID(), "CustomHead");
            head_profile.setProperty(new ProfileProperty("textures", textureValue));

            skullMeta.setPlayerProfile(head_profile);

            Component name = Component.text("Sword/Attack")
                    .color(NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD)
                    .decorate(TextDecoration.ITALIC);

            skullMeta.displayName(name);

        });

        return head;
    }

    /**
     * Gets the texture value for a specific location type.
     *
     * @param type the location type
     * @return the texture value string
     */
    private String getTextureValueByLocationType(LocationType type) {
        switch (type) {
            case ATTACK -> {
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
            }
            case GOTO -> {
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNjM2E5YmRjOGM0MGM0MmQ4NDFkYWViNzFlYTllN2QxYzU0YWIzMWEyM2EyZDkyNjU5MWQ1NTUxNDExN2U1ZCJ9fX0=";
            }
            case DEFEND -> {
                return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGRlMWRkNzBlNTRmNDUzYWFmZjExZGU3ZTUxZDJmYjQ5ZTI2NDFlNDkxYTY5YjZhYzFlOWY2YTRjNmJjZDM5YSJ9fX0=";
            }
            default -> {
                return "Notch";
            }
        }
    }


}
