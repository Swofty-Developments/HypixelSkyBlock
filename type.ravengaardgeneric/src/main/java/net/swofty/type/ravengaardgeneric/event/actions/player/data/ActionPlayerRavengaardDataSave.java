package net.swofty.type.ravengaardgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.ravengaardgeneric.data.RavengaardDataHandler;
import net.swofty.type.ravengaardgeneric.user.RavengaardPlayer;
import org.tinylog.Logger;

import java.util.UUID;

public class ActionPlayerRavengaardDataSave implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        RavengaardPlayer player = (RavengaardPlayer) event.getPlayer();
        UUID playerUuid = player.getUuid();
        RavengaardDataHandler handler = RavengaardDataHandler.ravengaardCache.get(playerUuid);

        if (handler == null) {
            return;
        }

        Logger.info("Saving Ravengaard data for: {}", player.getUsername());

        handler.runOnSave(player);
        new UserDatabase(playerUuid).saveData(handler);
        RavengaardDataHandler.ravengaardCache.remove(playerUuid);

        Logger.info("Successfully saved Ravengaard data for: {}", player.getUsername());
    }
}
