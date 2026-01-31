package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.events.custom.BedDestroyedEvent;
import net.swofty.type.bedwarsgame.events.custom.BedWarsGameEventAdvanceEvent;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

// todo: look like hypixel
public class BedWarsGameEventListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onBedDestroyed(BedDestroyedEvent event) {
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        if (game == null) return;

        // Announce bed destruction
        String teamColor = event.teamKey().chatColor();
        String teamName = event.teamKey().getName();

        Component message;
        if (event.destroyerName() != null) {
            message = Component.text("BED DESTRUCTION > ", NamedTextColor.GRAY)
                    .append(Component.text(teamColor + teamName + "'s ", NamedTextColor.WHITE))
                    .append(Component.text("bed was destroyed by ", NamedTextColor.GRAY))
                    .append(Component.text(event.destroyerName(), NamedTextColor.WHITE))
                    .append(Component.text("!", NamedTextColor.GRAY));
        } else {
            message = Component.text("BED DESTRUCTION > ", NamedTextColor.GRAY)
                    .append(Component.text(teamColor + teamName + "'s ", NamedTextColor.WHITE))
                    .append(Component.text("bed was destroyed!", NamedTextColor.GRAY));
        }

        Audience.audience(game.getPlayers()).sendMessage(message);
    }

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameEventAdvance(BedWarsGameEventAdvanceEvent event) {
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        if (game == null) return;

        // Announce phase change
        Component message = Component.text("» ", NamedTextColor.YELLOW)
                .append(Component.text(event.currentEvent(), NamedTextColor.GREEN))
                .append(Component.text(" has started!", NamedTextColor.YELLOW));

        Audience.audience(game.getPlayers()).sendMessage(message);
    }
}
