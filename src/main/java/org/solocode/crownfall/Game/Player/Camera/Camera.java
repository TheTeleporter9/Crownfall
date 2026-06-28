package org.solocode.crownfall.Game.Player.Camera;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.solocode.Corex.config.Config;

import java.util.Map;


public class Camera {
    private final Player player;
    private ArmorStand camera;

    @Config("cameraConfig.moveSpeed")
    private double moveSpeed = 0.6;

    @Config("cameraConfig.zoomSpeed")
    private double zoomSpeed = 0.5;

    @Config("cameraConfig.maxZoomHeight")
    private int maxZoomHeight = 20;

    @Config("cameraConfig.minZoomHeight")
    private int minZoomHeight = 1;

    @Config("cameraConfig.playerPitchCameraRotation")
    private float playerPitchCameraRotation = 60;

    @Config("cameraConfig.playerYawCameraRotation")
    private float playerYawCameraRotation = 90;

    public Camera(Player player, Plugin plugin) {
        this.player = player;
    }


    public void enable() {
        Location loc = player.getLocation().add(0, 15, 0);

        this.camera = loc.getWorld().spawn(loc, ArmorStand.class, stand -> {
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.addScoreboardTag("camera");
            stand.setCustomName("camera");
            stand.setCustomNameVisible(false);
        });

        // Lock player state
        player.addPotionEffect(
                new PotionEffect(
                        PotionEffectType.INVISIBILITY,
                        PotionEffect.INFINITE_DURATION,
                        0,
                        false,
                        false
                )
        );

        player.addScoreboardTag("inTopDown");

        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(camera);

        // Apply initial camera orientation correctly (WORLD rotation!)
        Location camLoc = camera.getLocation();
        camLoc.setYaw(playerYawCameraRotation);
        camLoc.setPitch(playerPitchCameraRotation); // pitch is NOT stored on armor stand
        camera.teleport(camLoc);
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
