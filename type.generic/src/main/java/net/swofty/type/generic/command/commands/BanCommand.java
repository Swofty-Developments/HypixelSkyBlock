package net.swofty.type.generic.command.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.*;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.mojang.MojangUtils;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.objects.punishment.PunishPlayerProtocolObject;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.commons.punishment.PunishmentTag;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.commons.punishment.template.BanType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@CommandParameters(
        aliases = "ban tempban banip tempbanip",
        permission = Rank.STAFF,
        description = "Ban a player from the server.",
        usage = "/ban <player> [duration] <reason>",
        allowsConsole = true
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
        }); // can be -O -U etc.

        command.addSyntax((sender, context) -> {
            String playerName = context.get(playerArg);
            String duration = context.get(durationArg);
            BanType type = BanType.valueOf(context.get(reasonArg));


            UUID targetUuid;
            try {
                targetUuid = MojangUtils.getUUID(playerName);
                sender.sendMessage("§8Processing ban for player §e" + playerName + "§7... (" + targetUuid + ")");
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
                banPlayer(sender, targetUuid, type, senderUuid, actualTime, expiryTime, playerName, null);
            });
        }, playerArg, durationArg, reasonArg);

        // permanent ban
        command.addSyntax((sender, context) -> {
            String playerName = context.get(playerArg);
            BanType reason = BanType.valueOf(context.get(reasonArg));

            CompletableFuture.runAsync(() -> {
                try {
                    banPlayer(sender,
                            MojangUtils.getUUID(playerName),
                            reason,
                            sender instanceof Player player ? player.getUuid() : UUID.fromString("00000000-0000-0000-0000-000000000000"),
                            0,
                            -1,
                            playerName, null);
                } catch (IOException e) {
                    sender.sendMessage("§cCould not find player: " + playerName);
                    return;
                }
            });
        }, playerArg, reasonArg);

        command.addSyntax((sender, context) -> {
            String playerName = context.get(playerArg);
            BanType reason = BanType.valueOf(context.get(reasonArg));
            List<PunishmentTag> tags = parseTags(List.of(context.get(extraArg)));

            CompletableFuture.runAsync(() -> {
                try {
                    banPlayer(sender,
                            MojangUtils.getUUID(playerName),
                            reason,
                            sender instanceof Player player ? player.getUuid() : UUID.fromString("00000000-0000-0000-0000-000000000000"),
                            0,
                            -1,
                            playerName, tags);
                } catch (IOException e) {
                    sender.sendMessage("§cCould not find player: " + playerName);
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

    private void banPlayer(CommandSender sender, UUID targetUuid, BanType type, UUID senderUuid, long actualTime, long expiryTime, String playerName, @Nullable List<PunishmentTag> tags) {
        if (tags != null && !tags.contains(PunishmentTag.OVERWRITE)) {
            Optional<PunishmentRedis.ActivePunishment> activePunishment = PunishmentRedis.getActive(targetUuid);
            AtomicBoolean alreadyBanned = new AtomicBoolean(false);
            activePunishment.ifPresent(punishment -> {
                PunishmentType t = PunishmentType.valueOf(punishment.type());
                if (t == PunishmentType.BAN) {
                    sender.sendMessage("§cThis player is already banned. If you want to replace this ban use the tag -O, Punishment ID: §7" + punishment.banId());
                    alreadyBanned.set(true);
                }
            });
            if (alreadyBanned.get()) {
                return;
            }
        }

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

        new ProxyPlayer(targetUuid).transferToLimbo();

        punishmentService.handleRequest(message).thenAccept(result -> {
            if (result instanceof PunishPlayerProtocolObject.PunishPlayerResponse response) {
                if (response.success()) {
                    sender.sendMessage("§aSuccessfully banned player §e" + playerName + "§a. §8Punishment ID: §7" + response.punishmentId());
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
