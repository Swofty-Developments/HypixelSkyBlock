package net.swofty.commons.protocol.objects.guild;

import net.swofty.commons.guild.GuildData;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GetGuildProtocolObject extends ProtocolObject
        <GetGuildProtocolObject.GetGuildMessage,
                GetGuildProtocolObject.GetGuildResponse> {

    @Override
    public Serializer<GetGuildMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetGuildMessage value) {
                return new JSONObject().put("memberUUID", value.memberUUID.toString()).toString();
            }

            @Override
            public GetGuildMessage deserialize(String json) {
                return new GetGuildMessage(UUID.fromString(new JSONObject(json).getString("memberUUID")));
            }

            @Override
            public GetGuildMessage clone(GetGuildMessage value) {
                return new GetGuildMessage(value.memberUUID);
            }
        };
    }

    @Override
    public Serializer<GetGuildResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetGuildResponse value) {
                JSONObject json = new JSONObject();
                json.put("hasGuild", value.guild != null);
                if (value.guild != null) {
                    json.put("guild", GuildData.getStaticSerializer().serialize(value.guild));
                }
                return json.toString();
            }

            @Override
            public GetGuildResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                if (!obj.getBoolean("hasGuild")) {
                    return new GetGuildResponse(null);
                }
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GetGuildResponse(guild);
            }

            @Override
            public GetGuildResponse clone(GetGuildResponse value) {
                return new GetGuildResponse(value.guild);
            }
        };
    }

    public record GetGuildMessage(UUID memberUUID) { }
    public record GetGuildResponse(GuildData guild) { }
}
