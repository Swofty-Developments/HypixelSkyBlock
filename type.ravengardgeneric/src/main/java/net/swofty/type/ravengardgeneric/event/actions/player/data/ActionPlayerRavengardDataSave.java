package net.swofty.type.ravengardgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengardgeneric.data.RavengardDataHandler;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPlayerRavengardDataSave implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerDisconnectEvent event) {
        RavengardPlayer player = (RavengardPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();
        RavengardDataHandler handler = RavengardDataHandler.ravengardCache.get(playerUuid);

        if (handler == null) {
            return;
        }

        Logger.info("Saving Ravengard data for: {}", player.getUsername());

        handler.runOnSave(player);
        new UserDatabase(playerUuid).saveData(handler);
        RavengardDataHandler.ravengardCache.remove(playerUuid);

        Logger.info("Successfully saved Ravengard data for: {}", player.getUsername());
    }
}
