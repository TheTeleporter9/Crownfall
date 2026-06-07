package org.solocode.crownfall.Player.Camera;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.solocode.crownfall.Crownfall;


public class Camera {
    private final Player player;
    private ArmorStand camera;

    private final double moveSpeed = 0.6;
    private final double zoomSpeed = 0.5;
    private final int maxZoomHeight = 2000;
    private final int minZoomHeight = -10;

    private final int playerCameraRotaion = 75;
    public Camera(Player player) {
        this.player = player;
    }

    public void enable() {
        Location loc = player.getLocation().add(0, 20, 0);

        this.camera = loc.getWorld().spawn(loc, ArmorStand.class, stand -> {
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.addScoreboardTag("camera");
        });

        player.addPotionEffect(
                new PotionEffect(
                        PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0, false, false
                )
        );

        player.setRotation(0, playerCameraRotaion);
        player.addScoreboardTag("inGame");

        this.camera.addPassenger(player);

    }

    public void disable() {
        if(camera != null && camera.isValid()) {
            camera.removePassenger(player);
            camera.remove();
        }
    }

    public void move(float sideways, float forward) {
        if (camera == null || !camera.isValid()) return;

        Vector movement = new Vector(-sideways, 0, -forward);

        if (movement.lengthSquared() > 0) {
            movement.normalize().multiply(moveSpeed);

            camera.teleport(
                    camera.getLocation().add(movement)
            );
        }
    }

    public void zoom(boolean zoomOut) {
        if (camera == null || !camera.isValid()) {
            return;
        }

        Location loc = camera.getLocation();

        if (zoomOut && loc.getY() < maxZoomHeight) {
            Bukkit.getLogger().info("zoom in");
            camera.teleport(loc.add(0, zoomSpeed, 0));
        } else if (!zoomOut && loc.getY() > minZoomHeight) {
            Bukkit.getLogger().info("zoom out");
            camera.teleport(loc.add(0, -zoomSpeed, 0));
        }
    }

    public ArmorStand getCamera() {
        return camera;
    }

}
