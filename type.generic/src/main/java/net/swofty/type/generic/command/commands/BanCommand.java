package net.swofty.type.generic.command.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.*;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.objects.punishment.PunishPlayerProtocolObject;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentTag;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.commons.punishment.template.BanType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@CommandParameters(
        aliases = "ban tempban banip tempbanip",
        permission = Rank.STAFF,
        description = "Ban a player from the server.",
        usage = "/ban <player> [duration] <reason>",
        allowsConsole = false
)
public class BanCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString playerArg = ArgumentType.String("player");
        ArgumentString durationArg = ArgumentType.String("duration");
        Argument<String> reasonArg = ArgumentType.String("reason").setSuggestionCallback((sender, context, suggestion) -> {
            for (BanType type : BanType.values()) {
                suggestion.addEntry(new SuggestionEntry(type.name(), Component.text("§c" + type.getReason() + " §7| Wipe: " + type.isWipe())));
            }
        });
        Argument<String[]> extraArg = ArgumentType.StringArray("extra").setSuggestionCallback((sender, context, suggestion) -> {
            for (PunishmentTag tag : PunishmentTag.values()) {
                suggestion.addEntry(new SuggestionEntry("-" + tag.getShortCode(), Component.text("§e" + tag.getShortCode() + " §7| " + (tag.getDescription() != null ? tag.getDescription() : "No description"))));
            }
        });

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String playerName = context.get(playerArg);
            String duration = context.get(durationArg);

            BanType type;
            try {
                type = BanType.valueOf(context.get(reasonArg));
            } catch (IllegalArgumentException e) {
                player.sendMessage("§cInvalid ban reason. Use tab-completion to see valid options.");
                return;
            }

            CompletableFuture.runAsync(() -> {
                try {
                    UUID targetUuid = net.minestom.server.utils.mojang.MojangUtils.getUUID(playerName);
                    long actualTime = StringUtility.parseDuration(duration);
                    long expiryTime = System.currentTimeMillis() + actualTime;
                    banPlayer(player, targetUuid, type, player.getUuid(), actualTime, expiryTime, playerName, null);
                } catch (IOException e) {
                    player.sendMessage("§cCould not find player: " + playerName);
                }
            });
        }, playerArg, durationArg, reasonArg);

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String playerName = context.get(playerArg);

            BanType reason;
            try {
                reason = BanType.valueOf(context.get(reasonArg));
            } catch (IllegalArgumentException e) {
                player.sendMessage("§cInvalid ban reason. Use tab-completion to see valid options.");
                return;
            }

            CompletableFuture.runAsync(() -> {
                try {
                    banPlayer(player, net.minestom.server.utils.mojang.MojangUtils.getUUID(playerName), reason,
                            player.getUuid(), 0, -1, playerName, null);
                } catch (IOException e) {
                    player.sendMessage("§cCould not find player: " + playerName);
                }
            });
        }, playerArg, reasonArg);

        command.addSyntax((sender, context) -> {
            HypixelPlayer player = (HypixelPlayer) sender;
            String playerName = context.get(playerArg);

            BanType reason;
            try {
                reason = BanType.valueOf(context.get(reasonArg));
            } catch (IllegalArgumentException e) {
                player.sendMessage("§cInvalid ban reason. Use tab-completion to see valid options.");
                return;
            }

            List<PunishmentTag> tags = parseTags(List.of(context.get(extraArg)));

            CompletableFuture.runAsync(() -> {
                try {
                    banPlayer(player, net.minestom.server.utils.mojang.MojangUtils.getUUID(playerName), reason,
                            player.getUuid(), 0, -1, playerName, tags);
                } catch (IOException e) {
                    player.sendMessage("§cCould not find player: " + playerName);
                }
            });
        }, playerArg, reasonArg, extraArg);
    }

    private List<PunishmentTag> parseTags(List<String> rawTags) {
        List<PunishmentTag> tags = new ArrayList<>();
        for (String rawTag : rawTags) {
            if (rawTag.startsWith("-")) {
                String tagCode = rawTag.substring(1).toUpperCase();
                for (PunishmentTag tag : PunishmentTag.values()) {
                    if (tag.getShortCode().equalsIgnoreCase(tagCode)) {
                        tags.add(tag);
                        break;
                    }
                }
            }
        }
        return tags;
    }

    private void banPlayer(HypixelPlayer sender, UUID targetUuid, BanType type, UUID senderUuid,
                           long actualTime, long expiryTime, String playerName, @Nullable List<PunishmentTag> tags) {
        ProxyService punishmentService = new ProxyService(ServiceType.PUNISHMENT);
        PunishmentReason reason = new PunishmentReason(type);
        ArrayList<PunishmentTag> tagList = (tags != null) ? new ArrayList<>(tags) : new ArrayList<>();
        PunishPlayerProtocolObject.PunishPlayerMessage message = new PunishPlayerProtocolObject.PunishPlayerMessage(
                targetUuid,
                PunishmentType.BAN.name(),
                reason,
                senderUuid,
                tagList,
                actualTime > 0 ? expiryTime : -1
        );

        punishmentService.handleRequest(message).thenAccept(result -> {
            if (result instanceof PunishPlayerProtocolObject.PunishPlayerResponse response) {
                if (response.success()) {
                    new ProxyPlayer(targetUuid).transferToLimbo();
                    sender.sendMessage("§aSuccessfully banned player §e" + playerName + "§a. §8Punishment ID: §7" + response.punishmentId());
                } else if (response.errorCode() == PunishPlayerProtocolObject.ErrorCode.ALREADY_PUNISHED) {
                    sender.sendMessage("§cThis player is already banned. Use the tag -O to overwrite. Punishment ID: §7" + response.errorMessage());
                } else {
                    sender.sendMessage("§cFailed to ban player: " + response.errorMessage());
                }
            }
        }).orTimeout(5, TimeUnit.SECONDS).exceptionally(_ -> {
            sender.sendMessage("§cCould not ban this player at this time. The punishment service may be offline.");
            return null;
        });
    }
}
