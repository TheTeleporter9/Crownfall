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
    private final int maxZoomHeight = 120;
    private final int minZoomHeight = 10;

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

        Vector velocity = new Vector(-sideways, 0, -forward);

        if (velocity.lengthSquared() > 0) {
            velocity.normalize().multiply(moveSpeed);
        }

        velocity.setY(camera.getVelocity().getY());
        camera.setVelocity(velocity);
    }

    public void zoom(boolean zoomOut, Crownfall plugin) {
        if (camera == null || !camera.isValid()) return;

        double currentY = camera.getLocation().getY();
        Vector velocity = camera.getVelocity();

        if(zoomOut && currentY < maxZoomHeight) {
            camera.setVelocity(velocity.setY(zoomSpeed));
        } else if (!zoomOut && currentY > minZoomHeight) {
            camera.setVelocity(velocity.setY(-zoomSpeed));
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (camera.isValid()) {
                camera.setVelocity(camera.getVelocity().setY(0));
            }
        }, 3L);

    }

    public ArmorStand getCamera() {
        return camera;
    }

}
