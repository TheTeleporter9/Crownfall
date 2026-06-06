package org.solocode.crownfall.Commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.solocode.crownfall.Camera.CameraManager;

public class startCommand implements BasicCommand {
    /**
     * Executes the start command.
     *
     * @param source the command source
     * @param args   the command arguments
     */
    @Override
    public void execute(CommandSourceStack source, String[] args) {
        // Make sure the command was executed by a player
        if (!(source.getExecutor() instanceof Player player)) {
            source.getSender().sendMessage("Only players can use this command!");
            return;
        }

        player.addScoreboardTag("inGame");
        new CameraManager().setupCamera(player);

    }
}
