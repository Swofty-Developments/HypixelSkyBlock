package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.events.custom.BedDestroyedEvent;
import net.swofty.type.bedwarsgame.events.custom.BedWarsGameEventAdvanceEvent;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class BedWarsGameEventListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onBedDestroyed(BedDestroyedEvent event) {
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        if (game == null) return;

        // Announce bed destruction
        String teamColor = event.teamKey().chatColor();
        String teamName = event.teamKey().getName();
        BedWarsPlayer destroyer = event.destroyer();

        // todo: look like hypixel
        Component message;
        if (destroyer != null) {
            message = Component.text("BED DESTRUCTION > ", NamedTextColor.GRAY)
                .append(Component.text(teamColor + teamName + "'s ", NamedTextColor.WHITE))
                .append(Component.text("bed was destroyed by ", NamedTextColor.GRAY))
                .append(Component.text(destroyer.getUsername(), NamedTextColor.WHITE))
                .append(Component.text("!", NamedTextColor.GRAY));
        } else {
            message = Component.text("BED DESTRUCTION > ", NamedTextColor.GRAY)
                .append(Component.text(teamColor + teamName + "'s ", NamedTextColor.WHITE))
                .append(Component.text("bed was destroyed!", NamedTextColor.GRAY));
        }

        Audience audience = Audience.audience(game.getPlayers());
        audience.sendMessage(message);

        // Play wither death sound for all players
        for (BedWarsPlayer player : game.getPlayers()) {
            player.playSound(Sound.sound(Key.key("minecraft:entity.wither.death"),
                Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());
        }
    }

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameEventAdvance(BedWarsGameEventAdvanceEvent event) {
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        if (game == null) return;

        // TODO: make this look like Hypixel
        Component message = Component.text(event.currentEvent(), NamedTextColor.GREEN)
            .append(Component.text(" has started!", NamedTextColor.YELLOW));

        Audience.audience(game.getPlayers()).sendMessage(message);

        // update displays
        game.getGeneratorManager().updateDisplaysForEventChange();
    }
}
