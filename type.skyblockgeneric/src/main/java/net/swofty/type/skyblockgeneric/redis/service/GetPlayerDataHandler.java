package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol.Request;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol.Response;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.commons.redis.RedisMessageContext;

public class GetPlayerDataHandler implements RedisMessageHandler<Request, Response> {

    private static final GetPlayerDataPushProtocol PROTOCOL = new GetPlayerDataPushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request message, RedisMessageContext context) {
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
