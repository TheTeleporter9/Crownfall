package org.solocode.crownfall.Commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.solocode.crownfall.Player.Camera.CameraManager;

import java.util.Collection;
import java.util.stream.Stream;

public class startCommand implements BasicCommand {
    private final CameraManager cameraManager;

    public startCommand(Object plugin, CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }


    @Override
    public void execute(CommandSourceStack sender, String[] args) {
        Player player = (Player) sender.getExecutor();

        if (args.length > 0 && args[0].equalsIgnoreCase("leave")) {
            assert player != null;
            cameraManager.disableCamera(player);
            player.sendMessage("Camera disabled!");
            return;
        }

        cameraManager.enableCamera(player);
        assert player != null;
        player.sendMessage("Camera enabled! Use WASD to move, Shift+Scroll to zoom.");
    }

    @Override
    public @NonNull Collection<String> suggest(CommandSourceStack sender, String[] args) {
        if (args.length == 0) {
            return java.util.List.of("leave");
        }

        if (args.length == 1) {
            return Stream.of("leave")
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        return java.util.List.of();
    }
}
