package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.CommandSender;
import net.minestom.server.entity.Player;

import java.util.List;
import java.util.UUID;

public final class PunishmentCommands {

    private final PunishmentService service;

    public PunishmentCommands(PunishmentService service) {
        this.service = service;
    }

    public Command banCommand() {
        Command cmd = new Command("ban");

        ArgumentWord targetArg = ArgumentType.Word("player");
        ArgumentString reasonArg = ArgumentType.String("reason");

        cmd.addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player staff)) {
                sender.sendMessage("§cPlayers only.");
                return;
            }

            String targetName = ctx.get(targetArg);
            String reason = ctx.get(reasonArg);

            UUID targetUuid = resolveUuid(targetName);
            if (targetUuid == null) {
                sender.sendMessage("§cUnknown player: §e" + targetName);
                return;
            }

            service.ban(targetUuid, staff.getUuid(), reason);

            sender.sendMessage("§aBanned §e" + targetName + "§a for: §f" + reason);
        }, targetArg, reasonArg);

        return cmd;
    }

    public Command tempbanCommand() {
        Command cmd = new Command("tempban");

        ArgumentWord targetArg = ArgumentType.Word("player");
        ArgumentWord durationArg = ArgumentType.Word("duration"); // 7d, 3h, etc
        ArgumentString reasonArg = ArgumentType.String("reason");

        cmd.addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player staff)) {
                sender.sendMessage("§cPlayers only.");
                return;
            }

            String targetName = ctx.get(targetArg);
            String durationStr = ctx.get(durationArg);
            String reason = ctx.get(reasonArg);

            UUID targetUuid = resolveUuid(targetName);
            if (targetUuid == null) {
                sender.sendMessage("§cUnknown player: §e" + targetName);
                return;
            }

            long durationMs;
            try {
                durationMs = DurationParser.parseToMillis(durationStr);
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§c" + e.getMessage());
                return;
            }

            service.tempban(targetUuid, staff.getUuid(), reason, durationMs);

            sender.sendMessage("§aTempbanned §e" + targetName + "§a for §e" + durationStr + "§a: §f" + reason);
        }, targetArg, durationArg, reasonArg);

        return cmd;
    }

    public Command bansCommand() {
        Command cmd = new Command("bans");

        ArgumentWord targetArg = ArgumentType.Word("player");
        ArgumentWord flagArg = ArgumentType.Word("flag"); // -a

        // /bans <player>
        cmd.addSyntax((sender, ctx) -> {
            String targetName = ctx.get(targetArg);

            UUID targetUuid = resolveUuid(targetName);
            if (targetUuid == null) {
                sender.sendMessage("§cUnknown player: §e" + targetName);
                return;
            }

            List<PunishmentRecord> history = service.getHistoryNewestFirst(targetUuid);

            sender.sendMessage(LynxPunishmentFormatter.formatBansHeader(targetName, 1, 1));

            int i = 1;
            for (PunishmentRecord rec : history) {
                if (rec.getType() != PunishmentType.BAN && rec.getType() != PunishmentType.TEMPBAN) continue;

                String bannerName = resolveName(rec.getBannerUuid());
                if (bannerName == null) bannerName = "Console";

                sender.sendMessage(LynxPunishmentFormatter.formatBansLine(i++, rec, bannerName));
            }
        }, targetArg);

        // /bans -a <player>
        cmd.addSyntax((sender, ctx) -> {
            String flag = ctx.get(flagArg);
            if (!flag.equalsIgnoreCase("-a")) {
                sender.sendMessage("§cUsage: /bans -a <player>");
                return;
            }

            String targetName = ctx.get(targetArg);

            UUID targetUuid = resolveUuid(targetName);
            if (targetUuid == null) {
                sender.sendMessage("§cUnknown player: §e" + targetName);
                return;
            }

            List<PunishmentRecord> history = service.getHistoryNewestFirst(targetUuid);
            for (PunishmentRecord rec : history) {
                if (rec.getType() != PunishmentType.BAN && rec.getType() != PunishmentType.TEMPBAN) continue;
                sender.sendMessage(LynxPunishmentFormatter.formatBansA(rec));
            }
        }, flagArg, targetArg);

        return cmd;
    }

    // ------------------------------------------------------------
    // TODO: replace these two with your own lookup systems
    // ------------------------------------------------------------

    private UUID resolveUuid(String name) {
        // Must return UUID for offline players too if you want full history.
        return null;
    }

    private String resolveName(UUID uuid) {
        return null;
    }
}
