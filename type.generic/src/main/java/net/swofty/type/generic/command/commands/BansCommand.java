package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.handlers.PunishmentRecord;
import net.swofty.type.generic.data.handlers.PunishmentService;
import net.swofty.type.generic.data.handlers.PunishmentType;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.List;
import java.util.UUID;

@CommandParameters(
        aliases = "bans",
        description = "View bans for a player",
        usage = "/bans -a <player>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class BansCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord flagArg = ArgumentType.Word("flag");
        ArgumentWord playerArg = ArgumentType.Word("player");

        // /bans -a <player>
        command.addSyntax((sender, ctx) -> {
            if (!permissionCheck(sender)) return;

            String flag = ctx.get(flagArg);
            String targetName = ctx.get(playerArg);

            if (!flag.equalsIgnoreCase("-a")) {
                sender.sendMessage("§cUsage: /bans -a <player>");
                return;
            }

            Thread.startVirtualThread(() -> {
                try {
                    UUID targetUuid = resolveUuid(targetName);
                    if (targetUuid == null) {
                        sender.sendMessage("§cPlayer §e" + targetName + " §cnot found.");
                        return;
                    }

                    // pull BAN + TEMPBAN history (you can extend later)
                    List<PunishmentRecord> list = PunishmentService.getPunishments(
                            targetUuid,
                            PunishmentType.BAN,
                            PunishmentType.TEMPBAN
                    );

                    // Output lines Lynx parses:
                    // "[Lynx] <data>" where <data> matches:
                    // .*?Ω(true|false)Ω(true|false)Ω(null|\d+)Ω<uuid>Ω\d+
                    for (PunishmentRecord rec : list) {
                        // Lynx expects ONLY punished entries; cancelled entries will be filtered client-side
                        sender.sendMessage("[Lynx] " + rec.toLynxWireString());
                    }

                    // It’s OK to output nothing if no history — Lynx handles empty.
                } catch (Exception e) {
                    sender.sendMessage("§cFailed to fetch bans: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        }, flagArg, playerArg);
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
