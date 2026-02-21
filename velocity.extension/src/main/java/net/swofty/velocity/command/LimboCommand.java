package net.swofty.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.TransferHandler;

public class LimboCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            return;
        }
        player.getCurrentServer().ifPresent((connection) -> {
            if (connection.getServer() == SkyBlockVelocity.getLimboServer()) {
                for (int i = 0; i < 5; i++) {
                    player.sendMessage(Component.empty());
                }
                player.sendMessage(Component.text("§cThe lobby you attempted to join was full or offline."));
                player.sendMessage(Component.text("§eBecause of this, you were routed to Limbo, a subset of your own imagination."));
                player.sendMessage(Component.text("§dThis place doesn't exist anywhere, any you can stay here as long as you'd like."));
                player.sendMessage(Component.text("§6To return to \"reality\", use §b/lobby GAME."));
                player.sendMessage(Component.text("§cExamples: /lobbby, /lobby skywars, /lobby arcade"));
                player.sendMessage(Component.text("§4Watch out, though, as there are things that live in Limbo."));
                return;
            }

            TransferHandler transferHandler = new TransferHandler(player);
            transferHandler.sendToLimbo();
        });
    }
}
