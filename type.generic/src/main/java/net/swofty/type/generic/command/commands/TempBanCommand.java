package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.handlers.PunishmentService;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;

@CommandParameters(
        aliases = "tempban",
        description = "Temporarily ban a player",
        usage = "/tempban <player> <duration> <reason...>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class TempBanCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord playerArg = ArgumentType.Word("player");
        ArgumentWord durationArg = ArgumentType.Word("duration");
        ArgumentStringArray reasonArg = ArgumentType.StringArray("reason");

        command.addSyntax((sender, ctx) -> {
            if (!permissionCheck(sender)) return;

            String targetName = ctx.get(playerArg);
            String durationStr = ctx.get(durationArg);
            String reason = String.join(" ", ctx.get(reasonArg));

            Thread.startVirtualThread(() -> {
                try {
                    UUID targetUuid = resolveUuid(targetName);
                    if (targetUuid == null) {
                        sender.sendMessage("§cPlayer §e" + targetName + " §cnot found.");
                        return;
                    }

                    long durationMs = PunishmentService.parseDurationMs(durationStr);

                    UUID bannerUuid = (sender instanceof net.minestom.server.entity.Player p)
                            ? p.getUuid()
                            : new UUID(0L, 0L);

                    PunishmentService.createTempBan(targetUuid, bannerUuid, durationMs, reason);

                    sender.sendMessage("§aTempbanned §e" + HypixelPlayer.getRawName(targetUuid) + "§a for §e" + durationStr + "§a.");
                } catch (Exception e) {
                    sender.sendMessage("§cFailed to tempban player: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        }, playerArg, durationArg, reasonArg);
    }

    private UUID resolveUuid(String name) {
        try {
            for (HypixelPlayer p : net.swofty.type.generic.HypixelGenericLoader.getLoadedPlayers()) {
                if (p.getUsername().equalsIgnoreCase(name)) return p.getUuid();
            }
        } catch (Exception ignored) {}

        return HypixelDataHandler.getPotentialUUIDFromName(name);
    }
}
