package net.swofty.type.skyblockgeneric.data;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.commons.data.SwoftyData;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public final class CoopSync {

    private CoopSync() {}

    public static void subscribe() {
        for (SkyBlockDataHandler.Data data : SkyBlockDataHandler.Data.values()) {
            if (data.coopField() == null) continue;
            SwoftyData.profile().subscribe(data.coopField(),
                    (coopId, oldValue, newValue, affected) -> apply(data, coopId, newValue));
        }
    }

    private static void apply(SkyBlockDataHandler.Data data, UUID coopId, String newValue) {
        if (newValue == null) return;
        for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
            SkyBlockDataHandler handler = player.getSkyblockDataHandler();
            if (handler == null) continue;

            UUID profileId = handler.getCurrentProfileId();
            UUID playerCoopId = SwoftyData.profile().getLinkKey(profileId, CoopLinks.COOP).orElse(profileId);
            if (!playerCoopId.equals(coopId)) continue;

            Datapoint<?> datapoint = handler.getDatapoint(data.getKey());
            if (datapoint == null) continue;
            datapoint.deserializeValue(newValue);

            Player online = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player.getUuid());
            if (online != null && data.onChange != null) {
                data.onChange.accept(online, datapoint);
            }
        }
    }
}
