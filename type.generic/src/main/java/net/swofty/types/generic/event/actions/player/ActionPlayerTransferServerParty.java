package net.swofty.types.generic.event.actions.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServiceType;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.proxyapi.ProxyService;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.party.PartyManager;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class ActionPlayerTransferServerParty implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        MathUtility.delay(() -> {
            ProxyService partyService = new ProxyService(ServiceType.PARTY);
            if (!partyService.isOnline().join()) {
                return;
            }
            PartyManager.switchPartyServer(player);
        }, 40);
    }
}
