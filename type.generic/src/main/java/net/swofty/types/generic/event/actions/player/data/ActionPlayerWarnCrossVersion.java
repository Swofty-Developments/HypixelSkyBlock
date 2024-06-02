package net.swofty.types.generic.event.actions.player.data;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.commons.MinecraftVersion;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerWarnCrossVersion implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!player.getVersion().isMoreRecentThan(MinecraftVersion.MINECRAFT_1_20_2)) {
            StringBuilder message = new StringBuilder();

            message.append(" \n");
            message.append("§6§l----------- §cServer Notice §6§l-----------\n");
            message.append("§cAlthough we do support versions prior to §61.20.4§c, the experience may be buggy.\n");
            message.append("§cIf you experience a bug, please test if it also occurs on §61.20.4§c before reporting it.\n");
            message.append("§6§l---------------------------------\n");
            message.append(" \n");

            player.sendMessage(message.toString());
        }
    }
}
