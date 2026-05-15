package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol.Request;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol.Response;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class TypedGetPlayerDataHandler implements TypedServiceHandler<Request, Response> {

    private static final GetPlayerDataPushProtocol PROTOCOL = new GetPlayerDataPushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(message.playerUUID());
        if (player == null) {
            return Response.failure("Player not found on this server");
        }

        try {
            SkyBlockDataHandler.Data dataType = SkyBlockDataHandler.Data.fromKey(message.dataKey());
            if (dataType == null) {
                return Response.failure("Invalid data key: " + message.dataKey());
            }

            Object data = player.getSkyblockDataHandler().get(dataType, dataType.getType()).getValue();
            String serializedData = ((SkyBlockDatapoint) dataType.getDefaultDatapoint()).getSerializer().serialize(data);

            return Response.success(serializedData, System.currentTimeMillis());
        } catch (Exception e) {
            return Response.failure("Failed to get data: " + e.getMessage());
        }
    }
}
