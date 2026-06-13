package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildDisbandRequestEvent extends GuildEvent {
    private final UUID disbander;

    public GuildDisbandRequestEvent(UUID disbander) {
        super(null);
        this.disbander = disbander;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(disbander);
    }

    @Override
    public Serializer<GuildDisbandRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildDisbandRequestEvent value) {
                return new JSONObject().put("disbander", value.disbander.toString()).toString();
            }

            @Override
            public GuildDisbandRequestEvent deserialize(String json) {
                return new GuildDisbandRequestEvent(UUID.fromString(new JSONObject(json).getString("disbander")));
            }

            @Override
            public GuildDisbandRequestEvent clone(GuildDisbandRequestEvent value) {
                return new GuildDisbandRequestEvent(value.disbander);
            }
        };
    }
}
