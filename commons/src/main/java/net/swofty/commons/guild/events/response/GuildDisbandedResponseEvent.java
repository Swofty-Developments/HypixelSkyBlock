package net.swofty.commons.guild.events.response;

import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GuildDisbandedResponseEvent extends GuildResponseEvent {
    private final UUID disbander;

    public GuildDisbandedResponseEvent(GuildData guild, UUID disbander) {
        super(guild);
        this.disbander = disbander;
    }

    public UUID getDisbander() { return disbander; }

    @Override
    public Serializer<GuildDisbandedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildDisbandedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("disbander", value.disbander.toString());
                return json.toString();
            }

            @Override
            public GuildDisbandedResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildDisbandedResponseEvent(guild, UUID.fromString(obj.getString("disbander")));
            }

            @Override
            public GuildDisbandedResponseEvent clone(GuildDisbandedResponseEvent value) {
                return new GuildDisbandedResponseEvent(value.getGuild(), value.disbander);
            }
        };
    }
}
