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
import org.solocode.betterConfig.BetterConfig;
import org.solocode.betterConfig.ConfigType;

import java.util.Map;


public class Camera {
    private final Player player;
    private ArmorStand camera;
    private BetterConfig config;

    private double moveSpeed;
    private double zoomSpeed;
    private int maxZoomHeight;
    private int minZoomHeight;
    private float playerPitchCameraRotation;
    private float playerYawCameraRotation;

    public Camera(Player player, Plugin plugin) {
        this.player = player;
        config = new BetterConfig(plugin);
       createCameraConfig();
    }

    void createCameraConfig() {
        config.createConfig(ConfigType.Custom, "cameraConfig");

        config.addDefault(
                "cameraConfig",
                Map.of(
                        "move-speed", 0.6,
                        "zoom-speed", 0.5,
                        "max-zoom-height", 20,
                        "min-zoom-height", 1,
                        "player-pitch-lock", 60,
                        "player-yaw-lock", 90
                )
        );

        config.saveConfig("cameraConfig");


        YamlConfiguration cfg = config.getConfig("cameraConfig");

        moveSpeed = cfg.getDouble("move-speed");
        zoomSpeed = cfg.getDouble("zoom-speed");
        maxZoomHeight = cfg.getInt("max-zoom-height");
        minZoomHeight = cfg.getInt("min-zoom-height");
        playerPitchCameraRotation = (float) cfg.getDouble("player-pitch-lock");
        playerYawCameraRotation = (float) cfg.getDouble("player-yaw-lock");
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
