package net.swofty.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.tinylog.Logger;

public class ActionPlayerJoin implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        RavengardPlayer player = (RavengardPlayer) event.getPlayer();

        Instance mainInstance = HypixelConst.getInstanceContainer();
        if (mainInstance == null) {
            mainInstance = HypixelConst.getEmptyInstance();
        }

        event.setSpawningInstance(mainInstance);
        player.setRespawnPoint(HypixelConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer())
        );

        Logger.info("Player {} joined Ravengard from {}", player.getUsername(), player.getOriginServer());
    }
}
