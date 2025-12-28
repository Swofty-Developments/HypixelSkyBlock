package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.Command;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@CommandParameters(
        aliases = "wikithis wikihand wikiinhand",
        description = "Shows page link of the item held towards the official Hypixel Wiki.",
        usage = "/wikithis",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class WikiThisCommand extends HypixelCommand {

    private static final String WIKI_BASE = "https://wiki.hypixel.net/";

    @Override
    public void registerUsage(MinestomCommand command) {

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (player.getItemInMainHand() == null || player.getItemInMainHand().isAir()) {
                player.sendMessage("§cYou must be holding an item to use this command!");
                return;
            }

            SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
            String name = item.getDisplayName();

            if (name == null || name.isBlank()) {
                player.sendMessage("§cThis item does not have a valid name.");
                return;
            }

            String clean = name.replaceAll("§.", "");
            String url = WIKI_BASE + clean.replace(" ", "_");

            Component line1 = Component.text("Found Item: ", NamedTextColor.GRAY)
                    .append(Component.text(clean, NamedTextColor.GREEN));

            Component here = Component.text("HERE", NamedTextColor.YELLOW, TextDecoration.BOLD)
                    .clickEvent(ClickEvent.openUrl(url));

            Component line2 = Component.text("Click ", NamedTextColor.GRAY)
                    .append(here)
                    .append(Component.text(" to find it on the ", NamedTextColor.GRAY))
                    .append(Component.text("Official SkyBlock Wiki", NamedTextColor.GOLD))
                    .append(Component.text("!", NamedTextColor.GRAY));

            player.sendMessage(line1);
            player.sendMessage(line2);
            player.getAchievementHandler().completeAchievement("skyblock.wow_thats_useful");
        });
    }
}
