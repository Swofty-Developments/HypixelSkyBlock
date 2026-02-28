package net.swofty.service.guild.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.guild.GetGuildProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.guild.GuildCache;

public class GetGuildEndpoint implements ServiceEndpoint<
    GetGuildProtocolObject.GetGuildMessage,
    GetGuildProtocolObject.GetGuildResponse> {

    @Override
    public GetGuildProtocolObject associatedProtocolObject() {
        return new GetGuildProtocolObject();
    }

    @Override
    public GetGuildProtocolObject.GetGuildResponse onMessage(
        ServiceProxyRequest message,
        GetGuildProtocolObject.GetGuildMessage messageObject) {
        return new GetGuildProtocolObject.GetGuildResponse(
            GuildCache.getGuildFromPlayer(messageObject.memberUUID())
        );
    }
}
