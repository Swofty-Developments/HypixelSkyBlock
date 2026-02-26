package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.utils.mojang.MojangUtils;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.punishment.UnpunishPlayerProtocolObject;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import net.minestom.server.command.builder.arguments.Argument;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@CommandParameters(
        description = "Unmute a player on the server.",
        usage = "/unmute <player>",
        aliases = "unmute",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class UnMuteCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        Argument<String> playerArg = ArgumentType.String("player");

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String playerName = context.get(playerArg);

            CompletableFuture.runAsync(() -> {
                try {
                    UUID targetUuid = MojangUtils.getUUID(playerName);
                    ProxyService punishmentService = new ProxyService(ServiceType.PUNISHMENT);
                    UnpunishPlayerProtocolObject.UnpunishPlayerMessage message = new UnpunishPlayerProtocolObject.UnpunishPlayerMessage(
                            targetUuid, player.getUuid(), PunishmentType.MUTE.name()
                    );

                    punishmentService.handleRequest(message).thenAccept(result -> {
                        if (result instanceof UnpunishPlayerProtocolObject.UnpunishPlayerResponse response) {
                            if (response.success()) {
                                player.sendMessage("§aSuccessfully unmuted player: " + playerName);
                            } else {
                                player.sendMessage("§c" + response.errorMessage());
                            }
                        }
                    }).orTimeout(5, TimeUnit.SECONDS).exceptionally(_ -> {
                        player.sendMessage("§cCould not unmute this player at this time. The punishment service may be offline.");
                        return null;
                    });
                } catch (IOException e) {
                    player.sendMessage("§cCould not find player: " + playerName);
                }
            });
        }, playerArg);
    }
}
