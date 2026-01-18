package net.swofty.type.generic.command.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.mojang.MojangUtils;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.objects.punishment.PunishPlayerProtocolObject;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.commons.punishment.template.MuteType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@CommandParameters(
        aliases = "mute tempmute",
        permission = Rank.STAFF,
        description = "Mute a player from the server.",
        usage = "/mute <player> [duration] <reason>",
        allowsConsole = true
)
public class MuteCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString playerArg = ArgumentType.String("player");
        ArgumentString durationArg = ArgumentType.String("duration");
        Argument<String> reasonArg = ArgumentType.String("reason").setSuggestionCallback((sender, context, suggestion) -> {
            for (MuteType type : MuteType.values()) {
                suggestion.addEntry(new SuggestionEntry(type.name(), Component.text("§c" + type.getReason())));
            }
        });

        command.addSyntax((sender, context) -> {
            String playerName = context.get(playerArg);
            String duration = context.get(durationArg);
            MuteType type = MuteType.valueOf(context.get(reasonArg));


            UUID targetUuid;
            try {
                targetUuid = MojangUtils.getUUID(playerName);
                sender.sendMessage("§8Processing mute for player §e" + playerName + "§7... (" + targetUuid + ")");
            } catch (IOException e) {
                sender.sendMessage("§cCould not find player: " + playerName);
                return;
            }

            UUID senderUuid;
            if (sender instanceof Player player) {
                senderUuid = player.getUuid();
            } else {
                senderUuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
            }

            long actualTime = StringUtility.parseDuration(duration);
            long expiryTime = System.currentTimeMillis() + actualTime;

            CompletableFuture.runAsync(() -> {
                mutePlayer(sender, targetUuid, type, senderUuid, actualTime, expiryTime, playerName);
            });
        }, playerArg, durationArg, reasonArg);

        // permanent mute
        command.addSyntax((sender, context) -> {
            String playerName = context.get(playerArg);
            MuteType reason = MuteType.valueOf(context.get(reasonArg));

            CompletableFuture.runAsync(() -> {
                try {
                    mutePlayer(sender,
                            MojangUtils.getUUID(playerName),
                            reason,
                            sender instanceof Player player ? player.getUuid() : UUID.fromString("00000000-0000-0000-0000-000000000000"),
                            0,
                            -1,
                            playerName);
                } catch (IOException e) {
                    sender.sendMessage("§cCould not find player: " + playerName);
                }
            });


        }, playerArg, reasonArg);
    }

    private void mutePlayer(CommandSender sender, UUID targetUuid, MuteType type, UUID senderUuid, long actualTime, long expiryTime, String playerName) {
        ProxyService punishmentService = new ProxyService(ServiceType.PUNISHMENT);
        PunishmentReason reason = new PunishmentReason(type);
        PunishPlayerProtocolObject.PunishPlayerMessage message = new PunishPlayerProtocolObject.PunishPlayerMessage(
                targetUuid,
                PunishmentType.MUTE.name(),
                reason,
                senderUuid,
                actualTime > 0 ? expiryTime : -1
        );

        punishmentService.handleRequest(message).thenAccept(result -> {
            if (result instanceof PunishPlayerProtocolObject.PunishPlayerResponse response) {
                if (response.success()) {
                    sender.sendMessage("§aSuccessfully muted player §e" + playerName + "§a. §8Punishment ID: §7" + response.punishmentId());
                } else {
                    sender.sendMessage("§cFailed to mute player: " + response.errorMessage());
                }
            }
        }).orTimeout(5, TimeUnit.SECONDS).exceptionally(_ -> {
            sender.sendMessage("§cCould not mute this player at this time. The punishment service may be offline.");
            return null;
        });
    }
}
