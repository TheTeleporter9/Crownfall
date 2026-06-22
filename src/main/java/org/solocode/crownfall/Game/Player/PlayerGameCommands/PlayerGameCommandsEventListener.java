package org.solocode.crownfall.Game.Player.PlayerGameCommands;

import com.comphenix.protocol.PacketType;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerGameCommandsEventListener implements Listener {

    private PlayerCommands currentCommand = PlayerCommands.NONE;
    private Location pmttl;
    @EventHandler
    public void onPlayerItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.hasItem()) {
            switch (event.getAction()) {
                case LEFT_CLICK_AIR -> {
                    return;
                }
                case LEFT_CLICK_BLOCK -> {
                    assert event.getItem() != null;
                    //ignore this horrific ifstatment, I am to lazy to fix this until it breaks, then I am going to be annoyed! Hours wasted: 1
                    if (java.util.Optional.ofNullable(event.getItem())
                            .map(i -> i.getData(DataComponentTypes.CUSTOM_NAME))
                            .filter(name -> name.contains(Component.text("Move Selected Troop to Location")))
                            .isPresent()) {
                        currentCommand = PlayerCommands.TROOP_MOVE_TO;
                        pmttl = event.getClickedBlock().getLocation();
                    }
                }
            }
        }
    }

    public Location getPlayerMoveToTroopLocation() {
        return pmttl; //the pmttl name is a abreviation of this function. Kill me, this is not a public domain and only for me to suffer!
    }

}
