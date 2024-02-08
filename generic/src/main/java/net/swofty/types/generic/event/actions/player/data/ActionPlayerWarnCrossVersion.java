package net.swofty.types.generic.event.actions.player.data;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.commons.MinecraftVersion;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Warn the player of the effects of cross version if pre 1.20.4",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerWarnCrossVersion extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerSpawnEvent event = (PlayerSpawnEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!player.getVersion().isMoreRecentThan(MinecraftVersion.MINECRAFT_1_20_2)) {
            player.sendMessage(" ");
            player.sendMessage("§cAlthough we do support versions prior to 1.20.4, the experience may be buggy. If you experience a bug, test that it also happens on 1.20.4 before reporting it.");
            player.sendMessage(" ");
        }
    }
}
