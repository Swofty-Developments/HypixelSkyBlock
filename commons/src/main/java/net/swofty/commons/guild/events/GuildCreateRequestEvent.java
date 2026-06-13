package net.swofty.commons.guild.events;

import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class GuildCreateRequestEvent extends GuildEvent {
    private final UUID creator;
    private final String guildName;

    public GuildCreateRequestEvent(UUID creator, String guildName) {
        super(null);
        this.creator = creator;
        this.guildName = guildName;
    }

    public UUID getCreator() { return creator; }
    public String getGuildName() { return guildName; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(creator);
    }

    @Override
    public Serializer<GuildCreateRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildCreateRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("creator", value.creator.toString());
                json.put("guildName", value.guildName);
                return json.toString();
            }

            @Override
            public GuildCreateRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildCreateRequestEvent(
                        UUID.fromString(obj.getString("creator")),
                        obj.getString("guildName")
                );
            }

            @Override
            public GuildCreateRequestEvent clone(GuildCreateRequestEvent value) {
                return new GuildCreateRequestEvent(value.creator, value.guildName);
            }
        };
    }
}
