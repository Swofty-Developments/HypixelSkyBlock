package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.data.UpdatePlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.UpdatePlayerDataPushProtocol.Request;
import net.swofty.commons.protocol.objects.data.UpdatePlayerDataPushProtocol.Response;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.commons.redis.RedisMessageContext;

public class UpdatePlayerDataHandler implements RedisMessageHandler<Request, Response> {

    private static final UpdatePlayerDataPushProtocol PROTOCOL = new UpdatePlayerDataPushProtocol();

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
            HypixelDataHandler.Data dataType = HypixelDataHandler.Data.fromKey(message.dataKey());
            if (dataType == null) {
                return Response.failure("Invalid data key: " + message.dataKey());
            }

            Datapoint datapoint = dataType.getDefaultDatapoint().getClass()
                    .getDeclaredConstructor(String.class).newInstance(message.dataKey());
            datapoint.deserializeValue(message.newData());
            datapoint.setUser(player.getSkyblockDataHandler()).setData(dataType);

            player.getSkyblockDataHandler().getDatapoints().put(message.dataKey(), datapoint);

            return Response.success(System.currentTimeMillis());
        } catch (Exception e) {
            return Response.failure("Failed to update data: " + e.getMessage());
        }
    }
}
