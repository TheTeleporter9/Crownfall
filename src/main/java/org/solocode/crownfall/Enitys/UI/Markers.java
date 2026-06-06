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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;
import org.solocode.crownfall.Enitys.LocationType;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Markers {

    List<ArmorStand> markers = new ArrayList<>();

    public void createMarker(LocationType type, Location location) {
        World world = location.getWorld();

        ArmorStand stand = (ArmorStand) world.spawn(location, ArmorStand.class);

        stand.setInvisible(true);
        stand.setMarker(true);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setSilent(true);

        stand.getEquipment().setHelmet(setupHead(getTextureValueByLocationType(type)));
        stand.getEquipment().setHelmetDropChance(0);

        markers.add(stand);

    }

    public void removeMarker(ArmorStand stand) {
        if(stand == null)
    }

    public void updateMarker(World world) {
            for (ArmorStand stand : world.getEntitiesByClass(ArmorStand.class)) {

                if(!markers.contains(stand)) {
                    stand.remove();
                }
                animateMarker(stand);
            }
    }

    private void animateMarker(ArmorStand stand) {

        if(!stand.isValid() || stand.isDead()) {
            return;
        }

        double currentYaw = 0.0;
        currentYaw += Math.toRadians(5);

        EulerAngle currentPose = stand.getHeadPose();
        EulerAngle newPose = new EulerAngle(currentPose.getX(), currentYaw, currentPose.getZ());
    }

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
