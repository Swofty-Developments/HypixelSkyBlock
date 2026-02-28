package net.swofty.service.guild.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.guild.IsPlayerInGuildProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.guild.GuildCache;

public class IsPlayerInGuildEndpoint implements ServiceEndpoint<
        IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage,
        IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse> {

    @Override
    public IsPlayerInGuildProtocolObject associatedProtocolObject() {
        return new IsPlayerInGuildProtocolObject();
    }

    @Override
    public IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse onMessage(
            ServiceProxyRequest message,
            IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage messageObject) {
        return new IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse(
                GuildCache.isInGuild(messageObject.playerUUID())
        );
    }
}
