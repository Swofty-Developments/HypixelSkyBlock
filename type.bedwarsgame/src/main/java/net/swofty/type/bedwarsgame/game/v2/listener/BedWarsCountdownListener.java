package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.CountdownTickEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

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
        if (seconds > 10) {
            return Component.text("§eThe game starts in " + seconds + " seconds!");
        } else if (seconds == 10) {
            return Component.text("§eThe game starts in §610§e seconds!");
        } else if (seconds > 0) {
            return Component.text("§eThe game starts in §c" + seconds + " §eseconds!");
        }
        return null;
    }
}
