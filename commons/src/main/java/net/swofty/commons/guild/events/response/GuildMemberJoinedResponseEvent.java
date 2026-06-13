package net.swofty.commons.guild.events.response;

import lombok.Getter;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class GuildMemberJoinedResponseEvent extends GuildResponseEvent {
    private final UUID joiner;

    public GuildMemberJoinedResponseEvent(GuildData guild, UUID joiner) {
        super(guild);
        this.joiner = joiner;
    }

    @Override
    public Serializer<GuildMemberJoinedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildMemberJoinedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("joiner", value.joiner.toString());
                return json.toString();
            }

            @Override
            public GuildMemberJoinedResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildMemberJoinedResponseEvent(guild, UUID.fromString(obj.getString("joiner")));
            }

            @Override
            public GuildMemberJoinedResponseEvent clone(GuildMemberJoinedResponseEvent value) {
                return new GuildMemberJoinedResponseEvent(value.getGuild(), value.joiner);
            }
        };
    }
}
