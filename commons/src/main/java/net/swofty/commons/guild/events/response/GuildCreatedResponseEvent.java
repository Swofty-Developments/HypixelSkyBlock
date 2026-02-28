package net.swofty.commons.guild.events.response;

import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GuildCreatedResponseEvent extends GuildResponseEvent {
    private final UUID creator;

    public GuildCreatedResponseEvent(GuildData guild, UUID creator) {
        super(guild);
        this.creator = creator;
    }

    public UUID getCreator() { return creator; }

    @Override
    public Serializer<GuildCreatedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildCreatedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("creator", value.creator.toString());
                return json.toString();
            }

            @Override
            public GuildCreatedResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildCreatedResponseEvent(guild, UUID.fromString(obj.getString("creator")));
            }

            @Override
            public GuildCreatedResponseEvent clone(GuildCreatedResponseEvent value) {
                return new GuildCreatedResponseEvent(value.getGuild(), value.creator);
            }
        };
    }
}
