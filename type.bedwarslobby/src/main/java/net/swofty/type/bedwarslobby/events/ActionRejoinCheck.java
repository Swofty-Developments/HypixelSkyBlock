package net.swofty.type.bedwarslobby.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.RejoinGameProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;

public class ActionRejoinCheck implements HypixelEventClass {
    private static final ProxyService ORCHESTRATOR = new ProxyService(ServiceType.ORCHESTRATOR);

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Delay slightly to ensure player is fully spawned
        MathUtility.delay(() -> {
            // Check if player has an active game to rejoin
            RejoinGameProtocolObject.RejoinGameRequest request =
                    new RejoinGameProtocolObject.RejoinGameRequest(player.getUuid());

            ORCHESTRATOR.handleRequest(request).thenAccept(response -> {
                if (response instanceof RejoinGameProtocolObject.RejoinGameResponse resp
                        && resp.hasActiveGame()) {
                    // Show rejoin message
                    Component message = Component.text("You have a game currently ongoing! ", NamedTextColor.RED)
                            .append(Component.text("Click here to rejoin.", NamedTextColor.RED)
                                    .clickEvent(ClickEvent.runCommand("/rejoin")));

                    player.sendMessage(message);
                }
            }).exceptionally(throwable -> {
                // Silently fail - don't bother the player with errors on this check
                return null;
            });
        }, 500); // 500ms delay
    }
}
