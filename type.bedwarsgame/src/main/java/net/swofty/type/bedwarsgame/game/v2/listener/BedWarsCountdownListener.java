package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.commons.game.event.CountdownTickEvent;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

// todo: look like hypixel
public class BedWarsCountdownListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onCountdownTick(CountdownTickEvent event) {
        // Find the game for this event
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        if (game == null) return;

        // Only announce at specific intervals
        if (!event.shouldAnnounce()) return;

        Component message = createCountdownMessage(event.remainingSeconds());

        for (BedWarsPlayer player : game.getPlayers()) {
            player.sendMessage(message);
        }
    }

    private Component createCountdownMessage(int seconds) {
        if (seconds > 1) {
            return Component.text("Game starting in ", NamedTextColor.YELLOW)
                .append(Component.text(seconds, NamedTextColor.RED))
                .append(Component.text(" seconds!", NamedTextColor.YELLOW));
        } else if (seconds == 1) {
            return Component.text("Game starting in ", NamedTextColor.YELLOW)
                .append(Component.text("1", NamedTextColor.RED))
                .append(Component.text(" second!", NamedTextColor.YELLOW));
        } else {
            return Component.text("Game starting now!", NamedTextColor.GREEN);
        }
    }
}
