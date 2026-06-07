package org.solocode.crownfall.Player.Camera;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.solocode.crownfall.Crownfall;

public class CameraPacketListener {
    private static Crownfall plugin = new Crownfall();

    public CameraPacketListener(Crownfall plugin) {
        CameraPacketListener.plugin = plugin;}



    public static void register () {
        plugin.protocolManager.addPacketListener(new PacketAdapter(
                plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.STEER_VEHICLE
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                //Positive = Left (A)
                //Negative = Right (D)
                float sidewaysInput = packet.getFloat().read(0);

                //Positive = Forward (W)
                //Negative = Backwards (S)
                float forwardsInput = packet.getFloat().read(1);
            }
        });
    }

    public PlayerInputType
}
