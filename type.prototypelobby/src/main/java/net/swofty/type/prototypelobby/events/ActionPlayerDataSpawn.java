package net.swofty.type.prototypelobby.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Send rank join message for non-default ranks
        Rank rank = player.getRank();
        if (rank == Rank.DEFAULT) return;

        for (HypixelPlayer onlinePlayer : HypixelGenericLoader.getLoadedPlayers()) {
            onlinePlayer.sendMessage(player.getFullDisplayName() + " §6joined the lobby!");
        }

        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("§f➔ §6§lWelcome to the Prototype Lobby"));
        player.sendMessage(Component.text("All games in this lobby are currently in development.", NamedTextColor.GREEN));
        player.sendMessage(Component.text("§eClick here to leave feedback! §f➤ §b§nhttps://hypixel.net/PTL")
            .clickEvent(ClickEvent.openUrl("https://hypixel.net/PTL")));
        player.sendMessage(Component.empty());
    }
}