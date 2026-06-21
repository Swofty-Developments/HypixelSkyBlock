package net.swofty.service.guild.endpoints;

import net.swofty.commons.protocol.objects.guild.IsPlayerInGuildProtocolObject;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.service.guild.GuildCache;

public class IsPlayerInGuildEndpoint implements RedisMessageHandler<
    IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage,
    IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse> {

    @Override
    public IsPlayerInGuildProtocolObject protocol() {
        return new IsPlayerInGuildProtocolObject();
    }

    @Override
    public IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse handle(IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage messageObject, RedisMessageContext context) {
        return new IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse(
            GuildCache.isInGuild(messageObject.playerUUID())
        );
    }
}
