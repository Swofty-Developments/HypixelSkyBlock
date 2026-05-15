package net.swofty.type.bedwarslobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.bedwarslobby.TypeBedWarsLobbyLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.minestom.server.entity.GameMode;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.SPAWN)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL); //noticed stayed in Spectator after Bedwars game end. This is Hacky, but I don't see a reason why gamemode shouldn't be enforced on lobby join. -petethepossum

        TypeBedWarsLobbyLoader.bedWarsLobbyMap.sendMapData(player);
    }
}
