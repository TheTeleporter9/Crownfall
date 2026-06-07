package org.solocode.crownfall.Commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.solocode.crownfall.Player.Camera.CameraManager;

public class startCommand implements BasicCommand {
    private final CameraManager cameraManager;

    public startCommand(Object plugin, CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }


    @Override
    public void execute(CommandSourceStack sender, String[] args) {
//        if (!(sender instanceof Player player)) {
//            Bukkit.getLogger().warning("Only in game Player can use this command!");
//            return;
//        }
        Player player = (Player) sender.getExecutor();
        cameraManager.enableCamera(player);
        player.sendMessage("Camera enabled! Use WASD to move, Shift+Scroll to zoom.");
    }
}
