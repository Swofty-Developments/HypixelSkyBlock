package net.swofty.commons.protocol.objects.guild;

import net.swofty.commons.guild.GuildData;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class GetGuildProtocolObject extends RedisProtocol<GetGuildProtocolObject.GetGuildMessage, GetGuildProtocolObject.GetGuildResponse> {
    @Override
    public Serializer<GetGuildMessage> getSerializer() {
        return new JacksonSerializer<>(GetGuildMessage.class);
    }

    @Override
    public Serializer<GetGuildResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetGuildResponse.class);
    }

    public record GetGuildMessage(UUID memberUUID) {
    }

    public record GetGuildResponse(GuildData guild) {
    }
}
