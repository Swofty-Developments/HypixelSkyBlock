package net.swofty.type.skyblockgeneric.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class Wiki {

    private static final String WIKI_BASE = "https://wiki.hypixel.net/";

    public static void wiki(SkyBlockPlayer player) {
        player.sendMessage(Component.text("§7Click §e§lHERE §7to visit the §6Official SkyBlock Wiki§7!§r")
            .clickEvent(ClickEvent.openUrl(WIKI_BASE)));
        player.getAchievementHandler().completeAchievement("skyblock.wow_thats_useful");
    }

    public static void wikiThis(SkyBlockPlayer player) {
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
    }

}
