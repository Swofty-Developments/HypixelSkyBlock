package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildUnmuteRequestEvent extends GuildEvent {
    private final UUID unmuter;
    private final String target;

    public GuildUnmuteRequestEvent(UUID unmuter, String target) {
        super(null);
        this.unmuter = unmuter;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(unmuter);
    }

    @Override
    public Serializer<GuildUnmuteRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildUnmuteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("unmuter", value.unmuter.toString());
                json.put("target", value.target);
                return json.toString();
            }

            @Override
            public GuildUnmuteRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildUnmuteRequestEvent(
                    UUID.fromString(obj.getString("unmuter")),
                    obj.getString("target")
                );
            }

            @Override
            public GuildUnmuteRequestEvent clone(GuildUnmuteRequestEvent value) {
                return new GuildUnmuteRequestEvent(value.unmuter, value.target);
            }
        };
    }
}
