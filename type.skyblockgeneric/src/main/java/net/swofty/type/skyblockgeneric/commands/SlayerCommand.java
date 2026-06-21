package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.slayer.SlayerService;
import net.swofty.type.skyblockgeneric.slayer.SlayerTier;
import net.swofty.type.skyblockgeneric.slayer.SlayerTierDefinition;
import net.swofty.type.skyblockgeneric.slayer.SlayerType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Starts and manages Slayer quests",
    usage = "/slayer <start|status|cancel>",
    permission = Rank.DEFAULT,
    allowsConsole = false)
public class SlayerCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<SlayerType> typeArgument = ArgumentType.Enum("type", SlayerType.class);
        ArgumentEnum<SlayerTier> tierArgument = ArgumentType.Enum("tier", SlayerTier.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            SlayerService.StartResult result = SlayerService.startQuest(
                player,
                context.get(typeArgument),
                context.get(tierArgument)
            );
            if (!result.success()) {
                player.sendMessage("§c" + result.message());
            }
        }, ArgumentType.Literal("start"), typeArgument, tierArgument);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            SlayerService.QuestStatus status = SlayerService.status(player);
            if (!status.active()) {
                player.sendMessage("§cYou do not have an active Slayer quest.");
                return;
            }

            SlayerTierDefinition tier = status.tier();
            if (tier == null) {
                player.sendMessage("§cYour active Slayer quest is no longer configured.");
                return;
            }

            player.sendMessage("§5§lSLAYER QUEST");
            player.sendMessage("§7Boss: §c" + tier.displayName(status.quest().type()));
            player.sendMessage("§7Progress: §e" + status.quest().combatXp() + "§8/§e" + tier.requiredCombatXp() + " Combat XP");
            player.sendMessage(status.quest().bossSpawned() ? "§cBoss spawned!" : "§7Kill matching mobs to summon your boss.");
        }, ArgumentType.Literal("status"));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            SlayerService.cancelQuest((SkyBlockPlayer) sender);
        }, ArgumentType.Literal("cancel"));
    }
}
