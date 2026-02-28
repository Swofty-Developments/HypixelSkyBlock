package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildDemoteRequestEvent extends GuildEvent {
    private final UUID demoter;
    private final UUID target;

    public GuildDemoteRequestEvent(UUID demoter, UUID target) {
        super(null);
        this.demoter = demoter;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(demoter, target);
    }

    @Override
    public Serializer<GuildDemoteRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildDemoteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("demoter", value.demoter.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public GuildDemoteRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildDemoteRequestEvent(
                        UUID.fromString(obj.getString("demoter")),
                        UUID.fromString(obj.getString("target"))
                );
            }

            @Override
            public GuildDemoteRequestEvent clone(GuildDemoteRequestEvent value) {
                return new GuildDemoteRequestEvent(value.demoter, value.target);
            }
        };
    }
}
