package net.swofty.type.skyblockgeneric.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStash;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(aliases = "clearstash",
        description = "Clear your stash",
        usage = "/clearstash",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class ClearStashCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord confirmArg = new ArgumentWord("confirm").from("confirm");

        // No args - show confirmation message
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            DatapointStash.PlayerStash stash = player.getStash();

            if (stash.isEmpty()) {
                player.sendMessage("§cYour stash is already empty!");
                return;
            }

            int totalItems = stash.getItemStashCount() + stash.getMaterialStashCount();

            player.sendMessage("§cWARNING: This action is irreversible and deletes all " +
                    totalItems + " stashed items. If you still wish to continue, click below.");
            player.sendMessage(Component.text("§eCLEAR STASH YES I AM SURE")
                    .clickEvent(ClickEvent.runCommand("/clearstash confirm")));
        });

        // With confirm argument - actually clear
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            DatapointStash.PlayerStash stash = player.getStash();

            if (stash.isEmpty()) {
                player.sendMessage("§cYour stash is already empty!");
                return;
            }

            stash.clear();
            player.sendMessage("§aYour stash has been cleared!");
        }, confirmArg);
    }
}
