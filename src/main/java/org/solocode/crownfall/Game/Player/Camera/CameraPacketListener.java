package org.solocode.crownfall.Game.Player.Camera;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.solocode.crownfall.Crownfall;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CameraPacketListener implements Listener {

    private final Crownfall plugin;
    private final Map<UUID, PlayerTopdownCamInputType> playerInputs = new HashMap<>();

    public CameraPacketListener(Crownfall plugin) {
        this.plugin = plugin;
    }

    public void register(ProtocolManager manager) {
        manager.addPacketListener(new PacketAdapter(
                plugin,
                ListenerPriority.HIGH,
                PacketType.Play.Client.STEER_VEHICLE
        ) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                Object input = event.getPacket().getModifier().read(0);

                try {
                    Class<?> clazz = input.getClass();

                    boolean forward = (boolean) clazz.getMethod("forward").invoke(input);
                    boolean backward = (boolean) clazz.getMethod("backward").invoke(input);
                    boolean left = (boolean) clazz.getMethod("left").invoke(input);
                    boolean right = (boolean) clazz.getMethod("right").invoke(input);
                    boolean jump = (boolean) clazz.getMethod("jump").invoke(input);
                    boolean shift = (boolean) clazz.getMethod("shift").invoke(input);

                    Player player = event.getPlayer();

                    if (player.getScoreboardTags().contains("inTopDown")) {
                        event.setCancelled(true);
                    }

                    PlayerTopdownCamInputType movement = PlayerTopdownCamInputType.NONE;

                    if (forward) {
                        movement = PlayerTopdownCamInputType.MOVE_FORWARD;
                    } else if (backward) {
                        movement = PlayerTopdownCamInputType.MOVE_BACKWARD;
                    } else if (left) {
                        movement = PlayerTopdownCamInputType.MOVE_LEFT;
                    } else if (right) {
                        movement = PlayerTopdownCamInputType.MOVE_RIGHT;
                    } else if (jump) {
                        movement = PlayerTopdownCamInputType.MOVE_JUMP;
                    } else if (shift) {
                        movement = PlayerTopdownCamInputType.SHIFT;
                    }

                    playerInputs.put(player.getUniqueId(), movement);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public PlayerTopdownCamInputType getPlayerMovement(Player player) {
        return playerInputs.getOrDefault(
                player.getUniqueId(),
                PlayerTopdownCamInputType.NONE
        );
    }

}