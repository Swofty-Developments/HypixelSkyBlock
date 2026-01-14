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
        aliases = "ban",
        description = "Ban a player permanently",
        usage = "/ban <player> <reason...>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class BanCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord playerArg = ArgumentType.Word("player");
        ArgumentStringArray reasonArg = ArgumentType.StringArray("reason");

        command.addSyntax((sender, ctx) -> {
            if (!permissionCheck(sender)) return;

            String targetName = ctx.get(playerArg);
            String reason = String.join(" ", ctx.get(reasonArg));

            Thread.startVirtualThread(() -> {
                try {
                    UUID targetUuid = resolveUuid(targetName);
                    if (targetUuid == null) {
                        sender.sendMessage("§cPlayer §e" + targetName + " §cnot found.");
                        return;
                    }

                    UUID bannerUuid = (sender instanceof net.minestom.server.entity.Player p)
                            ? p.getUuid()
                            : new UUID(0L, 0L);

                    PunishmentService.createBan(targetUuid, bannerUuid, reason);

                    sender.sendMessage("§aBanned §e" + HypixelPlayer.getRawName(targetUuid) + "§a permanently.");
                } catch (Exception e) {
                    sender.sendMessage("§cFailed to ban player: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        }, playerArg, reasonArg);
    }

    private UUID resolveUuid(String name) {
        // Try online name -> uuid first
        try {
            for (HypixelPlayer p : net.swofty.type.generic.HypixelGenericLoader.getLoadedPlayers()) {
                if (p.getUsername().equalsIgnoreCase(name)) return p.getUuid();
            }
        } catch (Exception ignored) {}

        // Fallback to DB lookup (your HypixelDataHandler already does this)
        return HypixelDataHandler.getPotentialUUIDFromName(name);
    }
}
