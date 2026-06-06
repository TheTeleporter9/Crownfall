package org.solocode.crownfall.Commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.solocode.crownfall.Camera.CameraManager;

import java.nio.Buffer;

public class startCommand implements BasicCommand {
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
